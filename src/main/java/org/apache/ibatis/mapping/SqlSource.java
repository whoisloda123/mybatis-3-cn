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

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.reflection.ParamNameResolver;

/**
 * Represents the content of a mapped statement read from an XML file or an
 * annotation. It creates the SQL that will be passed to the database out of the
 * input parameter received from the user.
 *
 * @author Clinton Begin
 */
public interface SqlSource {

	/**
	 * 根据传入的参数对象，返回 BoundSql 对象
	 *
   * @param parameterObject 入参对象，如果方法的入参为为单个基础对象且没有加 {@link Param} 注解
   *    在该参数为对象本身，否则该参数为 {@link MapperMethod.ParamMap}
   *    具体查看 {@link ParamNameResolver#getNamedParams(Object[])}
	 * @return BoundSql 对象
	 */
	BoundSql getBoundSql(Object parameterObject);

}
