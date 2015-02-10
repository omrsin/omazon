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
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/svOffline"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/svOffline"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/svOffline"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class SvOfflineBean implements MessageListener {
    
    @Resource(mappedName = "jms/clNew")
    private Topic clNew; 
    
    @Resource(mappedName = "jms/clUpdate")
    private Topic clUpdate;
    
    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;
    
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
            synchEjb.getClientsCopy().remove(textmessage);
            System.out.println("Online Clients: " + synchEjb.getClients());
            if(!synchEjb.getPhase().equals("") && synchEjb.getClients().isEmpty()){
                System.out.println("First client received!");
                System.out.println("Client id: " + synchEjb.getCurrentClient());
                synchEjb.setPhase("");
                context.createProducer().send(clNew, synchEjb.getCurrentClient());
            } else {
                if(synchEjb.getPhase().equals(SynchronizationSingletonBean.READY_PHASE) && synchEjb.getClientsCopy().isEmpty()){
                    System.out.println("All clients are ready!");
                    synchEjb.createClientsCopy();
                    synchEjb.setSystemLocked(true);
                    synchEjb.setPhase(SynchronizationSingletonBean.UPDATE_PHASE);
                    context.createProducer().send(clUpdate, "Update!");
                    System.out.println("Online Clients: " + synchEjb.getClients());
                } else if(synchEjb.getPhase().equals(SynchronizationSingletonBean.UPDATE_PHASE) && synchEjb.getClientsCopy().isEmpty()) {
                    System.out.println("All clients have been updated!");
                    synchEjb.setPhase("");
                    context.createProducer().send(clNew, synchEjb.getCurrentClient());
                    System.out.println("Online Clients: " + synchEjb.getClients());
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(SvNewBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
