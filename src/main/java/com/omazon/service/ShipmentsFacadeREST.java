/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.service;

import com.omazon.business.ShipmentsFacade;
import com.omazon.entities.Products;
import com.omazon.entities.Shipments;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author floriment
 */
@Stateless
@Path("com.omazon.entities.shipments")
public class ShipmentsFacadeREST extends AbstractFacade<Shipments> {

    @PersistenceContext(unitName = "com_omazon_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @EJB
    private com.omazon.business.ShipmentsFacade ejbFacade;

    public ShipmentsFacadeREST() {
        super(Shipments.class);
    }

    public ShipmentsFacadeREST(Class<Shipments> entityClass) {
        super(entityClass);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Shipments entity) {
        getFacade().edit(entity);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Shipments> findAll() {
        return getFacade().findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    protected ShipmentsFacade getFacade() {
        return ejbFacade;
    }

}
