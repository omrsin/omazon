/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.omazon;

import com.omazon.entities.Customers;
import com.omazon.entities.Orders;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author omar
 */
public class EntitiesTest {
    
    private static EntityManager em = null;
        
    public EntitiesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
//        if (em == null) {
//            em = (EntityManager) Persistence.createEntityManagerFactory("omazon_test").createEntityManager();
//        }
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void testCustomerWithOneOrder() {
//         em.getTransaction().begin();
//         
//         Customers customer = new Customers();
//         customer.setName("Omar");
//         customer.setEmail("omar@omar.com");
//         Orders order = new Orders();
//         order.setShipmentId(1);
//         em.persist(customer);
//         em.flush();
//         
//         customer.getOrders().add(order);
//         order.setCustomer(customer);
//         
//         em.merge(customer);
//         em.flush();
//         
//         em.getTransaction().commit();
     }
}
