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
package com.lwohvye.gateway.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型的枚举，这里用枚举，实际上有点不符合开闭原则，加类型至少要改这个类
 *
 * @author Hongyan Wang
 * @date 2021年11月02日 16:51
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    EXTRA(-5, "使用扩展注解"),

    ADMIN(1, "尊贵的VIP"),
    NORMAL(0, "未来的VIP"),
    DEV(-1, "Ctrl C + V");

    private final Integer type;

    private final String desc;
}
