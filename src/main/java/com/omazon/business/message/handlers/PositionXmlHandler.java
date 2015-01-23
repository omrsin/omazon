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
public class PositionXmlHandler extends DefaultHandler {

    private int truckId;
    private double longidute;
    private double latitude;

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }

    public double getLongidute() {
        return longidute;
    }

    public void setLongidute(int longidute) {
        this.longidute = longidute;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    private static final int TRUCK = 0;
    private static final int LONG = 1;
    private static final int LAT = 2;

    public int processing = -1;

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes)
            throws SAXException {
        if ("truckId".equals(qName)) {
            processing = TRUCK;
        } else if ("long".equals(qName)) {
            processing = LONG;
        } else if ("lat".equals(qName)) {
            processing = LAT;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if ("truckId".equals(qName) || "long".equals(qName) || "lat".equals(qName)) {
            processing = -1;
        }
    }

    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        switch(processing){
            case TRUCK:
                this.truckId = new Integer(new String(ch,start,length));
                break;
            case LONG:
                this.longidute = new Double(new String(ch,start,length));
                break;
            case LAT:
                this.latitude = new Double(new String(ch,start,length));
                break;
        }
    }
}
