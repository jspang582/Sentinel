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

/**
 * 表示资源进入和资源退出的顺序不匹配(配对不匹配)。
 *
 * Represents order mismatch of resource entry and resource exit (pair mismatch).
 *
 * @author qinan.qn
 */
public class ErrorEntryFreeException extends RuntimeException {

    public ErrorEntryFreeException(String s) {
        super(s);
    }
}
