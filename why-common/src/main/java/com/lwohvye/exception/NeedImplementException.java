/*
 *    Copyright (c) 2021-2022.  lWoHvYe(Hongyan Wang)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lwohvye.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

/**
 * 不能使用默认实现 ，需对方法默认实现
 *
 * @author Hongyan Wang
 * @date 2021年07月18日 18:54
 */
@Getter
public class NeedImplementException extends RuntimeException {
    private Integer status = NOT_IMPLEMENTED.value();

    public NeedImplementException(String msg) {
        super(msg);
    }

    public NeedImplementException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }
}
