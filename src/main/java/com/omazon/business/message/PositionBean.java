/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business.message;

import com.omazon.business.TrucksFacade;
import com.omazon.business.message.handlers.PositionXmlHandler;
import com.omazon.business.message.handlers.PositionXmlHandler;
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
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/position"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/position"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/position"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class PositionBean implements MessageListener {

    @EJB
    private TrucksFacade trucksFacade;

    public PositionBean() {
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                parseAndExecute(textMessage.getText());
            } catch (JMSException ex) {
                Logger.getLogger(PositionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void parseAndExecute(String message) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        PositionXmlHandler position = new PositionXmlHandler();
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(new InputSource(new StringReader(message)), position);
        } catch (SAXException ex) {
            Logger.getLogger(PositionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PositionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PositionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Position Event triggered" + position.getTruckId());

        Trucks truck = trucksFacade.find(position.getTruckId());
        truck.setLatitude(position.getLatitude());
        truck.setLongitude(position.getLongidute());
        trucksFacade.edit(truck);
    }

}
