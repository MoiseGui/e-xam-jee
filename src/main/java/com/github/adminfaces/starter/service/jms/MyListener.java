package com.github.adminfaces.starter.service.jms;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.github.adminfaces.starter.model.Notification;
import com.github.adminfaces.starter.service.NotificationService;
import com.github.adminfaces.starter.service.email.SendEmail;

public class MyListener implements MessageListener, Serializable {
	// @Inject
	// LogonMB logonMB;
	// @Inject
	// private NotificationService notificationService;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			System.out.println("Notification received: " + textMessage.getText());
			System.out.println("processing the message");
			String[] content = textMessage.getText().split(":");
			String email = content[1];
			String notificationMessage = content[0];
			// Notification sending process here
			System.out.println("saving the notification");
			/**********************************/

			// Email message to be sent
			// msg = content[0]
			// Recipient's email ID needs to be mentioned.
			// to = content[1];

			// Sender's email ID needs to be mentioned "e-xam website"
			// from = "noreply.e-xam@gmail.com";
			SendEmail.send(content[1], "noreply.e-xam@gmail.com", content[0]);

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
