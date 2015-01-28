/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.boundary;

import com.omazon.boundary.util.JsfUtil;
import com.omazon.business.ShipmentsFacade;
import com.omazon.entities.Shipments;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author omar
 */

@Named("shipmentsController")
@RequestScoped
public class ShipmentsController implements Serializable {
    private Shipments current;
    private DataModel items = null;
    
    @EJB
    private com.omazon.business.ShipmentsFacade ejbFacade;
    
    @EJB
    private com.omazon.business.email.EmailSessionBean ejbEmail;
    
    private List<Shipments> availableShipments;

    public List<Shipments> getAvailableShipments() {
        availableShipments = new ArrayList<>();
        DataModel m = getItems();        
        for (Iterator iterator = m.iterator(); iterator.hasNext();) {
            Shipments next = (Shipments) iterator.next();
            availableShipments.add(next);
        }
        return availableShipments;
    }
    
    public ShipmentsController() {
    }

    public Shipments getSelected() {
        if (current == null) {
            current = new Shipments();
        }
        return current;
    }

    private ShipmentsFacade getFacade() {
        return ejbFacade;
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }   
    
    public String prepareView(int id) {
        current = (Shipments) getFacade().find(id);
        return "View";
    }

    public String prepareEdit(int id) {
        current = (Shipments) getFacade().find(id);
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Status updated successfully");
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String updateToDeliveredStatus(int id) {
        current = (Shipments) getFacade().find(id);
        current.setStatus(1);        
        return update();
    }

    public DataModel getItems() {
        items = new ListDataModel(getFacade().findAll());
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
    public Shipments getShipments(java.lang.Integer id) {
        return ejbFacade.find(id);
    }
    
    public void sendEmail() {
        String to = "omrsin@gmail.com";
        String subject = "This is just a test with java mail";
        String body = "This might be longer than I thought, however it is an email!";
        
        ejbEmail.sendEmail(to, subject, body);
    }

    @FacesConverter(forClass = Shipments.class)
    public static class ShipmentsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ShipmentsController controller = (ShipmentsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productsController");
            return controller.getShipments(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Shipments) {
                Shipments o = (Shipments) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Shipments.class.getName());
            }
        }

    }
}
