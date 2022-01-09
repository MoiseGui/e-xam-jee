package com.github.adminfaces.starter.service.jms;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import com.github.adminfaces.starter.model.Role;
import com.github.adminfaces.starter.service.email.SendEmail;

public class Producer {
    public static void sendMessage(String queueName, String message) {
        System.out.println("Sending message: " + message);

        try {
            QueueConnection connection = ConnectionDriver.getConnection();
            QueueSession session = ConnectionDriver.getSession();
            Queue queue = ConnectionDriver.getQueue(queueName);

            QueueSender sender = session.createSender(queue);

            TextMessage textMessage = session.createTextMessage(message);

            sender.send(textMessage);

//            connection.close();

            System.out.println("Message sent successfully");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
