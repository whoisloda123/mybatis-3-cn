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

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * {@link SqlSessionFactory} builder类，使用mybatis时候，一般使用的第一个类
 * @author Clinton Begin
 */
public class SqlSessionFactoryBuilder {

  /**
   * 创建 {@link SqlSession} 的工厂类 {@code SqlSessionFactory}
   * @param reader reader流，配置文件来源
   * @return {@link SqlSession} 的工厂类
   */
  public SqlSessionFactory build(Reader reader) {
    return build(reader, null, null);
  }

  /**
   * 创建 {@link SqlSession} 的工厂类 {@code SqlSessionFactory}
   * @param reader reader流，配置文件来源
   * @param environment {@link Environment} 的环境id
   * @return {@link SqlSession} 的工厂类
   */
  public SqlSessionFactory build(Reader reader, String environment) {
    return build(reader, environment, null);
  }

  /**
   * 创建 {@link SqlSession} 的工厂类 {@code SqlSessionFactory}
   * @param reader reader流，配置文件来源
   * @param properties Properties 变量
   * @return {@link SqlSession} 的工厂类
   */
  public SqlSessionFactory build(Reader reader, Properties properties) {
    return build(reader, null, properties);
  }

  /**
   * 构造 SqlSessionFactory 对象
   *
   * @param reader Reader 对象，配置文件来源
   * @param environment {@link Environment} 的环境id
   * @param properties Properties 变量
   * @return SqlSessionFactory 对象
   */
	public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
		try {
			/*
			 * <1> 创建 XMLConfigBuilder 对象
			 * 会生成一个 XPathParser，包含 Document 对象
			 * 会创建一个 Configuration 全局配置对象
			 */
			XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
			/*
			 * <2> 解析 XML 文件并配置到 Configuration 全局配置对象中
			 * <3> 创建 DefaultSqlSessionFactory 对象
			 */
			return build(parser.parse());
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("Error building SqlSession.", e);
		} finally {
      ErrorContext.instance().reset();
      try {
        reader.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  /**
   * 创建 {@link SqlSession} 的工厂类 {@code SqlSessionFactory}
   * @param inputStream input流，配置文件来源
   * @return {@link SqlSession} 的工厂类
   */
  public SqlSessionFactory build(InputStream inputStream) {
    return build(inputStream, null, null);
  }

  /**
   * 创建 {@link SqlSession} 的工厂类 {@code SqlSessionFactory}
   * @param inputStream input流，配置文件来源
   * @param environment {@link Environment} 的环境id
   * @return {@link SqlSession} 的工厂类
   */
  public SqlSessionFactory build(InputStream inputStream, String environment) {
    return build(inputStream, environment, null);
  }

  /**
   * 创建 {@link SqlSession} 的工厂类 {@code SqlSessionFactory}
   * @param inputStream inputStream流
   * @param properties Properties 变量
   * @return {@link SqlSession} 的工厂类
   */
  public SqlSessionFactory build(InputStream inputStream, Properties properties) {
    return build(inputStream, null, properties);
  }

  /**
   * 创建 {@link SqlSession} 的工厂类 {@code SqlSessionFactory}
   * @param inputStream inputStream流，配置文件来源
   * @param properties Properties 变量
   * @param environment {@link Environment} 的环境id
   * @return {@link SqlSession} 的工厂类
   */
  public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    try {
      XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        inputStream.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
  }

  /**
   * 创建 {@link SqlSession} 的工厂类 {@code SqlSessionFactory}
   * @param config 配置类
   * @return {@link SqlSession} 的工厂类
   */
  public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
  }

}
