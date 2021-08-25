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
package org.apache.ibatis.session;

import org.apache.ibatis.executor.Executor;

import java.sql.Connection;

/**
 * Creates an {@link SqlSession} out of a connection or a DataSource
 * @author Clinton Begin
 */
public interface SqlSessionFactory {

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @return 用户操作sql接口
   */
  SqlSession openSession();

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @param autoCommit 事务是否自动提交,默认 {@code false}
   * @return 用户操作sql接口
   */
  SqlSession openSession(boolean autoCommit);

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @param connection 数据库连接对象
   * @return 用户操作sql接口
   */
  SqlSession openSession(Connection connection);

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @param level 事务隔离级别
   * @return 用户操作sql接口
   */
  SqlSession openSession(TransactionIsolationLevel level);

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @param execType {@link Executor} 的类型
   * @return 用户操作sql接口
   */
  SqlSession openSession(ExecutorType execType);

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @param execType {@link Executor} 的类型
   * @param autoCommit 事务是否自动提交,默认 {@code false}
   * @return 用户操作sql接口
   */
  SqlSession openSession(ExecutorType execType, boolean autoCommit);

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @param execType {@link Executor} 的类型
   * @param level 事务隔离级别
   * @return 用户操作sql接口
   */
  SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level);

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @param execType {@link Executor} 的类型
   * @param connection 数据库连接对象
   * @return 用户操作sql接口
   */
  SqlSession openSession(ExecutorType execType, Connection connection);

  /**
   * 创建sql主要操作接口 {@code SqlSession}
   * @return 用户操作sql接口
   */
  Configuration getConfiguration();

}
