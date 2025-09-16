package com.luopc.platform.cloud.amq.util;

import com.luopc.platform.common.core.util.SmartJsonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.http.MediaType;

/**
 * @author by Robin
 * @className RabbitMessageConvertor
 * @description TODO
 * @date 2024/1/16 0016 18:14
 */
public class RabbitMessageConvertor {

    public Message toJsonMessage(Object entity) {
        String json = com.luopc.platform.common.core.util.SmartJsonUtil.writeJson(entity);
        MessageProperties jsonProperties = new MessageProperties();
        jsonProperties.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return new Message(json.getBytes(), jsonProperties);
    }

}
