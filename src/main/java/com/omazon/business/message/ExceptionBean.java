/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business.message;

import com.omazon.business.ShipmentsFacade;
import com.omazon.business.TrucksFacade;
import com.omazon.business.message.handlers.DeliveryXmlHandler;
import com.omazon.business.message.handlers.ExceptionXmlHandler;
import com.omazon.entities.Shipments;
import com.omazon.entities.Trucks;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author floriment
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/exception"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/exception"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/exception"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class ExceptionBean implements MessageListener {
    @EJB
    private TrucksFacade trucksFacade;
    
    

    public ExceptionBean() {
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                parseAndExecute(textMessage.getText());
            } catch (JMSException ex) {
                Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void parseAndExecute(String message) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        ExceptionXmlHandler exceptionHandler = new ExceptionXmlHandler();
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(new InputSource(new StringReader(message)), exceptionHandler);
        } catch (SAXException ex) {
            Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        //TODO:: Update the bean status;
        System.out.println("Exception Message" + exceptionHandler.getExceptionMessage());
        Trucks truck = trucksFacade.find(exceptionHandler.getTruckId());
        truck.setExcepDesc(exceptionHandler.getExceptionMessage());
        trucksFacade.edit(truck);
       //TODO:: Process the exception by updating the truck
    }
    
}
