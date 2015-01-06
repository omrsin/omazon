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
import com.omazon.entities.Customers;
import com.omazon.entities.Orders;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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
    private Customers customerSelected;

    public Customers getCustomerSelected() {
        return customerSelected;
    }

    public void setCustomerSelected(Customers customerSelected) {
        this.customerSelected = customerSelected;
    }

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

    public Orders getSelected() {
        if (current == null) {
            current = new Orders();
        }
        return current;
    }

    public DataModel getItems() {
        System.out.println("Hello first");
        if (items == null) {
            System.out.println("Hello");
            if (customerSelected != null) {
                items = getPagination(true).createPageDataModel();
            } else {
                items = getPagination(false).createPageDataModel();
            }
        }
        return items;
    }

    public PaginationHelper getPagination(final boolean param) {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return param ? getFacade().findByCustomer(customerSelected).size() : getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return param ? new ListDataModel(getFacade().findByCustomerGetRange(customerSelected, new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}))
                            : new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;

    }

    private OrdersFacade getFacade() {
        return ordersFacade;
    }

    public String create() {
        try {
            current.setShipmentId(2);
            getOrdersFacade().create(current);
            JsfUtil.addSuccessMessage("Yuhuuu");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.getMessage());
        }
        return "List";
    }

    private void recreateModel() {
        items = null;
    }
}
