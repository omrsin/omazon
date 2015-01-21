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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omar
 */
@Entity
@Table(name = "TRUCKS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trucks.findAll", query = "SELECT t FROM Trucks t"),
    @NamedQuery(name = "Trucks.findById", query = "SELECT t FROM Trucks t WHERE t.id = :id"),
    @NamedQuery(name = "Trucks.findByLongitude", query = "SELECT t FROM Trucks t WHERE t.longitude = :longitude"),
    @NamedQuery(name = "Trucks.findByLatitude", query = "SELECT t FROM Trucks t WHERE t.latitude = :latitude")})
public class Trucks implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")    
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGITUDE")
    private float longitude;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATITUDE")
    private float latitude;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "truck")
    private List<Shipments> shipments;

    public Trucks() {
    }

    public Trucks(Integer id) {
        this.id = id;
    }

    public Trucks(Integer id, float longitude, float latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public List<Shipments> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipments> shipments) {
        this.shipments = shipments;
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
        if (!(object instanceof Trucks)) {
            return false;
        }
        Trucks other = (Trucks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.omazon.entities.Trucks[ id=" + id + " ]";
    }
    
}
