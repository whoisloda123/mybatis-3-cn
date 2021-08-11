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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The default implementation of {@code ReflectorFactory}.
 *
 * @author Administrator
 */
public class DefaultReflectorFactory implements ReflectorFactory {
	/**
	 * 是否缓存
	 */
	private boolean classCacheEnabled = false;

	/**
     * Reflector 的缓存映射
     *
     * KEY：Class 对象
     * VALUE：Reflector 对象
     */
	private final ConcurrentMap<Class<?>, Reflector> reflectorMap = new ConcurrentHashMap<>();

	public DefaultReflectorFactory() {
	}

	@Override
	public boolean isClassCacheEnabled() {
		return classCacheEnabled;
	}

	@Override
	public void setClassCacheEnabled(boolean classCacheEnabled) {
		this.classCacheEnabled = classCacheEnabled;
	}

	@Override
	public Reflector findForClass(Class<?> type) {
		if (classCacheEnabled) {
			// synchronized (type) removed see issue #461
			return reflectorMap.computeIfAbsent(type, Reflector::new);
		} else {
			return new Reflector(type);
		}
	}

}
