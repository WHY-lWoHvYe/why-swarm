/*
 *    Copyright (c) 2022.  lWoHvYe(Hongyan Wang)
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

package com.lwohvye.log.consumer;

import com.lwohvye.log.config.RabbitMQLogConfig;
import com.lwohvye.log.domain.Log;
import com.lwohvye.log.service.ILogService;
import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.rabbitmq.AmqpMsgEntity;
import com.lwohvye.utils.rabbitmq.YRabbitAbstractConsumer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQLogMsgConsumer extends YRabbitAbstractConsumer {

    private ILogService logService;

    @Autowired
    public void setLogService(ILogService logService) {
        this.logService = logService;
    }

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        super.redissonClient = redissonClient;
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitMQLogConfig.LOGGING_QUEUE)
    public void saveLogMsg(String amqpMsgEntityStr) {
        baseConsumer(amqpMsgEntityStr, null, null, msgEntity -> {
            var logEntity = JsonUtils.toJavaObject(msgEntity.getMsgData(), Log.class); // 以Map的形态放进去的，这里可能出错
            logService.save(logEntity);
            return null;
        }, s -> {
        });
    }

    @Override
    public void baseBeforeConsumer(AmqpMsgEntity msgEntity) {

    }

    @Override
    public void baseBeforeMessageConsumer(AmqpMsgEntity msgEntity) {

    }
}
