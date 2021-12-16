package com.github.adminfaces.starter.service.jms;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.NamingException;
import java.io.Serializable;


public class Subscriber implements Serializable {
    public static void subscribe(String topicName) throws JMSException, NamingException {
        Topic topic = ConnectionDriver.getTopic(topicName);
        TopicSubscriber topicSubscriber = ConnectionDriver.getSession().createSubscriber(topic);

        // get LogonMB instance
        topicSubscriber.setMessageListener(new MyListener());
        System.out.println("Message Listener is set");
    }
}
