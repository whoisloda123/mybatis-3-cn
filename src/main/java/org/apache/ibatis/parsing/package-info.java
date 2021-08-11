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
/**
 * Parsing utils
 * 初始化时解析mybatis-config.xml配置文件、为处理动态SQL语句中占位符提供支持
 * 将XML文件解析成XPathParser对象，其中会解析成对应的Document对象，内部的Properties对象存储动态变量的值
 * PropertyParser用于解析XML文件中的动态值，根据GenericTokenParser获取动态属性的名称（例如${name}->name）
 * ，然后通过VariableTokenHandler根据Properties对象获取到动态属性（name）对应的值
 */
package org.apache.ibatis.parsing;
