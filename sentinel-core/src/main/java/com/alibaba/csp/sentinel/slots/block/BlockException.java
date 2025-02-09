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
package com.alibaba.csp.sentinel.slots.block;

/**
 * 由于流量控制、断路或系统保护触发，被Sentinel阻塞的抽象异常。
 *
 *
 * Abstract exception indicating blocked by Sentinel due to flow control,
 * circuit breaking or system protection triggered.
 *
 * @author youji.zj
 */
public abstract class BlockException extends Exception {

    private static final int MAX_SEARCH_DEPTH = 10;

    public static final String BLOCK_EXCEPTION_FLAG = "SentinelBlockException";
    public static final String BLOCK_EXCEPTION_MSG_PREFIX = "SentinelBlockException: ";

    /**
     * <p>this constant RuntimeException has no stack trace, just has a message
     * {@link #BLOCK_EXCEPTION_FLAG} that marks its name.
     * </p>
     * <p>
     * Use {@link #isBlockException(Throwable)} to check whether one Exception
     * Sentinel Blocked Exception.
     * </p>
     */
    public static RuntimeException THROW_OUT_EXCEPTION = new RuntimeException(BLOCK_EXCEPTION_FLAG);

    public static StackTraceElement[] sentinelStackTrace = new StackTraceElement[] {
        new StackTraceElement(BlockException.class.getName(), "block", "BlockException", 0)
    };

    static {
        THROW_OUT_EXCEPTION.setStackTrace(sentinelStackTrace);
    }

    protected AbstractRule rule;
    private String ruleLimitApp;

    public BlockException(String ruleLimitApp) {
        super();
        this.ruleLimitApp = ruleLimitApp;
    }

    public BlockException(String ruleLimitApp, AbstractRule rule) {
        super();
        this.ruleLimitApp = ruleLimitApp;
        this.rule = rule;
    }

    public BlockException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockException(String ruleLimitApp, String message) {
        super(message);
        this.ruleLimitApp = ruleLimitApp;
    }

    public BlockException(String ruleLimitApp, String message, AbstractRule rule) {
        super(message);
        this.ruleLimitApp = ruleLimitApp;
        this.rule = rule;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public String getRuleLimitApp() {
        return ruleLimitApp;
    }

    public void setRuleLimitApp(String ruleLimitApp) {
        this.ruleLimitApp = ruleLimitApp;
    }

    public RuntimeException toRuntimeException() {
        RuntimeException t = new RuntimeException(BLOCK_EXCEPTION_MSG_PREFIX + getClass().getSimpleName());
        t.setStackTrace(sentinelStackTrace);
        return t;
    }

    /**
     * Check whether the exception is sentinel blocked exception. One exception is sentinel blocked
     * exception only when:
     * <ul>
     * <li>the exception or its (sub-)cause is {@link BlockException}, or</li>
     * <li>the exception's message or any of its sub-cause's message is prefixed by {@link #BLOCK_EXCEPTION_FLAG}</li>
     * </ul>
     *
     * @param t the exception.
     * @return return true if the exception marks sentinel blocked exception.
     */
    public static boolean isBlockException(Throwable t) {
        if (null == t) {
            return false;
        }

        int counter = 0;
        Throwable cause = t;
        while (cause != null && counter++ < MAX_SEARCH_DEPTH) {
            if (cause instanceof BlockException) {
                return true;
            }
            if (cause.getMessage() != null && cause.getMessage().startsWith(BLOCK_EXCEPTION_FLAG)) {
                return true;
            }
            cause = cause.getCause();
        }

        return false;
    }

    public AbstractRule getRule() {
        return rule;
    }
}
