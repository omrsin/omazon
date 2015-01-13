package com.omazon.boundary;

import com.omazon.entities.Products;
import com.omazon.boundary.util.JsfUtil;
import com.omazon.business.ProductsFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("productsController")
@RequestScoped
public class ProductsController implements Serializable {

    private Products current;
    private DataModel items = null;
    
    @EJB
    private com.omazon.business.ProductsFacade ejbFacade;
    
    private List<Products> availableProducts;

    public List<Products> getAvailableProducts() {
        availableProducts = new ArrayList<>();
        DataModel m = getItems();
        for (Iterator iterator = m.iterator(); iterator.hasNext();) {
            Products next = (Products) iterator.next();
            availableProducts.add(next);
        }
        return availableProducts;
    }
    
    public ProductsController() {
    }

    public Products getSelected() {
        if (current == null) {
            current = new Products();
        }
        return current;
    }

    private ProductsFacade getFacade() {
        return ejbFacade;
    }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareCreate() {
        current = new Products();
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(int id) {
        System.out.println(id);
        current = (Products) getFacade().find(id);
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(int id) {
        current = (Products) getFacade().find(id);
        performDestroy();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductsDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
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

    public Products getProducts(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Products.class)
    public static class ProductsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductsController controller = (ProductsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productsController");
            return controller.getProducts(getKey(value));
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
            if (object instanceof Products) {
                Products o = (Products) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Products.class.getName());
            }
        }

    }

}
