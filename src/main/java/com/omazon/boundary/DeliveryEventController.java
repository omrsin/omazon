/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.boundary;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Topic;

/**
 *
 * @author floriment
 */
@Named("deliveryEventController")
@RequestScoped
public class DeliveryEventController {
    @Resource(mappedName = "jms/delivery")
    private Topic delivery;
    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;
    
    private String shipmentId;

    public DeliveryEventController() {
    }
    
    public String generateDeliveryEvent(){
        sendJMSMessageToDelivery("Hellooooo");
        return "/generate_events";
    }

    private void sendJMSMessageToDelivery(String messageData) {
        context.createProducer().send(delivery, messageData);
    }
}
