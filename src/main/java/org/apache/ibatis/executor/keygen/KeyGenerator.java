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
package org.apache.ibatis.executor.keygen;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.ParamNameResolver;

import java.sql.Statement;

/**
 * 主键生成器接口，用于生成insert语句之前/之后的主键生成,并赋值给入参对象
 * @author Clinton Begin
 */
public interface KeyGenerator {

  /**
   * 在 SQL 执行后设置自增键到入参中
   * @param executor 执行器
   * @param ms MappedStatement 对象
   * @param stmt Statement对象
   * @param parameter 入参对象，如果方法的入参为为单个基础对象且没有加 {@link Param} 注解
   *    在该参数为对象本身，否则该参数为 {@link MapperMethod.ParamMap}
   *    具体查看 {@link ParamNameResolver#getNamedParams(Object[])}
   */
  void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter);

  /**
   * 在 SQL 执行前设置自增键到入参中
   * @param executor 执行器
   * @param ms MappedStatement 对象
   * @param stmt Statement对象
   * @param parameter 入参对象，如果方法的入参为为单个基础对象且没有加 {@link Param} 注解
   *    在该参数为对象本身，否则该参数为 {@link MapperMethod.ParamMap}
   *    具体查看 {@link ParamNameResolver#getNamedParams(Object[])}
   */
  void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter);

}
