/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business;

import com.omazon.entities.Customers;
import com.omazon.entities.Orders;
import com.omazon.entities.Shipments;
import com.omazon.entities.Trucks;
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
    
    @EJB
    private com.omazon.business.TrucksFacade trucksFacade;

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
            assignTruck(shipment);
        } else {
            shipment = shipmentsFacade.maxByStatus(Shipments.NOT_DELIVERED);
            if(shipment.getOrders().size() >= 3) {
                shipment = new Shipments(shipmentsFacade.count()+1, 0);
                assignTruck(shipment);
            }
        }
        
        order.setShipment(shipment);
        getEntityManager().persist(order);
    }

    private void assignTruck(Shipments shipment) {
        int availableTrucks = trucksFacade.count();
        Trucks truck;
        if(availableTrucks == 0){
            truck = new Trucks(availableTrucks+1, 0.0, 0.0);           
        } else {
            truck = trucksFacade.max();
            if(truck.getShipments().size() >= 2) {
                truck = new Trucks(availableTrucks+1, 0.0, 0.0); 
            }
        }
        shipment.setTruck(truck);
    }
}