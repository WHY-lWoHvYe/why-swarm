package com.lwohvye.tools.consumer;

import com.lwohvye.tools.config.ToolsMqConfig;
import com.lwohvye.tools.utils.MailUtils;
import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.rabbitmq.AmqpMsgEntity;
import com.lwohvye.utils.rabbitmq.YRabbitAbstractConsumer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ToolsMsgConsumer extends YRabbitAbstractConsumer {

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        super.redissonClient = redissonClient;
    }

    @RabbitHandler
    @RabbitListener(queues = ToolsMqConfig.MAIL_COMMON_QUEUE)
    public void sendMailEvent(String amqpMsgEntityStr) {
        baseConsumer(amqpMsgEntityStr, null, null, msgEntity -> {
            var msgData = msgEntity.getMsgData();
            var msgMap = JsonUtils.toJavaObject(msgData, Map.class);
            var to = JsonUtils.getString(msgMap, "to");
            var subject = JsonUtils.getString(msgMap, "subject");
            var text = JsonUtils.getString(msgMap, "text");
            mailUtils.sendMail(to, subject, text);
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
