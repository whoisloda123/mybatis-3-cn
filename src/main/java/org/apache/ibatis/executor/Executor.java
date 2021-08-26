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
package org.apache.ibatis.executor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * sql核心执行器，所有sql执行都是调用该接口，{@link org.apache.ibatis.session.SqlSession} 里面用的就是该接口
 * @author Clinton Begin
 */
public interface Executor {

  /**
   * ResultHandler 空对象
   */
  @SuppressWarnings("rawtypes")
  ResultHandler NO_RESULT_HANDLER = null;

  /**
   * 更新或者插入或者删除
   * 由传入的 MappedStatement 的 SQL 所决定
   * @param ms sql语句对应的对象
   * @param parameter 入参对象
   * @return 更新结果数量
   * @throws SQLException 如果执行异常
   */
  int update(MappedStatement ms, Object parameter) throws SQLException;

  /**
   * 查询列表
   * @param ms sql语句对应的对象
   * @param parameter 入参对象
   * @param rowBounds 分页参数
   * @param resultHandler 查询结果处理器，可以为 {@code null}
   * @param cacheKey 一级/二级缓存key
   * @param boundSql 对应实际的sql语句对象
   * @param <E> 结果类型
   * @return 列表信息
   * @throws SQLException 如果执行异常
   */
  @SuppressWarnings("rawtypes")
  <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler,
                    CacheKey cacheKey, BoundSql boundSql) throws SQLException;

  /**
   * 查询列表
   * @param ms sql语句对应的对象
   * @param parameter 入参对象
   * @param rowBounds 分页参数
   * @param resultHandler 查询结果处理器，可以为 {@code null}
   * @param <E> 结果类型
   * @return 列表信息
   * @throws SQLException 如果执行异常
   */
  @SuppressWarnings("rawtypes")
  <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;

  /**
   * 查询，返回 Cursor 游标
   * @param ms sql语句对应的对象
   * @param parameter 入参对象
   * @param rowBounds 分页参数
   * @param <E> 结果类型
   * @return 如果执行异常
   * @throws SQLException 如果执行异常
   */
  <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException;

  /**
   * 刷入批处理语句
   * @return 批量结果
   * @throws SQLException 如果执行异常
   */
  List<BatchResult> flushStatements() throws SQLException;

  /**
   * 提交事务
   * @param required 是否事务提交
   * @throws SQLException 如果执行异常
   */
  void commit(boolean required) throws SQLException;

  /**
   * 回滚事务
   * @param required 是否回滚事务
   * @throws SQLException 如果执行异常
   */
  void rollback(boolean required) throws SQLException;

  /**
   * 创建 CacheKey 对象
   * @param ms sql语句对应的对象
   * @param parameterObject 入参对象
   * @param rowBounds 分页参数
   * @param boundSql 对应实际的sql语句对象
   * @return 缓存key对象
   */
  CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql);

  /**
   * 判断是否缓存
   * @param key 一级/二级缓存key
   * @param ms sql语句对应的对象
   * @return 是否缓存
   */
  boolean isCached(MappedStatement ms, CacheKey key);

  /**
   * 清除本地缓存
   */
  void clearLocalCache();

  /**
   * 延迟加载
   * @param ms sql语句对应的对象
   * @param resultObject resultObject
   * @param property property
   * @param key 一级/二级缓存key
   * @param targetType 目标类型
   */
  void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType);

  /**
   * 获得事务
   * @return 事务对象
   */
  Transaction getTransaction();

  /**
   * 关闭事务
   * @param forceRollback 是否强制还原
   */
  void close(boolean forceRollback);

  /**
   * 判断事务是否关闭
   * @return 是否关闭
   */
  boolean isClosed();

  /**
   * 设置包装的 Executor 对象
   * @param executor 执行器
   */
  void setExecutorWrapper(Executor executor);

}
