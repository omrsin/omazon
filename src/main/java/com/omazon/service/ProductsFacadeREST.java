/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.service;

import com.omazon.business.ProductsFacade;
import com.omazon.entities.Products;
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
@Path("com.omazon.entities.products")
public class ProductsFacadeREST extends AbstractFacade<Products> {
    @PersistenceContext(unitName = "com_omazon_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @EJB
    private com.omazon.business.ProductsFacade ejbFacade;

    public ProductsFacadeREST() {
        super(Products.class);
    }
    
    public ProductsFacade getFacade() {
        return ejbFacade;
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Products entity) {
        getFacade().create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Products entity) {
        getFacade().edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        getFacade().remove(getFacade().find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Products find(@PathParam("id") Integer id) {
        return getFacade().find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Products> findAll() {
        return getFacade().findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Products> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return getFacade().findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(getFacade().count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }    
}
