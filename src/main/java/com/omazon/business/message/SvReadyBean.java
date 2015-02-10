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
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/svReady"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/svReady"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/svReady"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class SvReadyBean implements MessageListener {
    
    @Resource(mappedName = "jms/clNew")
    private Topic clNew;
    
    @Resource(mappedName = "jms/clUpdate")
    private Topic clUpdate;
    
    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;
    
    @EJB
    private SynchronizationSingletonBean synchEjb;
    
    public SvReadyBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            String textmessage = ((TextMessage)message).getText();
            System.out.println("Ready: "+ textmessage);
            synchEjb.removeFromClientsCopy(textmessage);
            if(synchEjb.getClientsCopy().isEmpty()){
                System.out.println("All clients are ready!");
                synchEjb.createClientsCopy();
                synchEjb.setSystemLocked(true);
                synchEjb.setPhase(SynchronizationSingletonBean.UPDATE_PHASE);
                context.createProducer().send(clUpdate, "Update!");
                System.out.println("Online Clients: " + synchEjb.getClients());
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(SvNewBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
