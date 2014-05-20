/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.breakdown;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author elialva
 */
@Repository("eamControler")
@Transactional
public class ItemCntrllrJpa implements ItemCntrllr {

    @PersistenceContext(unitName = "PUBusqueda")
    private EntityManager em;

    @Override
    public void addItem() {
//        em.createQuery("select ddd ");
        System.out.println("Añadiendo ItemOP " + em);
    }
}
