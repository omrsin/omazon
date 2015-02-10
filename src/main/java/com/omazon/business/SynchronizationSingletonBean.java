/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

/**
 *
 * @author omar
 */
@Singleton
@LocalBean
@Startup
public class SynchronizationSingletonBean {

    public static final String READY_PHASE = "ready_phase";
    public static final String UPDATE_PHASE = "update_phase";
    
    private List<String> clients;
    private List<String> clientsCopy;
    private String currentClient;
    private boolean systemLocked;
    private String phase;
           
    @PostConstruct
    void init () {
        clients = new ArrayList<>();
        clientsCopy = new ArrayList<>();
        currentClient = "";
        systemLocked = false;
        phase = "";
    }

    public SynchronizationSingletonBean() {
    }   

    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        this.clients = clients;
    }
    
    public void addClient(String client) {
        this.clients.add(client);
    }

    public List<String> getClientsCopy() {
        return clientsCopy;
    }
    
    public void createClientsCopy(){
        this.clientsCopy = new ArrayList<>(clients);
    }
    
    public void removeFromClientsCopy (String client) {
        this.clientsCopy.remove(client);
    }

    public String getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(String currentClient) {
        this.currentClient = currentClient;
    }

    public boolean isSystemLocked() {
        return systemLocked;
    }

    public void setSystemLocked(boolean systemLocked) {
        this.systemLocked = systemLocked;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }   
}
