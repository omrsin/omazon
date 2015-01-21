/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business;

import com.omazon.entities.Customers;
import com.omazon.entities.Orders;
import com.omazon.entities.Shipments;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author omar
 */
@Stateless
public class OrdersFacade extends AbstractFacade<Orders> {
    @PersistenceContext(unitName = "com_omazon_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @EJB
    private com.omazon.business.ShipmentsFacade shipmentsFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Orders> findByCustomer(Customers customer)
    {
        List resultList = em.createNamedQuery("Orders.findByCustomerId").setParameter("customerId", customer.getId()).getResultList();
        return resultList;
    }
    
    public List<Orders> findByCustomerGetRange(Customers customer,int[] range)
    {
        Query query = em.createNamedQuery("Orders.findByCustomerId").setParameter("customerId", customer.getId());
        query.setMaxResults(range[1] - range[0] + 1);
        query.setFirstResult(range[0]);
        return query.getResultList();
    }

    public OrdersFacade() {
        super(Orders.class);
    }
    
    @Override
    public void create(Orders order){        
        int notDeliveredShipments = shipmentsFacade.countByStatus(Shipments.NOT_DELIVERED);
        Shipments shipment;        
        
        if(notDeliveredShipments == 0) {            
            shipment = new Shipments(shipmentsFacade.count()+1, 0);
        } else {
            shipment = shipmentsFacade.maxByStatus(Shipments.NOT_DELIVERED);
            if(shipment.getOrders().size() >= 3) {
                shipment = new Shipments(shipmentsFacade.count()+1, 0);
            }
        }
        
        order.setShipment(shipment);
        
        getEntityManager().persist(order);
    }
}