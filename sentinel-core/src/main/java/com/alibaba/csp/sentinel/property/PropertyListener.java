/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.property;

/**
 * 当SentinelProperty.updateValue(Object)需要通知监听器时，这个类保存回调方法。
 *
 * This class holds callback method when {@link SentinelProperty#updateValue(Object)} need inform the listener
 *
 * @author jialiang.linjl
 */
public interface PropertyListener<T> {

    /**
     * 当SentinelProperty.updateValue(Object)需要通知监听器时的回调方法。
     *
     * Callback method when {@link SentinelProperty#updateValue(Object)} need inform the listener.
     *
     * @param value updated value.
     */
    void configUpdate(T value);

    /**
     * 第一次加载该值。
     *
     * The first time of the {@code value}'s load.
     *
     * @param value the value loaded.
     */
    void configLoad(T value);
}
