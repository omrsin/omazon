/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omar
 */
@Entity
@Table(name = "SHIPMENTS")
@XmlRootElement
@Cacheable(false)
@NamedQueries({
    @NamedQuery(name = "Shipments.findAll", query = "SELECT s FROM Shipments s"),
    @NamedQuery(name = "Shipments.findById", query = "SELECT s FROM Shipments s WHERE s.id = :id"),
    @NamedQuery(name = "Shipments.findByStatus", query = "SELECT s FROM Shipments s WHERE s.status = :status"),
    @NamedQuery(name = "Shipments.countByStatus", query = "SELECT COUNT(s) FROM Shipments s WHERE s.status = :status"),
    @NamedQuery(name = "Shipments.maxByStatus", query = "SELECT s FROM Shipments s WHERE s.id = (SELECT MAX(x.id) FROM Shipments x WHERE x.status = :status)")})
public class Shipments implements Serializable {
    
    public static final int NOT_DELIVERED = 0;
    public static final int DELIVERED = 1;
    public static final int IN_PROGRESS = 2;

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shipment", fetch=FetchType.EAGER)
    private List<Orders> orders;
    
    @ManyToOne(cascade=CascadeType.ALL, optional=false, fetch=FetchType.EAGER)
    @JoinColumn(name="TRUCK_ID", referencedColumnName="ID")
    private Trucks truck;

    @Transient
    private String writtenStatus;

    public Shipments() {
    }

    public Shipments(Integer id) {
        this.id = id;
    }

    public Shipments(Integer id, int status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
    
    public String getWrittenStatus() {
        switch (this.status) {
            case NOT_DELIVERED:
                writtenStatus = "Not Delivered";
                break;
            case DELIVERED:
                writtenStatus = "Delivered";
                break;
            case IN_PROGRESS:
                writtenStatus = "In Progress";
                break;
        }

        return writtenStatus;
    }

    public void setWrittenStatus(String writtenStatus) {
        this.writtenStatus = writtenStatus;
    }

    public Trucks getTruck() {
        return truck;
    }

    public void setTruck(Trucks truck) {
        this.truck = truck;
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
        if (!(object instanceof Shipments)) {
            return false;
        }
        Shipments other = (Shipments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.omazon.entities.Shipments[ id=" + id + " ]";
    }
}
