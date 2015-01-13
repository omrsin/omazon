/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.business;

import com.omazon.entities.Customers;
import com.omazon.entities.Orders;
import java.util.List;
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
}