/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omar
 */
@Entity
@Table(name = "ORDERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o"),
    @NamedQuery(name = "Orders.findById", query = "SELECT o FROM Orders o WHERE o.id = :id"),
    @NamedQuery(name = "Orders.findByShipmentId", query = "SELECT o FROM Orders o WHERE o.shipment.id = :shipmentId"),
    @NamedQuery(name = "Orders.findByCustomerId", query = "SELECT o FROM Orders o WHERE o.customer.id = :customerId")})
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)    
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;
    
    @ManyToOne(cascade=CascadeType.ALL, optional=false, fetch=FetchType.EAGER)
    @JoinColumn(name="SHIPMENT_ID", referencedColumnName="ID")
    private Shipments shipment;    

    @ManyToOne(optional=false, fetch=FetchType.EAGER)    
    @JoinColumn(name="CUSTOMER_ID", referencedColumnName="ID")
    private Customers customer;
    
    @ManyToMany
    @JoinTable(
            name="ORDERS_PRODUCTS",
            joinColumns={@JoinColumn(name="ORDER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="PRODUCT_ID", referencedColumnName="ID")})
    private List<Products> products;

    public Orders() {
    }

    public Orders(Integer id) {
        this.id = id;
    }

    public Orders(Integer id, Shipments shipment, List<Products> products, Customers customer) {
        this.id = id;
        this.shipment = shipment;
        this.customer = customer;
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Shipments getShipment() {
        return shipment;
    }

    public void setShipment(Shipments shipment) {
        this.shipment = shipment;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.omazon.entities.Orders[ id=" + id + " ]";
    }    
}