/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business.message;

import com.omazon.business.ShipmentsFacade;
import com.omazon.business.message.handlers.DeliveryXmlHandler;
import com.omazon.entities.Shipments;
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
import org.eclipse.persistence.internal.oxm.record.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
    
    @EJB
    private ShipmentsFacade shipmentsFacade;

    public DeliveryBean() {
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
        DeliveryXmlHandler delivery = new DeliveryXmlHandler();
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(new InputSource(new StringReader(message)), delivery);
        } catch (SAXException ex) {
            Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DeliveryBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        //TODO:: Update the bean status;
        System.out.println("ShipmentId" + delivery.getShipmentId());
        Shipments shipment = shipmentsFacade.find(delivery.getShipmentId());
        shipment.setStatus(1);
        shipmentsFacade.edit(shipment);
    }

}
