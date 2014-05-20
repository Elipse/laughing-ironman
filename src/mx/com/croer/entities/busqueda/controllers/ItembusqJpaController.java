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
import mx.com.croer.entities.busqueda.ItemOrtograma;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mx.com.croer.entities.busqueda.Itembusq;
import mx.com.croer.entities.busqueda.ItembusqPK;
import mx.com.croer.entities.busqueda.controllers.exceptions.IllegalOrphanException;
import mx.com.croer.entities.busqueda.controllers.exceptions.NonexistentEntityException;
import mx.com.croer.entities.busqueda.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author elialva
 */
public class ItembusqJpaController implements Serializable {

    public ItembusqJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Itembusq itembusq) throws PreexistingEntityException, Exception {
        if (itembusq.getItembusqPK() == null) {
            itembusq.setItembusqPK(new ItembusqPK());
        }
        if (itembusq.getItemOrtogramaCollection() == null) {
            itembusq.setItemOrtogramaCollection(new ArrayList<ItemOrtograma>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            Collection<ItemOrtograma> attachedItemOrtogramaCollection = new ArrayList<ItemOrtograma>();
            for (ItemOrtograma itemOrtogramaCollectionItemOrtogramaToAttach : itembusq.getItemOrtogramaCollection()) {
                itemOrtogramaCollectionItemOrtogramaToAttach = em.getReference(itemOrtogramaCollectionItemOrtogramaToAttach.getClass(), itemOrtogramaCollectionItemOrtogramaToAttach.getItemOrtogramaPK());
                attachedItemOrtogramaCollection.add(itemOrtogramaCollectionItemOrtogramaToAttach);
            }
            itembusq.setItemOrtogramaCollection(attachedItemOrtogramaCollection);
            em.persist(itembusq);
            for (ItemOrtograma itemOrtogramaCollectionItemOrtograma : itembusq.getItemOrtogramaCollection()) {
                Itembusq oldItembusqOfItemOrtogramaCollectionItemOrtograma = itemOrtogramaCollectionItemOrtograma.getItembusq();
                itemOrtogramaCollectionItemOrtograma.setItembusq(itembusq);
                itemOrtogramaCollectionItemOrtograma = em.merge(itemOrtogramaCollectionItemOrtograma);
                if (oldItembusqOfItemOrtogramaCollectionItemOrtograma != null) {
                    oldItembusqOfItemOrtogramaCollectionItemOrtograma.getItemOrtogramaCollection().remove(itemOrtogramaCollectionItemOrtograma);
                    oldItembusqOfItemOrtogramaCollectionItemOrtograma = em.merge(oldItembusqOfItemOrtogramaCollectionItemOrtograma);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItembusq(itembusq.getItembusqPK()) != null) {
                throw new PreexistingEntityException("Itembusq " + itembusq + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Itembusq itembusq) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Itembusq persistentItembusq = em.find(Itembusq.class, itembusq.getItembusqPK());
            Collection<ItemOrtograma> itemOrtogramaCollectionOld = persistentItembusq.getItemOrtogramaCollection();
            Collection<ItemOrtograma> itemOrtogramaCollectionNew = itembusq.getItemOrtogramaCollection();
            List<String> illegalOrphanMessages = null;
            for (ItemOrtograma itemOrtogramaCollectionOldItemOrtograma : itemOrtogramaCollectionOld) {
                if (!itemOrtogramaCollectionNew.contains(itemOrtogramaCollectionOldItemOrtograma)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemOrtograma " + itemOrtogramaCollectionOldItemOrtograma + " since its itembusq field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ItemOrtograma> attachedItemOrtogramaCollectionNew = new ArrayList<ItemOrtograma>();
            for (ItemOrtograma itemOrtogramaCollectionNewItemOrtogramaToAttach : itemOrtogramaCollectionNew) {
                itemOrtogramaCollectionNewItemOrtogramaToAttach = em.getReference(itemOrtogramaCollectionNewItemOrtogramaToAttach.getClass(), itemOrtogramaCollectionNewItemOrtogramaToAttach.getItemOrtogramaPK());
                attachedItemOrtogramaCollectionNew.add(itemOrtogramaCollectionNewItemOrtogramaToAttach);
            }
            itemOrtogramaCollectionNew = attachedItemOrtogramaCollectionNew;
            itembusq.setItemOrtogramaCollection(itemOrtogramaCollectionNew);
            itembusq = em.merge(itembusq);
            for (ItemOrtograma itemOrtogramaCollectionNewItemOrtograma : itemOrtogramaCollectionNew) {
                if (!itemOrtogramaCollectionOld.contains(itemOrtogramaCollectionNewItemOrtograma)) {
                    Itembusq oldItembusqOfItemOrtogramaCollectionNewItemOrtograma = itemOrtogramaCollectionNewItemOrtograma.getItembusq();
                    itemOrtogramaCollectionNewItemOrtograma.setItembusq(itembusq);
                    itemOrtogramaCollectionNewItemOrtograma = em.merge(itemOrtogramaCollectionNewItemOrtograma);
                    if (oldItembusqOfItemOrtogramaCollectionNewItemOrtograma != null && !oldItembusqOfItemOrtogramaCollectionNewItemOrtograma.equals(itembusq)) {
                        oldItembusqOfItemOrtogramaCollectionNewItemOrtograma.getItemOrtogramaCollection().remove(itemOrtogramaCollectionNewItemOrtograma);
                        oldItembusqOfItemOrtogramaCollectionNewItemOrtograma = em.merge(oldItembusqOfItemOrtogramaCollectionNewItemOrtograma);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ItembusqPK id = itembusq.getItembusqPK();
                if (findItembusq(id) == null) {
                    throw new NonexistentEntityException("The itembusq with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ItembusqPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Itembusq itembusq;
            try {
                itembusq = em.getReference(Itembusq.class, id);
                itembusq.getItembusqPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itembusq with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ItemOrtograma> itemOrtogramaCollectionOrphanCheck = itembusq.getItemOrtogramaCollection();
            for (ItemOrtograma itemOrtogramaCollectionOrphanCheckItemOrtograma : itemOrtogramaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Itembusq (" + itembusq + ") cannot be destroyed since the ItemOrtograma " + itemOrtogramaCollectionOrphanCheckItemOrtograma + " in its itemOrtogramaCollection field has a non-nullable itembusq field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(itembusq);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Itembusq> findItembusqEntities() {
        return findItembusqEntities(true, -1, -1);
    }

    public List<Itembusq> findItembusqEntities(int maxResults, int firstResult) {
        return findItembusqEntities(false, maxResults, firstResult);
    }

    private List<Itembusq> findItembusqEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Itembusq.class));
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

    public Itembusq findItembusq(ItembusqPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Itembusq.class, id);
        } finally {
            em.close();
        }
    }

    public int getItembusqCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Itembusq> rt = cq.from(Itembusq.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
