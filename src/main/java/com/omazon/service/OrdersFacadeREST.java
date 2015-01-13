/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.service;

import com.omazon.business.OrdersFacade;
import com.omazon.entities.Customers;
import com.omazon.entities.Orders;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author omar
 */
@Stateless
@Path("com.omazon.entities.orders")
public class OrdersFacadeREST extends AbstractFacade<Orders> {

    @PersistenceContext(unitName = "com_omazon_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @EJB
    private com.omazon.business.OrdersFacade ejbFacade;
    @EJB
    private com.omazon.business.CustomersFacade customerFacade;

    public OrdersFacadeREST() {
        super(Orders.class);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Orders> findAll() {
        return getFacade().findAll();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public List<Orders> find(@PathParam("id") Integer id) {
        return getFacade().findByCustomer(customerFacade.find(id));
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Orders order) {
        getFacade().create(order);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Orders order) {
        getFacade().edit(order);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        getFacade().remove(getFacade().find(id));
    }

    public OrdersFacade getFacade() {
        return ejbFacade;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
