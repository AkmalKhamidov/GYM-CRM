package com.epamlearning.producer;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import static com.epamlearning.GymcrmSecurityApplication.TRANSACTION_ID;

@Service
@Slf4j
public class MessageProducer {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public MessageProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String destination, Object object) {
        log.info("Sending message to " + destination + " with object " + object);
        jmsTemplate.convertAndSend(destination, object, message -> {
            message.setJMSCorrelationID(MDC.get(TRANSACTION_ID));
            return message;
        });
    }

}
