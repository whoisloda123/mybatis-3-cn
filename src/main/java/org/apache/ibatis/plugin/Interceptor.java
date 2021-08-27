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
package org.apache.ibatis.plugin;

import java.util.Properties;

/**
 * mybatis 拦截器接口，实现该接口可在拦截之后做一些额外处理，如打印sql日志，分页等等
 * 目前只能针对 mybatis 执行sql流程过程中的几个核心类和对应的方法拦截：
 * <ul>
 * <li>执行器 {@link org.apache.ibatis.executor.Executor}</li>
 * <li>statement处理器 {@link org.apache.ibatis.executor.statement.StatementHandler}</li>
 * <li>参数处理器 {@link org.apache.ibatis.executor.parameter.ParameterHandler}</li>
 * <li>查询结果处理器 {@link org.apache.ibatis.executor.resultset.ResultSetHandler}</li>
 * </ul>
 *
 * <p>需要在实现类上面通过 {@link Intercepts} 和 {@link Signature} 注解指定需要拦截的对象和方法
 * 使用方法：
 * <pre>
 *   {@code
 *    @Intercepts({@Signature(type = Executor.class, method = "query",
 *    args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
 *    public class ExamplePlugin implements Interceptor {
 *    }
 *   }
 * </pre>
 * @author Clinton Begin
 */
public interface Interceptor {

  /**
   * 拦截方法
   * @param invocation 被拦截的对象信息
   * @return 调用结果
   * @throws Throwable 若发生异常
   */
  Object intercept(Invocation invocation) throws Throwable;

  /**
   * 应用插件。如应用成功，则会创建目标对象的代理对象
   * @param target 目标对象
   * @return 应用的结果对象，可以是代理对象，也可以是 target 对象，也可以是任意对象。具体的，看代码实现
   */
  default Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  /**
   * 设置拦截器属性
   * @param properties 属性
   */
  default void setProperties(Properties properties) {
    // NOP
  }

}
