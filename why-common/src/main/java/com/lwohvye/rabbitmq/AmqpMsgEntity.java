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
package com.lwohvye.rabbitmq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AmqpMsgEntity {

    //    延时时间
    private Long expire;
    //    时间单位
    private TimeUnit timeUnit;

    //    消息类型
    private String msgType;

    //    操作类型
    private DataOperatorEnum operator;

    //    消息体
    private String msgData;

    //    其他额外属性
    private String extraData;

}
