/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business.message;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;

/**
 *
 * @author omar
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/svNew"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/svNew"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/svNew"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")    
})
public class SvNewBean implements MessageListener {
    
    @Resource(mappedName = "jms/clNew")
    private Topic clNew;
    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;
    
    public SvNewBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        context.createProducer().send(clNew, message);
    }    
}
