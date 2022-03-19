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
package com.lwohvye.gateway.rabbitmq.consumer;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.lwohvye.modules.rabbitmq.config.RabbitMqConfig;
import com.lwohvye.gateway.rabbitmq.service.AuthMQService;
import com.lwohvye.rabbitmq.AmqpMsgEntity;
import com.lwohvye.utils.JsonUtils;
import com.lwohvye.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
// 监听延迟插件相关队列的消息
@RabbitListener(queues = RabbitMqConfig.DATA_COMMON_DELAY_QUEUE)
// 支持多种配置方式 property placeholders and SpEL  https://docs.spring.io/spring-amqp/docs/current/reference/html/#choose-container
//@RabbitListener(queues = "#{'${property.with.comma.delimited.queue.names}'.split(',')}" )
// 还可调用静态和非静态方法
// SpEL https://www.lwohvye.com/2021/06/11/spring-%e8%a1%a8%e8%be%be%e5%bc%8f%e8%af%ad%e8%a8%80-spel/
//@RabbitListener(queues = "#{T(全类名).静态方法名}")
//@RabbitListener(queues = "#{beanName.方法名}")

// 当将@RabbitListener注解放在类上时，一些情况下会报 org.springframework.amqp.AmqpException: No method found for class [B
// 相关原因参见：https://jira.spring.io/browse/AMQP-573
// 可使用将@RabbitListener放到方法上的方式。因为类型推断只适用于方法级别的@RabbitListener
// 使用@Payload注解可以获取消息中的body信息，使用@Headers注解可以获取消息中的headers信息，也可以使用@Header获取单个header属性

// @RabbitListener 可以标注在类上面，需配合 @RabbitHandler 注解一起使用
// @RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理，具体使用哪个方法处理，根据 MessageConverter 转换后的参数类型
public class RabbitMQDelayMsgConsumerService {

    @Autowired
    private AuthMQService authMQService;

    @RabbitHandler
    public void handle(String amqpMsgEntityStr) {
        var amqpMsgEntity = JsonUtils.toJavaObject(amqpMsgEntityStr, AmqpMsgEntity.class);
        var msgType = amqpMsgEntity.getMsgType();
        var msgData = amqpMsgEntity.getMsgData();
        try {
            if (StrUtil.isBlank(msgData))
                return;
            // 鉴权类
            if (ObjectUtil.equals(msgType, "auth")) {
                var extraData = amqpMsgEntity.getExtraData();
                if (StrUtil.isNotBlank(extraData))
                    ReflectUtil.invoke(authMQService, extraData, msgData);
            }
        } catch (Exception e) {
            log.error(" Consume Msg Error, Reason: {} || Msg detail: {} ", e.getMessage(), amqpMsgEntityStr);
            var to = "";
            var subject = "Consume Msg Error" + this.getClass().getSimpleName();
            // 基于模版生成正文
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
            Template template = engine.getTemplate("email/noticeEmail.ftl");
            var text = template.render(Dict.create().setIgnoreNull("errMsg", e.getMessage()));

            // TODO: 2022/1/4 通过MQ交由特定服务发送邮件
            // 邮件通知
            Object mailUtils;
            var beanName = "mailUtils";
            try {
                mailUtils = SpringContextHolder.getBean(beanName);
            } catch (Exception ex) {
                log.error("获取 {} 异常，原因 {} ，请确认是否引入相关模块", beanName, ex.getMessage());
                return;
            }
            ReflectUtil.invoke(mailUtils, "sendMail", to, subject, text);
        } finally {
            log.info("Consume Msg,Msg type: {}, -+- ,Msg detail: {}", msgType, amqpMsgEntityStr);
            // 处理完成，根据结果记录相关表（看业务需求）。若处理报错，需邮件通知
        }
    }

}

