/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business.message;

import com.omazon.business.SynchronizationSingletonBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author omar
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/svOffline"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/svOffline"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/svOffline"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class SvOfflineBean implements MessageListener {
    
    @EJB
    private SynchronizationSingletonBean synchEjb;
    
    public SvOfflineBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            String textmessage = ((TextMessage)message).getText();
            System.out.println("Client is going offline: "+ textmessage);
            synchEjb.getClients().remove(textmessage);
            System.out.println("Online Clients: " + synchEjb.getClients());
        } catch (JMSException ex) {
            Logger.getLogger(SvNewBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
