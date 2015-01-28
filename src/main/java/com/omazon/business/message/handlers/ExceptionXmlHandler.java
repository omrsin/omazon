/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business.message.handlers;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author floriment
 */
public class ExceptionXmlHandler extends DefaultHandler{
  private int truckId;
    private String exceptionMessage;

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }


    private static final int TRUCK = 0;
    private static final int EXCEPTION_MESSAGE = 1;

    public int processing = -1;

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes)
            throws SAXException {
        if ("truckId".equals(qName)) {
            processing = TRUCK;
        } else if ("exceptionDescription".equals(qName)) {
            processing = EXCEPTION_MESSAGE;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if ("truckId".equals(qName) || "exceptionDescription".equals(qName)) {
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
            case EXCEPTION_MESSAGE:
                this.exceptionMessage = new String(ch,start,length);
                break;
        }
    }   
}
