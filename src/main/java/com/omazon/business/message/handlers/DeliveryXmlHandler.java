/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business.message.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author floriment
 */
public class DeliveryXmlHandler extends DefaultHandler {

    private int shipmentId;
    private boolean interested = false;

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes)
            throws SAXException {
        if ("shipmentId".equals(qName)) {
            interested = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if ("shipmentId".equals(qName)) {
            interested = false;
        }
    }

    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (interested) {
            shipmentId = new Integer(new String(ch, start, length));
        }
    }
}
