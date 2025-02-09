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
package com.alibaba.csp.sentinel;

import java.lang.reflect.Method;
import java.util.List;

import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;

/**
 * 从概念上讲，需要保护的物理或逻辑资源应该用一个条目包围起来。如果满足任何条件，对该资源的请求将被阻塞。当超过任何规则的阈值时。一旦被阻塞，SphO.entry()将返回false。
 *
 * 要配置标准，可以使用XXXRuleManager.loadRules()添加规则
 * 如；FlowRuleManager.loadRules(List) DegradeRuleManager.loadRules(List) SystemRuleManager.loadRules(List)。
 *
 * 确保SphO.entry()和exit()在同一个线程中配对，否则将抛出ErrorEntryFreeException。
 *
 * Conceptually, physical or logical resource that need protection should be
 * surrounded by an entry. The requests to this resource will be blocked if any
 * criteria is met, eg. when any {@link Rule}'s threshold is exceeded. Once blocked,
 * {@link SphO}#entry() will return false.
 *
 * <p>
 * To configure the criteria, we can use <code>XXXRuleManager.loadRules()</code> to add rules. eg.
 * {@link FlowRuleManager#loadRules(List)}, {@link DegradeRuleManager#loadRules(List)},
 * {@link SystemRuleManager#loadRules(List)}.
 * </p>
 *
 * <p>
 * Following code is an example. {@code "abc"} represent a unique name for the
 * protected resource:
 * </p>
 *
 * <pre>
 * public void foo() {
 *    if (SphO.entry("abc")) {
 *        try {
 *            // business logic
 *        } finally {
 *            SphO.exit(); // must exit()
 *        }
 *    } else {
 *        // failed to enter the protected resource.
 *    }
 * }
 * </pre>
 *
 * Make sure {@code SphO.entry()} and {@link SphO#exit()} be paired in the same thread,
 * otherwise {@link ErrorEntryFreeException} will be thrown.
 *
 * @author jialiang.linjl
 * @author leyou
 * @author Eric Zhao
 * @see SphU
 */
public class SphO {

    private static final Object[] OBJECTS0 = new Object[0];

    /**
     * Record statistics and perform rule checking for the given resource.
     *
     * @param name the unique name of the protected resource
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(String name) {
        return entry(name, EntryType.OUT, 1, OBJECTS0);
    }

    /**
     * Checking all {@link Rule}s about the protected method.
     *
     * @param method the protected method
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(Method method) {
        return entry(method, EntryType.OUT, 1, OBJECTS0);
    }

    /**
     * Checking all {@link Rule}s about the protected method.
     *
     * @param method     the protected method
     * @param batchCount the amount of calls within the invocation (e.g. batchCount=2 means request for 2 tokens)
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(Method method, int batchCount) {
        return entry(method, EntryType.OUT, batchCount, OBJECTS0);
    }

    /**
     * Record statistics and perform rule checking for the given resource.
     *
     * @param name       the unique string for the resource
     * @param batchCount the amount of calls within the invocation (e.g. batchCount=2 means request for 2 tokens)
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(String name, int batchCount) {
        return entry(name, EntryType.OUT, batchCount, OBJECTS0);
    }

    /**
     * Checking all {@link Rule}s about the protected method.
     *
     * @param method the protected method
     * @param type   the resource is an inbound or an outbound method. This is used
     *               to mark whether it can be blocked when the system is unstable,
     *               only inbound traffic could be blocked by {@link SystemRule}
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(Method method, EntryType type) {
        return entry(method, type, 1, OBJECTS0);
    }

    /**
     * Record statistics and perform rule checking for the given resource.
     *
     * @param name the unique name for the protected resource
     * @param type the resource is an inbound or an outbound method. This is used
     *             to mark whether it can be blocked when the system is unstable,
     *             only inbound traffic could be blocked by {@link SystemRule}
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(String name, EntryType type) {
        return entry(name, type, 1, OBJECTS0);
    }

    /**
     * Checking all {@link Rule}s about the protected method.
     *
     * @param method the protected method
     * @param type   the resource is an inbound or an outbound method. This is used
     *               to mark whether it can be blocked when the system is unstable,
     *               only inbound traffic could be blocked by {@link SystemRule}
     * @param count  the amount of calls within the invocation (e.g. batchCount=2 means request for 2 tokens)
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(Method method, EntryType type, int count) {
        return entry(method, type, count, OBJECTS0);
    }

    /**
     * Record statistics and perform rule checking for the given resource.
     *
     * @param name  the unique name for the protected resource
     * @param type  the resource is an inbound or an outbound method. This is used
     *              to mark whether it can be blocked when the system is unstable,
     *              only inbound traffic could be blocked by {@link SystemRule}
     * @param count the amount of calls within the invocation (e.g. batchCount=2 means request for 2 tokens)
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(String name, EntryType type, int count) {
        return entry(name, type, count, OBJECTS0);
    }

    /**
     * Record statistics and perform rule checking for the given resource.
     *
     * @param name        the unique name for the protected resource
     * @param trafficType the traffic type (inbound, outbound or internal). This is used
     *                    to mark whether it can be blocked when the system is unstable,
     *                    only inbound traffic could be blocked by {@link SystemRule}
     * @param batchCount  the amount of calls within the invocation (e.g. batchCount=2 means request for 2 tokens)
     * @param args        args for parameter flow control or customized slots
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(String name, EntryType trafficType, int batchCount, Object... args) {
        try {
            Env.sph.entry(name, trafficType, batchCount, args);
        } catch (BlockException e) {
            return false;
        } catch (Throwable e) {
            RecordLog.warn("SphO fatal error", e);
            return true;
        }
        return true;
    }

    /**
     * Record statistics and perform rule checking for the given method resource.
     *
     * @param method      the protected method
     * @param trafficType the traffic type (inbound, outbound or internal). This is used
     *                    to mark whether it can be blocked when the system is unstable,
     *                    only inbound traffic could be blocked by {@link SystemRule}
     * @param batchCount  the amount of calls within the invocation (e.g. batchCount=2 means request for 2 tokens)
     * @param args        args for parameter flow control or customized slots
     * @return true if no rule's threshold is exceeded, otherwise return false.
     */
    public static boolean entry(Method method, EntryType trafficType, int batchCount, Object... args) {
        try {
            Env.sph.entry(method, trafficType, batchCount, args);
        } catch (BlockException e) {
            return false;
        } catch (Throwable e) {
            RecordLog.warn("SphO fatal error", e);
            return true;
        }
        return true;
    }

    public static void exit(int count, Object... args) {
        ContextUtil.getContext().getCurEntry().exit(count, args);
    }

    public static void exit(int count) {
        ContextUtil.getContext().getCurEntry().exit(count, OBJECTS0);
    }

    public static void exit() {
        exit(1, OBJECTS0);
    }
}
