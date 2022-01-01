package com.github.adminfaces.starter.service.jms;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Named
@SessionScoped
public class MyListener implements MessageListener, Serializable {
   // @Inject
   // LogonMB logonMB;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Message received: " + textMessage.getText());

            //logonMB.isLoggedIn();
           // logonMB.showMessage(textMessage.getText());
            
            //Email sending process here
            
            
            

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
