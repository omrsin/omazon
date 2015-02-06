/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business.message;

import com.omazon.business.SynchronizationSingletonBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author omar
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/svQuery"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/svQuery"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/svQuery"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class SvQueryBean implements MessageListener {
    
    @Resource(mappedName = "jms/clNew")
    private Topic clNew;
    
    @Resource(mappedName = "jms/clReady")
    private Topic clReady;
    
    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;
    
    @EJB
    private SynchronizationSingletonBean synchEjb;
    
    public SvQueryBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            String textmessage = ((TextMessage)message).getText();
            synchEjb.getClients().remove(textmessage);
            if(synchEjb.getClients().isEmpty()) {
                System.out.println("First client received!");
                System.out.println("Client id: " + textmessage);
                synchEjb.setCurrentClient(textmessage);
                context.createProducer().send(clNew, textmessage);
            } else {
                System.out.println("New client received, starting synchronization phase!");
                System.out.println("Client id: " + textmessage);
                synchEjb.createClientsCopy();
                synchEjb.setCurrentClient(textmessage);
                context.createProducer().send(clReady, "Get Ready!");
            }            
        } catch (JMSException ex) {
            Logger.getLogger(SvNewBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
