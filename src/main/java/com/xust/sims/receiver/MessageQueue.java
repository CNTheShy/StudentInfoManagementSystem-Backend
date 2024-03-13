package com.xust.sims.receiver;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.jms.Queue;



@Component
public class MessageQueue {
    @Bean
    public Queue queue() {
        return new ActiveMQQueue("com.xust.student.welcome");
    }
}
