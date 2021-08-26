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
package org.apache.ibatis.executor.parameter;

import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * {@link PreparedStatement} 的参数处理器，将预编译好的sql里面的占位符？替换为真正的参数
 * @author Clinton Begin
 * @see DefaultParameterHandler
 */
public interface ParameterHandler {

  /**
   * 返回参数对象
   * @return 参数对象
   */
  Object getParameterObject();

  /**
   * 设置 {@code PreparedStatement} 的参数
   * @param ps prepared statement
   * @throws SQLException 如果设置异常
   */
  void setParameters(PreparedStatement ps) throws SQLException;

}
