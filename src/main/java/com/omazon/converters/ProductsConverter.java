/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.converters;

import com.omazon.entities.Products;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;

/**
 *
 * @author floriment
 */
@FacesConverter("productsConverter")
public class ProductsConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<Products> products = (List<Products>) context.getApplication().evaluateExpressionGet(context,"#{productsController.availableProducts}", List.class);
        
        for(Products p : products)
        {
            if(p.getName().equals(value)){
                return p;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Products) value).getName();
    }

}
