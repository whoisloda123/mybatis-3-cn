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
package org.apache.ibatis.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.ParamNameUtil;
import org.apache.ibatis.session.Configuration;


/**
 * 对应xml里面
 * {@code
 *  <resultMap><result></result> </resultMap>
 * }
 * resultMap标签
 * @author Clinton Begin
 */
public class ResultMap {
	/**
	 * Configuration 对象
	 */
	private Configuration configuration;

	/**
	 * ResultMap 对象的 id
   * 配置的 resultType 也会转换成 ResultMap 对象，其 id 就是 statementId + '-Inline'
	 */
	private String id;
	/**
	 * Java 类型
	 */
	private Class<?> type;
	/**
	 * ResultMapping 集合
	 */
	private List<ResultMapping> resultMappings;
	/**
	 * ID ResultMapping 集合
	 *
	 * 当 idResultMappings 为空时，使用 {@link #resultMappings} 赋值
	 */
	private List<ResultMapping> idResultMappings;
	/**
	 * 构造方法的入参 ResultMapping 集合
   * 根据参数名称已经排序好了
	 *
	 * 和 {@link #propertyResultMappings} 不存在相同元素
	 */
	private List<ResultMapping> constructorResultMappings;
	/**
	 * 属性 ResultMapping 集合
	 */
	private List<ResultMapping> propertyResultMappings;
	/**
	 * 配置的数据库的字段集合（全部大写）
	 */
	private Set<String> mappedColumns;
	/**
	 * Java 对象的属性集合
	 */
	private Set<String> mappedProperties;
	/**
	 * Discriminator 选择器对象
	 */
	private Discriminator discriminator;
	/**
	 * 是否有内嵌的 ResultMap
	 */
	private boolean hasNestedResultMaps;
	/**
	 * 是否有嵌套关联的子查询
	 */
	private boolean hasNestedQueries;
	/**
	 * 是否自定映射，默认为 null
	 * 全局 autoMappingBehavior 配置默认为 PARTIAL，只会自动映射没有定义嵌套结果映射的字段
	 */
	private Boolean autoMapping;

	private ResultMap() {
	}

	public static class Builder {
		private static final Log log = LogFactory.getLog(Builder.class);

		private ResultMap resultMap = new ResultMap();

		public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings) {
			this(configuration, id, type, resultMappings, null);
		}

