package com.github.adminfaces.starter.service.jms;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConnectionDriver {
    private static InitialContext ctx;
    private static TopicConnection connection;
    private static Topic topic;
    private static TopicSession session;

    private static void startConnection() throws NamingException, JMSException {
        if (connection == null) {
            ctx = new InitialContext();
            TopicConnectionFactory factory = (TopicConnectionFactory) ctx.lookup("myTopicConnectionFactory");
            connection = factory.createTopicConnection();
            connection.start();
            session = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
        }
    }

    public static Topic getTopic(String topicName) {
        if (topic == null) {
            try {
                if (connection == null) startConnection();
                topic = (Topic) ctx.lookup(topicName);

                return topic;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return topic;
    }

    public static TopicSession getSession() throws JMSException, NamingException {
        if (connection == null) {
            startConnection();
        }
        return session;
    }

    public static TopicConnection getConnection() throws NamingException, JMSException {
        if (connection == null) startConnection();
        return connection;
    }
}
