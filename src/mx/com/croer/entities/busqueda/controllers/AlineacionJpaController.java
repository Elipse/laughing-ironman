/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.entities.busqueda.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mx.com.croer.entities.busqueda.Alineacion;
import mx.com.croer.entities.busqueda.AlineacionPK;
import mx.com.croer.entities.busqueda.Ortograma;
import mx.com.croer.entities.busqueda.Simigrama;
import mx.com.croer.entities.busqueda.controllers.exceptions.NonexistentEntityException;
import mx.com.croer.entities.busqueda.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author elialva
 */
public class AlineacionJpaController implements Serializable {

    public AlineacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alineacion alineacion) throws PreexistingEntityException, Exception {
        if (alineacion.getAlineacionPK() == null) {
            alineacion.setAlineacionPK(new AlineacionPK());
        }
        alineacion.getAlineacionPK().setSimigrama(alineacion.getSimigrama1().getSimigrama());
        alineacion.getAlineacionPK().setOrtograma(alineacion.getOrtograma1().getOrtograma());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ortograma ortograma1 = alineacion.getOrtograma1();
            if (ortograma1 != null) {
                ortograma1 = em.getReference(ortograma1.getClass(), ortograma1.getOrtograma());
                alineacion.setOrtograma1(ortograma1);
            }
            Simigrama simigrama1 = alineacion.getSimigrama1();
            if (simigrama1 != null) {
                simigrama1 = em.getReference(simigrama1.getClass(), simigrama1.getSimigrama());
                alineacion.setSimigrama1(simigrama1);
            }
            em.persist(alineacion);
            if (ortograma1 != null) {
                ortograma1.getAlineacionCollection().add(alineacion);
                ortograma1 = em.merge(ortograma1);
            }
            if (simigrama1 != null) {
                simigrama1.getAlineacionCollection().add(alineacion);
                simigrama1 = em.merge(simigrama1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlineacion(alineacion.getAlineacionPK()) != null) {
                throw new PreexistingEntityException("Alineacion " + alineacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alineacion alineacion) throws NonexistentEntityException, Exception {
        alineacion.getAlineacionPK().setSimigrama(alineacion.getSimigrama1().getSimigrama());
        alineacion.getAlineacionPK().setOrtograma(alineacion.getOrtograma1().getOrtograma());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alineacion persistentAlineacion = em.find(Alineacion.class, alineacion.getAlineacionPK());
            Ortograma ortograma1Old = persistentAlineacion.getOrtograma1();
            Ortograma ortograma1New = alineacion.getOrtograma1();
            Simigrama simigrama1Old = persistentAlineacion.getSimigrama1();
            Simigrama simigrama1New = alineacion.getSimigrama1();
            if (ortograma1New != null) {
                ortograma1New = em.getReference(ortograma1New.getClass(), ortograma1New.getOrtograma());
                alineacion.setOrtograma1(ortograma1New);
            }
            if (simigrama1New != null) {
                simigrama1New = em.getReference(simigrama1New.getClass(), simigrama1New.getSimigrama());
                alineacion.setSimigrama1(simigrama1New);
            }
            alineacion = em.merge(alineacion);
            if (ortograma1Old != null && !ortograma1Old.equals(ortograma1New)) {
                ortograma1Old.getAlineacionCollection().remove(alineacion);
                ortograma1Old = em.merge(ortograma1Old);
            }
            if (ortograma1New != null && !ortograma1New.equals(ortograma1Old)) {
                ortograma1New.getAlineacionCollection().add(alineacion);
                ortograma1New = em.merge(ortograma1New);
            }
            if (simigrama1Old != null && !simigrama1Old.equals(simigrama1New)) {
                simigrama1Old.getAlineacionCollection().remove(alineacion);
                simigrama1Old = em.merge(simigrama1Old);
            }
            if (simigrama1New != null && !simigrama1New.equals(simigrama1Old)) {
                simigrama1New.getAlineacionCollection().add(alineacion);
                simigrama1New = em.merge(simigrama1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AlineacionPK id = alineacion.getAlineacionPK();
                if (findAlineacion(id) == null) {
                    throw new NonexistentEntityException("The alineacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AlineacionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alineacion alineacion;
            try {
                alineacion = em.getReference(Alineacion.class, id);
                alineacion.getAlineacionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alineacion with id " + id + " no longer exists.", enfe);
            }
            Ortograma ortograma1 = alineacion.getOrtograma1();
            if (ortograma1 != null) {
                ortograma1.getAlineacionCollection().remove(alineacion);
                ortograma1 = em.merge(ortograma1);
            }
            Simigrama simigrama1 = alineacion.getSimigrama1();
            if (simigrama1 != null) {
                simigrama1.getAlineacionCollection().remove(alineacion);
                simigrama1 = em.merge(simigrama1);
            }
            em.remove(alineacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alineacion> findAlineacionEntities() {
        return findAlineacionEntities(true, -1, -1);
    }

    public List<Alineacion> findAlineacionEntities(int maxResults, int firstResult) {
        return findAlineacionEntities(false, maxResults, firstResult);
    }

    private List<Alineacion> findAlineacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alineacion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Alineacion findAlineacion(AlineacionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alineacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlineacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alineacion> rt = cq.from(Alineacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
