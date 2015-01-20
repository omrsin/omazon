/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author floriment
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/delivery"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/delivery"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/delivery"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class DeliveryBean implements MessageListener {
    
    public DeliveryBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage)
        {
            TextMessage textMessage = (TextMessage)message;
            try {
                System.out.println("We have a new event:"+textMessage.getText());
            } catch (JMSException ex) {
                Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
