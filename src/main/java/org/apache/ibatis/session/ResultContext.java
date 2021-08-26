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

/**
 * 暂存映射后的返回行结果
 * @author Clinton Begin
 * @see org.apache.ibatis.executor.result.DefaultResultContext
 */
public interface ResultContext<T> {

  /**
   * 返回当前暂存映射对象
   * @return 当前暂存对象
   */
  T getResultObject();

  /**
   * 返回暂存过映射对象个数
   * @return 暂存过映射对象个数
   */
  int getResultCount();

  /**
   * 是否进行映射
   * @return 是否进行映射
   */
  boolean isStopped();

  /**
   * 控制是否进行映射
   */
  void stop();

}
