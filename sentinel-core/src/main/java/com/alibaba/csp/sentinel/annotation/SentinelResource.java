/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
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
package com.alibaba.csp.sentinel.annotation;

import com.alibaba.csp.sentinel.EntryType;

import java.lang.annotation.*;

/**
 * 该注解表示Sentinel资源的定义。
 *
 * The annotation indicates a definition of Sentinel resource.
 *
 * @author Eric Zhao
 * @author zhaoyuguang
 * @since 0.1.1
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SentinelResource {

    /**
     * 指定Sentinel资源的名称。
     *
     * @return name of the Sentinel resource
     */
    String value() default "";

    /**
     * 指定条目类型(入站或出站)，默认为出站。
     *
     * @return the entry type (inbound or outbound), outbound by default
     */
    EntryType entryType() default EntryType.OUT;

    /**
     * 指定资源类型。
     *
     * @return the classification (type) of the resource
     * @since 1.7.0
     */
    int resourceType() default 0;

    /**
     * 指定BlockException函数的名称，默认为空。
     *
     * @return name of the block exception function, empty by default
     */
    String blockHandler() default "";

    /**
     * 默认情况下，blockHandler与原始方法位于同一个类中。但是，如果一些方法共享相同的签名并打算设置相同的块处理程序，那么用户可以设置块处理程序所在的类。注意，Block Handler方法必须是静态的。
     *
     * The {@code blockHandler} is located in the same class with the original method by default.
     * However, if some methods share the same signature and intend to set the same block handler,
     * then users can set the class where the block handler exists. Note that the block handler method
     * must be static.
     *
     * @return the class where the block handler exists, should not provide more than one classes
     */
    Class<?>[] blockHandlerClass() default {};

    /**
     * 指定回退函数的名称，默认为空。
     *
     * @return name of the fallback function, empty by default
     */
    String fallback() default "";

    /**
     * 被用作默认的通用回退方法。它不应该接受任何参数，返回类型应该与原始方法兼容。
     *
     * The {@code defaultFallback} is used as the default universal fallback method.
     * It should not accept any parameters, and the return type should be compatible
     * with the original method.
     *
     * @return name of the default fallback method, empty by default
     * @since 1.6.0
     */
    String defaultFallback() default "";

    /**
     * 默认情况下，回退与原始方法位于同一个类中。但是，如果某些方法共享相同的签名并打算设置相同的回退，那么用户可以设置存在回退函数的类。注意，共享回退方法必须是静态的。
     *
     * The {@code fallback} is located in the same class with the original method by default.
     * However, if some methods share the same signature and intend to set the same fallback,
     * then users can set the class where the fallback function exists. Note that the shared fallback method
     * must be static.
     *
     * @return the class where the fallback method is located (only single class)
     * @since 1.6.0
     */
    Class<?>[] fallbackClass() default {};

    /**
     * 指定要跟踪的异常类列表，默认为Throwable。
     *
     * @return the list of exception classes to trace, {@link Throwable} by default
     * @since 1.5.1
     */
    Class<? extends Throwable>[] exceptionsToTrace() default {Throwable.class};
    
    /**
     * 指定需要忽略的异常。注意，exceptionsToTrace不应该与exceptionsToIgnore同时出现，否则exceptionsToIgnore将具有更高的优先级。
     *
     * Indicates the exceptions to be ignored. Note that {@code exceptionsToTrace} should
     * not appear with {@code exceptionsToIgnore} at the same time, or {@code exceptionsToIgnore}
     * will be of higher precedence.
     *
     * @return the list of exception classes to ignore, empty by default
     * @since 1.6.0
     */
    Class<? extends Throwable>[] exceptionsToIgnore() default {};
}
