/**
 *    Copyright 2009-2019 the original author or authors.
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

/**
 * @author Clinton Begin
 * @description 每次对数据库的操作，都会创建对应的Statement对象，执行完成后，关闭该Statement对象
 */
public class SimpleExecutor extends BaseExecutor {

	public SimpleExecutor(Configuration configuration, Transaction transaction) {
		super(configuration, transaction);
	}

	@Override
	public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
		Statement stmt = null;
		try {
			Configuration configuration = ms.getConfiguration();
			// 创建 StatementHandler 对象
			StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null);
			// 初始化 Statement 对象
			stmt = prepareStatement(handler, ms.getStatementLog());
			// 通过 StatementHandler 执行写操作
			return handler.update(stmt);
		} finally {
      // 关闭 Statement 对象
			closeStatement(stmt);
		}
	}

	@Override
	public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler,
			BoundSql boundSql) throws SQLException {
		Statement stmt = null;
		try {
			Configuration configuration = ms.getConfiguration();
			// 创建 StatementHandler 对象
			StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
			// 初始化 Statement 对象
			stmt = prepareStatement(handler, ms.getStatementLog());
			// 通过 StatementHandler 执行读操作
			return handler.query(stmt, resultHandler);
		} finally {
			// 关闭 Statement 对象
			closeStatement(stmt);
		}
	}

	@Override
	protected <E> Cursor<E> doQueryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds, BoundSql boundSql)
			throws SQLException {
		Configuration configuration = ms.getConfiguration();
		StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds, null, boundSql);
		Statement stmt = prepareStatement(handler, ms.getStatementLog());
		Cursor<E> cursor = handler.queryCursor(stmt);
		stmt.closeOnCompletion();
		return cursor;
	}

	@Override
	public List<BatchResult> doFlushStatements(boolean isRollback) {
		return Collections.emptyList();
	}

	private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException {
		Statement stmt;
		// 获得 Connection 对象，如果开启了 Debug 模式，则返回的是一个代理对象
		Connection connection = getConnection(statementLog);
		// 创建 Statement 或 PrepareStatement 对象
		stmt = handler.prepare(connection, transaction.getTimeout());
		// 往 Statement 中设置 SQL 语句上的参数，例如 PrepareStatement 的 ? 占位符
		handler.parameterize(stmt);
		return stmt;
	}

}