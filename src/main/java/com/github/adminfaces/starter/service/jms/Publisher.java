package com.github.adminfaces.starter.service.jms;

import javax.jms.*;

public class Publisher {
    public static void sendMessage(String topicName, String message) {
        System.out.println("Sending message: " + message);

        try {
            TopicConnection connection = ConnectionDriver.getConnection();
            TopicSession session = ConnectionDriver.getSession();
            Topic topic = ConnectionDriver.getTopic(topicName);

            TopicPublisher publisher = session.createPublisher(topic);

            TextMessage textMessage = session.createTextMessage(message);

            publisher.publish(textMessage);

//            connection.close();

            System.out.println("Message sent successfully");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