		public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings,
				Boolean autoMapping) {
			resultMap.configuration = configuration;
			resultMap.id = id;
			resultMap.type = type;
			resultMap.resultMappings = resultMappings;
			resultMap.autoMapping = autoMapping;
		}

		public Builder discriminator(Discriminator discriminator) {
			resultMap.discriminator = discriminator;
			return this;
		}

		public Class<?> type() {
			return resultMap.type;
		}

		/**
		 * 构造 ResultMap 对象
		 *
		 * @return ResultMap 对象
		 */
		public ResultMap build() {
			if (resultMap.id == null) {
				throw new IllegalArgumentException("ResultMaps must have an id");
			}
			resultMap.mappedColumns = new HashSet<>();
			resultMap.mappedProperties = new HashSet<>();
			resultMap.idResultMappings = new ArrayList<>();
			resultMap.constructorResultMappings = new ArrayList<>();
			resultMap.propertyResultMappings = new ArrayList<>();
			final List<String> constructorArgNames = new ArrayList<>();
			for (ResultMapping resultMapping : resultMap.resultMappings) {
				// 是否有嵌套关联的子查询
				resultMap.hasNestedQueries = resultMap.hasNestedQueries || resultMapping.getNestedQueryId() != null;
				// 是否有内嵌的 ResultMap
				resultMap.hasNestedResultMaps = resultMap.hasNestedResultMaps || (resultMapping.getNestedResultMapId() != null && resultMapping.getResultSet() == null);
				final String column = resultMapping.getColumn();
				if (column != null) {
				  // 数据库的字段集合（全部大写）
					resultMap.mappedColumns.add(column.toUpperCase(Locale.ENGLISH));
				} else if (resultMapping.isCompositeResult()) {
					for (ResultMapping compositeResultMapping : resultMapping.getComposites()) {
						final String compositeColumn = compositeResultMapping.getColumn();
						if (compositeColumn != null) {
							resultMap.mappedColumns.add(compositeColumn.toUpperCase(Locale.ENGLISH));
						}
					}
				}
				final String property = resultMapping.getProperty();
				if (property != null) {
          // Java 对象的属性集合
					resultMap.mappedProperties.add(property);
				}
				if (resultMapping.getFlags().contains(ResultFlag.CONSTRUCTOR)) {
				  // 构造方法的入参 ResultMapping 集合
					resultMap.constructorResultMappings.add(resultMapping);
					if (resultMapping.getProperty() != null) {
					  // 构造方法的参数名称
						constructorArgNames.add(resultMapping.getProperty());
					}
				} else {
				  // 属性 ResultMapping 集合
					resultMap.propertyResultMappings.add(resultMapping);
				}
				// ID ResultMapping 集合
				if (resultMapping.getFlags().contains(ResultFlag.ID)) {
					resultMap.idResultMappings.add(resultMapping);
				}
			}

			if (resultMap.idResultMappings.isEmpty()) {
			  // 如果 ID ResultMapping 集合为空，则添加所有的 ResultMapping
				resultMap.idResultMappings.addAll(resultMap.resultMappings);
			}
			// 将 constructorResultMappings 排序成符合的构造方法的参数顺序
			if (!constructorArgNames.isEmpty()) {
				// 获得真正的构造方法的参数数组 actualArgNames
				final List<String> actualArgNames = argNamesOfMatchingConstructor(constructorArgNames);
				if (actualArgNames == null) {
					throw new BuilderException("Error in result map '" + resultMap.id
							+ "'. Failed to find a constructor in '" + resultMap.getType().getName() + "' by arg names "
							+ constructorArgNames + ". There might be more info in debug log.");
				}
				// 基于 actualArgNames ，将 constructorResultMappings 排序
				resultMap.constructorResultMappings.sort((o1, o2) -> {
					int paramIdx1 = actualArgNames.indexOf(o1.getProperty());
					int paramIdx2 = actualArgNames.indexOf(o2.getProperty());
					return paramIdx1 - paramIdx2;
				});
			}
			// lock down collections
			resultMap.resultMappings = Collections.unmodifiableList(resultMap.resultMappings);
			resultMap.idResultMappings = Collections.unmodifiableList(resultMap.idResultMappings);
			resultMap.constructorResultMappings = Collections.unmodifiableList(resultMap.constructorResultMappings);
			resultMap.propertyResultMappings = Collections.unmodifiableList(resultMap.propertyResultMappings);
			resultMap.mappedColumns = Collections.unmodifiableSet(resultMap.mappedColumns);
			return resultMap;
		}

		private List<String> argNamesOfMatchingConstructor(List<String> constructorArgNames) {
			// 获得所有构造方法
			Constructor<?>[] constructors = resultMap.type.getDeclaredConstructors();
			// 遍历所有构造方法
			for (Constructor<?> constructor : constructors) {
				Class<?>[] paramTypes = constructor.getParameterTypes();
				// 参数数量一致
				if (constructorArgNames.size() == paramTypes.length) {
					// 获得构造方法的参数名的数组
					List<String> paramNames = getArgNames(constructor);
					if (constructorArgNames.containsAll(paramNames) // 判断名字
							&& argTypesMatch(constructorArgNames, paramTypes, paramNames)) { // 判断类型
						return paramNames;
					}
				}
			}
			return null;
		}

		/**
		 * 判断构造方法的参数类型是否符合
		 *
		 * @param constructorArgNames 构造方法的参数名数组
		 * @param paramTypes          构造方法的参数类型数组
		 * @param paramNames          声明的参数名数组
		 * @return 是否符合
		 */
		private boolean argTypesMatch(final List<String> constructorArgNames, Class<?>[] paramTypes,
				List<String> paramNames) {
			// 遍历所有参数名
			for (int i = 0; i < constructorArgNames.size(); i++) {
				// 判断类型是否匹配
				Class<?> actualType = paramTypes[paramNames.indexOf(constructorArgNames.get(i))];
				Class<?> specifiedType = resultMap.constructorResultMappings.get(i).getJavaType();
				if (!actualType.equals(specifiedType)) {
					if (log.isDebugEnabled()) {
						log.debug("While building result map '" + resultMap.id
								+ "', found a constructor with arg names " + constructorArgNames + ", but the type of '"
								+ constructorArgNames.get(i) + "' did not match. Specified: [" + specifiedType.getName()
								+ "] Declared: [" + actualType.getName() + "]");
					}
					return false;
				}
			}
			return true;
		}

		/**
		 * 获得构造方法的参数名的数组
     * 如果需要在 <resultMap /> 中使用 <constructor />，需要在构造方法的参数上面都添加好 @Param 注解
		 *
		 * @param constructor 构造方法
		 * @return 参数名数组
		 */
		private List<String> getArgNames(Constructor<?> constructor) {
			List<String> paramNames = new ArrayList<>();
			List<String> actualParamNames = null;
			final Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
			int paramCount = paramAnnotations.length;
			for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
				String name = null;
				for (Annotation annotation : paramAnnotations[paramIndex]) {
					if (annotation instanceof Param) {
					  // 获取 @Param 注解的值，作为参数名称
						name = ((Param) annotation).value();
						break;
					}
				}
				if (name == null && resultMap.configuration.isUseActualParamName()) {
					if (actualParamNames == null) {
					  // 通过反射获取到构造方法的所有参数名称
						actualParamNames = ParamNameUtil.getParamNames(constructor);
					}
					if (actualParamNames.size() > paramIndex) {
						name = actualParamNames.get(paramIndex);
					}
				}
				paramNames.add(name != null ? name : "arg" + paramIndex);
			}
			return paramNames;
		}
	}

	public String getId() {
		return id;
	}

	public boolean hasNestedResultMaps() {
		return hasNestedResultMaps;
	}

	public boolean hasNestedQueries() {
		return hasNestedQueries;
	}

	public Class<?> getType() {
		return type;
	}

	public List<ResultMapping> getResultMappings() {
		return resultMappings;
	}

	public List<ResultMapping> getConstructorResultMappings() {
		return constructorResultMappings;
	}

	public List<ResultMapping> getPropertyResultMappings() {
		return propertyResultMappings;
	}

	public List<ResultMapping> getIdResultMappings() {
		return idResultMappings;
	}

	public Set<String> getMappedColumns() {
		return mappedColumns;
	}

	public Set<String> getMappedProperties() {
		return mappedProperties;
	}

	public Discriminator getDiscriminator() {
		return discriminator;
	}

	public void forceNestedResultMaps() {
		hasNestedResultMaps = true;
	}

	public Boolean getAutoMapping() {
		return autoMapping;
	}

}
