/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.entities.busqueda.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import mx.com.croer.entities.busqueda.Alineacion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mx.com.croer.entities.busqueda.Simigrama;
import mx.com.croer.entities.busqueda.controllers.exceptions.IllegalOrphanException;
import mx.com.croer.entities.busqueda.controllers.exceptions.NonexistentEntityException;
import mx.com.croer.entities.busqueda.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author elialva
 */
public class SimigramaJpaController implements Serializable {

    public SimigramaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Simigrama simigrama) throws PreexistingEntityException, Exception {
        if (simigrama.getAlineacionCollection() == null) {
            simigrama.setAlineacionCollection(new ArrayList<Alineacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Alineacion> attachedAlineacionCollection = new ArrayList<Alineacion>();
            for (Alineacion alineacionCollectionAlineacionToAttach : simigrama.getAlineacionCollection()) {
                alineacionCollectionAlineacionToAttach = em.getReference(alineacionCollectionAlineacionToAttach.getClass(), alineacionCollectionAlineacionToAttach.getAlineacionPK());
                attachedAlineacionCollection.add(alineacionCollectionAlineacionToAttach);
            }
            simigrama.setAlineacionCollection(attachedAlineacionCollection);
            em.persist(simigrama);
            for (Alineacion alineacionCollectionAlineacion : simigrama.getAlineacionCollection()) {
                Simigrama oldSimigrama1OfAlineacionCollectionAlineacion = alineacionCollectionAlineacion.getSimigrama1();
                alineacionCollectionAlineacion.setSimigrama1(simigrama);
                alineacionCollectionAlineacion = em.merge(alineacionCollectionAlineacion);
                if (oldSimigrama1OfAlineacionCollectionAlineacion != null) {
                    oldSimigrama1OfAlineacionCollectionAlineacion.getAlineacionCollection().remove(alineacionCollectionAlineacion);
                    oldSimigrama1OfAlineacionCollectionAlineacion = em.merge(oldSimigrama1OfAlineacionCollectionAlineacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSimigrama(simigrama.getSimigrama()) != null) {
                throw new PreexistingEntityException("Simigrama " + simigrama + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Simigrama simigrama) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Simigrama persistentSimigrama = em.find(Simigrama.class, simigrama.getSimigrama());
            Collection<Alineacion> alineacionCollectionOld = persistentSimigrama.getAlineacionCollection();
            Collection<Alineacion> alineacionCollectionNew = simigrama.getAlineacionCollection();
            List<String> illegalOrphanMessages = null;
            for (Alineacion alineacionCollectionOldAlineacion : alineacionCollectionOld) {
                if (!alineacionCollectionNew.contains(alineacionCollectionOldAlineacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alineacion " + alineacionCollectionOldAlineacion + " since its simigrama1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Alineacion> attachedAlineacionCollectionNew = new ArrayList<Alineacion>();
            for (Alineacion alineacionCollectionNewAlineacionToAttach : alineacionCollectionNew) {
                alineacionCollectionNewAlineacionToAttach = em.getReference(alineacionCollectionNewAlineacionToAttach.getClass(), alineacionCollectionNewAlineacionToAttach.getAlineacionPK());
                attachedAlineacionCollectionNew.add(alineacionCollectionNewAlineacionToAttach);
            }
            alineacionCollectionNew = attachedAlineacionCollectionNew;
            simigrama.setAlineacionCollection(alineacionCollectionNew);
            simigrama = em.merge(simigrama);
            for (Alineacion alineacionCollectionNewAlineacion : alineacionCollectionNew) {
                if (!alineacionCollectionOld.contains(alineacionCollectionNewAlineacion)) {
                    Simigrama oldSimigrama1OfAlineacionCollectionNewAlineacion = alineacionCollectionNewAlineacion.getSimigrama1();
                    alineacionCollectionNewAlineacion.setSimigrama1(simigrama);
                    alineacionCollectionNewAlineacion = em.merge(alineacionCollectionNewAlineacion);
                    if (oldSimigrama1OfAlineacionCollectionNewAlineacion != null && !oldSimigrama1OfAlineacionCollectionNewAlineacion.equals(simigrama)) {
                        oldSimigrama1OfAlineacionCollectionNewAlineacion.getAlineacionCollection().remove(alineacionCollectionNewAlineacion);
                        oldSimigrama1OfAlineacionCollectionNewAlineacion = em.merge(oldSimigrama1OfAlineacionCollectionNewAlineacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = simigrama.getSimigrama();
                if (findSimigrama(id) == null) {
                    throw new NonexistentEntityException("The simigrama with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Simigrama simigrama;
            try {
                simigrama = em.getReference(Simigrama.class, id);
                simigrama.getSimigrama();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The simigrama with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Alineacion> alineacionCollectionOrphanCheck = simigrama.getAlineacionCollection();
            for (Alineacion alineacionCollectionOrphanCheckAlineacion : alineacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Simigrama (" + simigrama + ") cannot be destroyed since the Alineacion " + alineacionCollectionOrphanCheckAlineacion + " in its alineacionCollection field has a non-nullable simigrama1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(simigrama);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Simigrama> findSimigramaEntities() {
        return findSimigramaEntities(true, -1, -1);
    }

    public List<Simigrama> findSimigramaEntities(int maxResults, int firstResult) {
        return findSimigramaEntities(false, maxResults, firstResult);
    }

    private List<Simigrama> findSimigramaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Simigrama.class));
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

    public Simigrama findSimigrama(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Simigrama.class, id);
        } finally {
            em.close();
        }
    }

    public int getSimigramaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Simigrama> rt = cq.from(Simigrama.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
