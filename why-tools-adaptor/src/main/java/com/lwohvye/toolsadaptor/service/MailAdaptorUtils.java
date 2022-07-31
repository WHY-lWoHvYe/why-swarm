package com.lwohvye.toolsadaptor.service;

import com.lwohvye.toolsadaptor.config.MailAdaptorMQConfig;
import com.lwohvye.utils.json.JsonUtils;
import com.lwohvye.utils.rabbitmq.AmqpMsgEntity;
import com.lwohvye.utils.rabbitmq.SimpleMQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("mailUtils")
@ConditionalOnMissingBean(name = {"mailUtils"})
public class MailAdaptorUtils {

    @Autowired
    private SimpleMQProducerService simpleMQProducerService;

    public void sendMail(String to, String subject, String text) {
        var mailInfo = Map.of("to", to, "subject", subject, "text", text);
        var msg = new AmqpMsgEntity().setMsgType("event").setMsgData(JsonUtils.toJSONString(mailInfo));
        simpleMQProducerService.sendMsg(MailAdaptorMQConfig.DIRECT_SYNC_EXCHANGE, MailAdaptorMQConfig.MAIL_ROUTE_KEY, msg);
    }
}
