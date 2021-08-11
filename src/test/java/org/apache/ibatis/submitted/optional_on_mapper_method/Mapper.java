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
package org.apache.ibatis.submitted.optional_on_mapper_method;

import org.apache.ibatis.annotations.Select;

import java.util.Optional;

public interface Mapper {

  @Select("select * from users where id = #{id}")
  Optional<User> getUserUsingAnnotation(Integer id);

  Optional<User> getUserUsingXml(Integer id);

}
