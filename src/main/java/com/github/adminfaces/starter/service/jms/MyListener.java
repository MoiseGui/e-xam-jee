package com.github.adminfaces.starter.service.jms;

import com.github.adminfaces.starter.infra.security.LogonMB;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.Serializable;

@Named
@SessionScoped
public class MyListener implements MessageListener, Serializable {
    @Inject
    LogonMB logonMB;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Message received: " + textMessage.getText());

            logonMB.isLoggedIn();
            logonMB.showMessage(textMessage.getText());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
