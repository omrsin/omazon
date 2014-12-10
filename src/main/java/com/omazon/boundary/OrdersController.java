/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.boundary;

import com.omazon.boundary.util.JsfUtil;
import com.omazon.boundary.util.PaginationHelper;
import com.omazon.business.CustomersFacade;
import com.omazon.business.OrdersFacade;
import com.omazon.business.ProductsFacade;
import com.omazon.entities.Orders;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Named;

/**
 *
 * @author omar
 */

@Named("ordersController")
@RequestScoped
public class OrdersController implements Serializable {
    
    private Orders current;
    private DataModel items = null;
    @EJB
    private com.omazon.business.OrdersFacade ordersFacade;
    @EJB
    private com.omazon.business.CustomersFacade customersFacade;
    @EJB
    private com.omazon.business.ProductsFacade productsFacade;
    private PaginationHelper pagination;

    public OrdersFacade getOrdersFacade() {
        return ordersFacade;
    }

    public CustomersFacade getCustomersFacade() {
        return customersFacade;
    }

    public ProductsFacade getProductsFacade() {
        return productsFacade;
    }    
    
    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public Orders getSelected(){
        if(current==null){
            current = new Orders();
        }
        return current;
    }
    
    public String create(){
        try{
            current.setShipmentId(2);
            getOrdersFacade().create(current);
            JsfUtil.addSuccessMessage("Yuhuuu");
        }catch(Exception e)
        {
            JsfUtil.addErrorMessage(e.getMessage());
        }
        return "List";
    }

    private void recreateModel() {
        items = null;
    }
}
