package com.github.adminfaces.starter.service.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.naming.NamingException;


public class Consumer implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void consume(String queueName) throws JMSException, NamingException {
    	Queue queue = ConnectionDriver.getQueue(queueName);
    	QueueReceiver queueReceiver = ConnectionDriver.getSession().createReceiver(queue);  ;

    	queueReceiver.setMessageListener(new MyListener());
        System.out.println("Message Listener is set");
    }
}
