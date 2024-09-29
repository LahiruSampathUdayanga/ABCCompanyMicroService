package com.order.order.kafka;

import com.base.base.dto.OrderEventDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventDto.class);

    private final NewTopic orderTopic;
    private final KafkaTemplate<String, OrderEventDto> kafkaTemplate;

    public OrderProducer(NewTopic orderTopic, KafkaTemplate<String, OrderEventDto> kafkaTemplate) {
        this.orderTopic = orderTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderEventDto orderEventDto){
        LOGGER.info(String.format("Sending order event to topic %s", orderEventDto.toString()));

        Message<OrderEventDto> message = MessageBuilder
                .withPayload(orderEventDto)
                .setHeader(KafkaHeaders.TOPIC, orderTopic.name())
                .build();

        kafkaTemplate.send(message);
    }
}
