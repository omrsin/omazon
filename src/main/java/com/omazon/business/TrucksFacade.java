/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business;

import com.omazon.entities.Trucks;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author omar
 */
@Stateless
public class TrucksFacade extends AbstractFacade<Trucks> {
    @PersistenceContext(unitName = "com_omazon_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrucksFacade() {
        super(Trucks.class);
    }
    
    public Trucks max(int status) {
        Trucks truck = (Trucks) em.createNamedQuery("Trucks.max").getSingleResult();        
        return truck;
    }
}

