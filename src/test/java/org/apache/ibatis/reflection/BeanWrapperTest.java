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
package org.apache.ibatis.reflection;

import java.util.HashMap;

import org.apache.ibatis.domain.misc.CustomBeanWrapperFactory;
import org.apache.ibatis.domain.misc.RichType;
import org.junit.jupiter.api.Test;

public class BeanWrapperTest {

	@Test
	public void test01() {
		RichType object = new RichType();

		object.setRichType(new RichType());
		object.getRichType().setRichMap(new HashMap<String, Object>());
		object.getRichType().getRichMap().put("nihao", "123");

		MetaObject meta = MetaObject.forObject(object, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
				new CustomBeanWrapperFactory(), new DefaultReflectorFactory());
		Class<?> clazz = meta.getObjectWrapper().getGetterType("richType.richMap.nihao");
		System.out.println(clazz);
	}

}
