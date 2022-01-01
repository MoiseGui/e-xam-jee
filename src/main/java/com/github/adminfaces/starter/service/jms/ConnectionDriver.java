package com.github.adminfaces.starter.service.jms;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConnectionDriver {
    private static InitialContext ctx;
    private static QueueConnection connection;
    private static Queue queue;
    private static QueueSession session;
    
    

    private static void startConnection() throws NamingException, JMSException {
        if (connection == null) {
            ctx = new InitialContext();
            QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("myQueueConnectionFactory");
            connection = factory.createQueueConnection();
            connection.start();
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        }
    }

    public static Queue getQueue(String queueName) {
        if (queue == null) {
            try {
                if (connection == null) startConnection();
                queue = (Queue) ctx.lookup(queueName);

                return queue;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return queue;
    }

    public static QueueSession getSession() throws JMSException, NamingException {
        if (connection == null) {
            startConnection();
        }
        return session;
    }

    public static QueueConnection getConnection() throws NamingException, JMSException {
        if (connection == null) startConnection();
        return connection;
    }
}
