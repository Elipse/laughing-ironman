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
import mx.com.croer.entities.busqueda.ItemOrtograma;
import mx.com.croer.entities.busqueda.Ortograma;
import mx.com.croer.entities.busqueda.controllers.exceptions.IllegalOrphanException;
import mx.com.croer.entities.busqueda.controllers.exceptions.NonexistentEntityException;
import mx.com.croer.entities.busqueda.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author elialva
 */
public class OrtogramaJpaController1 implements Serializable {

    public OrtogramaJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ortograma ortograma) throws PreexistingEntityException, Exception {
        if (ortograma.getAlineacionCollection() == null) {
            ortograma.setAlineacionCollection(new ArrayList<Alineacion>());
        }
        if (ortograma.getItemOrtogramaCollection() == null) {
            ortograma.setItemOrtogramaCollection(new ArrayList<ItemOrtograma>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Alineacion> attachedAlineacionCollection = new ArrayList<Alineacion>();
            for (Alineacion alineacionCollectionAlineacionToAttach : ortograma.getAlineacionCollection()) {
                alineacionCollectionAlineacionToAttach = em.getReference(alineacionCollectionAlineacionToAttach.getClass(), alineacionCollectionAlineacionToAttach.getAlineacionPK());
                attachedAlineacionCollection.add(alineacionCollectionAlineacionToAttach);
            }
            ortograma.setAlineacionCollection(attachedAlineacionCollection);
            Collection<ItemOrtograma> attachedItemOrtogramaCollection = new ArrayList<ItemOrtograma>();
            for (ItemOrtograma itemOrtogramaCollectionItemOrtogramaToAttach : ortograma.getItemOrtogramaCollection()) {
                itemOrtogramaCollectionItemOrtogramaToAttach = em.getReference(itemOrtogramaCollectionItemOrtogramaToAttach.getClass(), itemOrtogramaCollectionItemOrtogramaToAttach.getItemOrtogramaPK());
                attachedItemOrtogramaCollection.add(itemOrtogramaCollectionItemOrtogramaToAttach);
            }
            ortograma.setItemOrtogramaCollection(attachedItemOrtogramaCollection);
            em.persist(ortograma);
            for (Alineacion alineacionCollectionAlineacion : ortograma.getAlineacionCollection()) {
                Ortograma oldOrtograma1OfAlineacionCollectionAlineacion = alineacionCollectionAlineacion.getOrtograma1();
                alineacionCollectionAlineacion.setOrtograma1(ortograma);
                alineacionCollectionAlineacion = em.merge(alineacionCollectionAlineacion);
                if (oldOrtograma1OfAlineacionCollectionAlineacion != null) {
                    oldOrtograma1OfAlineacionCollectionAlineacion.getAlineacionCollection().remove(alineacionCollectionAlineacion);
                    oldOrtograma1OfAlineacionCollectionAlineacion = em.merge(oldOrtograma1OfAlineacionCollectionAlineacion);
                }
            }
            for (ItemOrtograma itemOrtogramaCollectionItemOrtograma : ortograma.getItemOrtogramaCollection()) {
                Ortograma oldOrtograma1OfItemOrtogramaCollectionItemOrtograma = itemOrtogramaCollectionItemOrtograma.getOrtograma1();
                itemOrtogramaCollectionItemOrtograma.setOrtograma1(ortograma);
                itemOrtogramaCollectionItemOrtograma = em.merge(itemOrtogramaCollectionItemOrtograma);
                if (oldOrtograma1OfItemOrtogramaCollectionItemOrtograma != null) {
                    oldOrtograma1OfItemOrtogramaCollectionItemOrtograma.getItemOrtogramaCollection().remove(itemOrtogramaCollectionItemOrtograma);
                    oldOrtograma1OfItemOrtogramaCollectionItemOrtograma = em.merge(oldOrtograma1OfItemOrtogramaCollectionItemOrtograma);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrtograma(ortograma.getOrtograma()) != null) {
                throw new PreexistingEntityException("Ortograma " + ortograma + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ortograma ortograma) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ortograma persistentOrtograma = em.find(Ortograma.class, ortograma.getOrtograma());
            Collection<Alineacion> alineacionCollectionOld = persistentOrtograma.getAlineacionCollection();
            Collection<Alineacion> alineacionCollectionNew = ortograma.getAlineacionCollection();
            Collection<ItemOrtograma> itemOrtogramaCollectionOld = persistentOrtograma.getItemOrtogramaCollection();
            Collection<ItemOrtograma> itemOrtogramaCollectionNew = ortograma.getItemOrtogramaCollection();
            List<String> illegalOrphanMessages = null;
            for (Alineacion alineacionCollectionOldAlineacion : alineacionCollectionOld) {
                if (!alineacionCollectionNew.contains(alineacionCollectionOldAlineacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Alineacion " + alineacionCollectionOldAlineacion + " since its ortograma1 field is not nullable.");
                }
            }
            for (ItemOrtograma itemOrtogramaCollectionOldItemOrtograma : itemOrtogramaCollectionOld) {
                if (!itemOrtogramaCollectionNew.contains(itemOrtogramaCollectionOldItemOrtograma)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemOrtograma " + itemOrtogramaCollectionOldItemOrtograma + " since its ortograma1 field is not nullable.");
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
            ortograma.setAlineacionCollection(alineacionCollectionNew);
            Collection<ItemOrtograma> attachedItemOrtogramaCollectionNew = new ArrayList<ItemOrtograma>();
            for (ItemOrtograma itemOrtogramaCollectionNewItemOrtogramaToAttach : itemOrtogramaCollectionNew) {
                itemOrtogramaCollectionNewItemOrtogramaToAttach = em.getReference(itemOrtogramaCollectionNewItemOrtogramaToAttach.getClass(), itemOrtogramaCollectionNewItemOrtogramaToAttach.getItemOrtogramaPK());
                attachedItemOrtogramaCollectionNew.add(itemOrtogramaCollectionNewItemOrtogramaToAttach);
            }
            itemOrtogramaCollectionNew = attachedItemOrtogramaCollectionNew;
            ortograma.setItemOrtogramaCollection(itemOrtogramaCollectionNew);
            ortograma = em.merge(ortograma);
            for (Alineacion alineacionCollectionNewAlineacion : alineacionCollectionNew) {
                if (!alineacionCollectionOld.contains(alineacionCollectionNewAlineacion)) {
                    Ortograma oldOrtograma1OfAlineacionCollectionNewAlineacion = alineacionCollectionNewAlineacion.getOrtograma1();
                    alineacionCollectionNewAlineacion.setOrtograma1(ortograma);
                    alineacionCollectionNewAlineacion = em.merge(alineacionCollectionNewAlineacion);
                    if (oldOrtograma1OfAlineacionCollectionNewAlineacion != null && !oldOrtograma1OfAlineacionCollectionNewAlineacion.equals(ortograma)) {
                        oldOrtograma1OfAlineacionCollectionNewAlineacion.getAlineacionCollection().remove(alineacionCollectionNewAlineacion);
                        oldOrtograma1OfAlineacionCollectionNewAlineacion = em.merge(oldOrtograma1OfAlineacionCollectionNewAlineacion);
                    }
                }
            }
            for (ItemOrtograma itemOrtogramaCollectionNewItemOrtograma : itemOrtogramaCollectionNew) {
                if (!itemOrtogramaCollectionOld.contains(itemOrtogramaCollectionNewItemOrtograma)) {
                    Ortograma oldOrtograma1OfItemOrtogramaCollectionNewItemOrtograma = itemOrtogramaCollectionNewItemOrtograma.getOrtograma1();
                    itemOrtogramaCollectionNewItemOrtograma.setOrtograma1(ortograma);
                    itemOrtogramaCollectionNewItemOrtograma = em.merge(itemOrtogramaCollectionNewItemOrtograma);
                    if (oldOrtograma1OfItemOrtogramaCollectionNewItemOrtograma != null && !oldOrtograma1OfItemOrtogramaCollectionNewItemOrtograma.equals(ortograma)) {
                        oldOrtograma1OfItemOrtogramaCollectionNewItemOrtograma.getItemOrtogramaCollection().remove(itemOrtogramaCollectionNewItemOrtograma);
                        oldOrtograma1OfItemOrtogramaCollectionNewItemOrtograma = em.merge(oldOrtograma1OfItemOrtogramaCollectionNewItemOrtograma);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ortograma.getOrtograma();
                if (findOrtograma(id) == null) {
                    throw new NonexistentEntityException("The ortograma with id " + id + " no longer exists.");
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
            Ortograma ortograma;
            try {
                ortograma = em.getReference(Ortograma.class, id);
                ortograma.getOrtograma();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ortograma with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Alineacion> alineacionCollectionOrphanCheck = ortograma.getAlineacionCollection();
            for (Alineacion alineacionCollectionOrphanCheckAlineacion : alineacionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ortograma (" + ortograma + ") cannot be destroyed since the Alineacion " + alineacionCollectionOrphanCheckAlineacion + " in its alineacionCollection field has a non-nullable ortograma1 field.");
            }
            Collection<ItemOrtograma> itemOrtogramaCollectionOrphanCheck = ortograma.getItemOrtogramaCollection();
            for (ItemOrtograma itemOrtogramaCollectionOrphanCheckItemOrtograma : itemOrtogramaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ortograma (" + ortograma + ") cannot be destroyed since the ItemOrtograma " + itemOrtogramaCollectionOrphanCheckItemOrtograma + " in its itemOrtogramaCollection field has a non-nullable ortograma1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ortograma);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ortograma> findOrtogramaEntities() {
        return findOrtogramaEntities(true, -1, -1);
    }

    public List<Ortograma> findOrtogramaEntities(int maxResults, int firstResult) {
        return findOrtogramaEntities(false, maxResults, firstResult);
    }

    private List<Ortograma> findOrtogramaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ortograma.class));
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

    public Ortograma findOrtograma(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ortograma.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrtogramaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ortograma> rt = cq.from(Ortograma.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
