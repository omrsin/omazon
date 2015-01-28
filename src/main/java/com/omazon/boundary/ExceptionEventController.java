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
@Named("exceptionEventController")
@RequestScoped
public class ExceptionEventController {
    @Resource(mappedName = "jms/exception")
    private Topic exception;
    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;
    
    private String shipmentId;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ExceptionEventController() {
    }
    
    public String generateExceptionEvent(){
        sendJMSMessageToDelivery(text);
        return "/generate_events";
    }

    private void sendJMSMessageToDelivery(String messageData) {
        context.createProducer().send(exception, messageData);
    }
}