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
package com.liucan.loda;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息
 * @author liucan
 */
@Data
public class Actor implements Serializable {
  /**
   * 用户id
   */
  private Integer actorId;
  /**
   * 用户名
   */
  private String firstName;

  private String lastName;

  /**
   * 对应的 film id 列表
   */
  private List<FilmActorMap> filmIds;

  private LocalDateTime lastUpdate;
}
