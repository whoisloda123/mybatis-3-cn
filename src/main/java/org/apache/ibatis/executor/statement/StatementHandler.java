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
package org.apache.ibatis.executor.statement;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * SQL {@link Statement} 处理器，包括创建 {@link Statement} 对象
 * 预编译，设置参数，查询等，主要和jdbc有关系
 * @author Clinton Begin
 */
public interface StatementHandler {

  /**
   * 准备操作，创建 Statement 对象，如原始 {@link Statement} 和 预编译 {@link PreparedStatement}
   * 以及存储过程 {@link java.sql.CallableStatement}
   * @param connection Connection 对象
   * @param transactionTimeout 事务超时时间
   * @return Statement 对象
   * @throws SQLException 如果执行异常
   */
  Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException;

  /**
   * 设置 Statement 对象的参数，一般通过 {@link ParameterHandler#setParameters(PreparedStatement)}
   * 来进行对占位符？参数的设置
   * @param statement Statement 对象
   * @throws SQLException 如果执行异常
   */
  void parameterize(Statement statement) throws SQLException;

  /**
   * 添加 Statement 对象的批量操作
   * @param statement Statement 对象
   * @throws SQLException 如果执行异常
   */
  void batch(Statement statement) throws SQLException;

  /**
   * 执行写操作
   * @param statement Statement 对象
   * @return 影响的条数
   * @throws SQLException 如果执行异常
   */
  int update(Statement statement) throws SQLException;

  /**
   * 执行读操作
   * @param statement Statement 对象
   * @param resultHandler ResultHandler 对象，处理结果
   * @param <E> 泛型
   * @return 读取的结果
   * @throws SQLException 如果执行异常
   */
  <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;

  /**
   * 执行读操作，返回 Cursor 对象
   * @param statement Statement 对象
   * @param <E> 泛型
   * @return Cursor 对象
   * @throws SQLException 如果执行异常
   */
  <E> Cursor<E> queryCursor(Statement statement) throws SQLException;

  /**
   * 获取 {@link BoundSql}.
   * @return BoundSql 对象
   */
  BoundSql getBoundSql();

  /**
   * 返回 {@link ParameterHandler}.
   * @return ParameterHandler 对象
   */
  ParameterHandler getParameterHandler();

}
