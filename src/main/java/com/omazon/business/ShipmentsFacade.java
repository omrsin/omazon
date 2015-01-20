/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business;

import com.omazon.entities.Shipments;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author omar
 */
@Stateless
public class ShipmentsFacade extends AbstractFacade<Shipments> {
    @PersistenceContext(unitName = "com_omazon_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ShipmentsFacade() {
        super(Shipments.class);
    }
    
    public List<Shipments> findByStatus(int status) {
        List resultList = em.createNamedQuery("Shipments.findByStatus").setParameter("status", status).getResultList();
        return resultList;
    }    
}
