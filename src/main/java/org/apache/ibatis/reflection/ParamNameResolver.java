/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.reflection;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 方法参数名称解析器
 * <p>会通过构造方法 {@link #ParamNameResolver(Configuration, Method)}
 * 将方法的参数的信息保存在 {@link #names} 里面key为参数索引顺序，value为参数名称
 *
 * <p>而 {@link #getNamedParams(Object[])} 方法则会将真正入参数据解析为 {@link ParamMap} （key为参数名称，value为真正数据）
 * 或对象本身（在只有一个参数且没有 {@code Param} 注解）
 *
 * @author Clinton Begin
 */
public class ParamNameResolver {

	private static final String GENERIC_NAME_PREFIX = "param";

	/**
	 * <p>
	 * The key is the index and the value is the name of the parameter.<br />
	 * The name is obtained from {@link Param} if specified. When {@link Param} is
	 * not specified, the parameter index is used. Note that this index could be
	 * different from the actual index when the method has special parameters (i.e.
	 * {@link RowBounds} or {@link ResultHandler}).
	 * </p>
	 * <ul>
	 * <li>aMethod(@Param("M") int a, @Param("N") int b) -&gt; {{0, "M"}, {1,
	 * "N"}}</li>
	 * <li>aMethod(int a, int b) -&gt; {{0, "0"}, {1, "1"}}</li>
	 * <li>aMethod(int a, RowBounds rb, int b) -&gt; {{0, "0"}, {2, "1"}}</li>
	 * </ul>
	 *
	 * 参数名映射,主要解析Mapper接口里面的方法参数
	 * KEY：参数顺序
	 * VALUE：参数名
	 */
	private final SortedMap<Integer, String> names;

	/**
	 * 是否有 {@link Param} 注解的参数
	 */
	private boolean hasParamAnnotation;

	public ParamNameResolver(Configuration config, Method method) {
	  // 获取方法的参数类型集合
		final Class<?>[] paramTypes = method.getParameterTypes();
		// 获取方法的参数上面的注解集合
		final Annotation[][] paramAnnotations = method.getParameterAnnotations();
		final SortedMap<Integer, String> map = new TreeMap<>();
		int paramCount = paramAnnotations.length;
		// get names from @Param annotations
		for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
			// 忽略 RowBounds、ResultHandler参数类型
			if (isSpecialParameter(paramTypes[paramIndex])) {
				// skip special parameters
				continue;
			}
			String name = null;
			// <1> 首先，从 @Param 注解中获取参数名
			for (Annotation annotation : paramAnnotations[paramIndex]) {
				if (annotation instanceof Param) {
					hasParamAnnotation = true;
					name = ((Param) annotation).value();
					break;
				}
			}
			if (name == null) {
				// @Param was not specified.
				// <2> 其次，获取真实的参数名
				if (config.isUseActualParamName()) { // 默认开启
					name = getActualParamName(method, paramIndex);
				}
				// <3> 最差，使用 map 的顺序，作为编号
				if (name == null) {
					// use the parameter index as the name ("0", "1", ...)
					// gcode issue #71
					name = String.valueOf(map.size());
				}
			}
			// 添加到 map 中
			map.put(paramIndex, name);
		}
		// 构建不可变的 SortedMap 集合
		names = Collections.unmodifiableSortedMap(map);
	}

	private String getActualParamName(Method method, int paramIndex) {
		return ParamNameUtil.getParamNames(method).get(paramIndex);
	}

	private static boolean isSpecialParameter(Class<?> clazz) {
		return RowBounds.class.isAssignableFrom(clazz) || ResultHandler.class.isAssignableFrom(clazz);
	}

	/**
	 * Returns parameter names referenced by SQL providers.
	 */
	public String[] getNames() {
		return names.values().toArray(new String[0]);
	}

  /**
   * <p>
   * A single non-special parameter is returned without a name. Multiple
   * parameters are named using the naming rule. In addition to the default names,
   * this method also adds the generic names (param1, param2, ...).
   * </p>
   * <p>
   * 根据参数值返回参数名称与参数值的映射关系
   * @param args 参数值数组
   * @return 参数名称与参数值的映射关系 {@link ParamMap}
   */
  public Object getNamedParams(Object[] args) {
		final int paramCount = names.size();
		// 无参数，则返回 null
		if (args == null || paramCount == 0) {
			return null;
		// 只有1个参数，并且没有 @Param 注解，则直接返回该值
		} else if (!hasParamAnnotation && paramCount == 1) {
			return args[names.firstKey()];
		} else {
		  /*
		   * 参数名称与值的映射，包含以下两种组合数据：
		   * 组合1：(参数名,值)
		   * 组合2：(param+参数顺序,值)
		   */
      final Map<String, Object> param = new ParamMap<>();
			int i = 0;
			for (Map.Entry<Integer, String> entry : names.entrySet()) {
				// 组合 1 ：添加到 param 中
				param.put(entry.getValue(), args[entry.getKey()]);
				// add generic param names (param1, param2, ...)
				final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
				// ensure not to overwrite parameter named with @Param
				if (!names.containsValue(genericParamName)) {
					// 组合 2 ：添加到 param 中
					param.put(genericParamName, args[entry.getKey()]);
				}
				i++;
			}
			return param;
		}
	}
}
