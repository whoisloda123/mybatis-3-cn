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

/**
 * sql执行器 {@link Executor} 的类型
 * @author Clinton Begin
 */
public enum ExecutorType {
  /**
   * 默认执行类型 {@link org.apache.ibatis.executor.SimpleExecutor}
   */
  SIMPLE,
  /**
   * 可重用执行器类型 {@link org.apache.ibatis.executor.ReuseExecutor}
   */
  REUSE,
  /**
   * 批量操作执行类型 {@link org.apache.ibatis.executor.BatchExecutor}
   */
  BATCH
}
