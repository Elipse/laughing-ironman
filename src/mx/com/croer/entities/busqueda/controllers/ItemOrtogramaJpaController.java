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
import mx.com.croer.entities.busqueda.ItemOrtograma;
import mx.com.croer.entities.busqueda.ItemOrtogramaPK;
import mx.com.croer.entities.busqueda.Ortograma;
import mx.com.croer.entities.busqueda.Itembusq;
import mx.com.croer.entities.busqueda.controllers.exceptions.NonexistentEntityException;
import mx.com.croer.entities.busqueda.controllers.exceptions.PreexistingEntityException;

/**
 *
 * @author elialva
 */
public class ItemOrtogramaJpaController implements Serializable {

    public ItemOrtogramaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemOrtograma itemOrtograma) throws PreexistingEntityException, Exception {
        if (itemOrtograma.getItemOrtogramaPK() == null) {
            itemOrtograma.setItemOrtogramaPK(new ItemOrtogramaPK());
        }
        itemOrtograma.getItemOrtogramaPK().setOrtograma(itemOrtograma.getOrtograma1().getOrtograma());
        itemOrtograma.getItemOrtogramaPK().setType(itemOrtograma.getItembusq().getItembusqPK().getType());
        itemOrtograma.getItemOrtogramaPK().setIdItem(itemOrtograma.getItembusq().getItembusqPK().getIdItem());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ortograma ortograma1 = itemOrtograma.getOrtograma1();
            if (ortograma1 != null) {
                ortograma1 = em.getReference(ortograma1.getClass(), ortograma1.getOrtograma());
                itemOrtograma.setOrtograma1(ortograma1);
            }
            Itembusq itembusq = itemOrtograma.getItembusq();
            if (itembusq != null) {
                itembusq = em.getReference(itembusq.getClass(), itembusq.getItembusqPK());
                itemOrtograma.setItembusq(itembusq);
            }
            em.persist(itemOrtograma);
            if (ortograma1 != null) {
                ortograma1.getItemOrtogramaCollection().add(itemOrtograma);
                ortograma1 = em.merge(ortograma1);
            }
            if (itembusq != null) {
                itembusq.getItemOrtogramaCollection().add(itemOrtograma);
                itembusq = em.merge(itembusq);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItemOrtograma(itemOrtograma.getItemOrtogramaPK()) != null) {
                throw new PreexistingEntityException("ItemOrtograma " + itemOrtograma + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItemOrtograma itemOrtograma) throws NonexistentEntityException, Exception {
        itemOrtograma.getItemOrtogramaPK().setOrtograma(itemOrtograma.getOrtograma1().getOrtograma());
        itemOrtograma.getItemOrtogramaPK().setType(itemOrtograma.getItembusq().getItembusqPK().getType());
        itemOrtograma.getItemOrtogramaPK().setIdItem(itemOrtograma.getItembusq().getItembusqPK().getIdItem());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemOrtograma persistentItemOrtograma = em.find(ItemOrtograma.class, itemOrtograma.getItemOrtogramaPK());
            Ortograma ortograma1Old = persistentItemOrtograma.getOrtograma1();
            Ortograma ortograma1New = itemOrtograma.getOrtograma1();
            Itembusq itembusqOld = persistentItemOrtograma.getItembusq();
            Itembusq itembusqNew = itemOrtograma.getItembusq();
            if (ortograma1New != null) {
                ortograma1New = em.getReference(ortograma1New.getClass(), ortograma1New.getOrtograma());
                itemOrtograma.setOrtograma1(ortograma1New);
            }
            if (itembusqNew != null) {
                itembusqNew = em.getReference(itembusqNew.getClass(), itembusqNew.getItembusqPK());
                itemOrtograma.setItembusq(itembusqNew);
            }
            itemOrtograma = em.merge(itemOrtograma);
            if (ortograma1Old != null && !ortograma1Old.equals(ortograma1New)) {
                ortograma1Old.getItemOrtogramaCollection().remove(itemOrtograma);
                ortograma1Old = em.merge(ortograma1Old);
            }
            if (ortograma1New != null && !ortograma1New.equals(ortograma1Old)) {
                ortograma1New.getItemOrtogramaCollection().add(itemOrtograma);
                ortograma1New = em.merge(ortograma1New);
            }
            if (itembusqOld != null && !itembusqOld.equals(itembusqNew)) {
                itembusqOld.getItemOrtogramaCollection().remove(itemOrtograma);
                itembusqOld = em.merge(itembusqOld);
            }
            if (itembusqNew != null && !itembusqNew.equals(itembusqOld)) {
                itembusqNew.getItemOrtogramaCollection().add(itemOrtograma);
                itembusqNew = em.merge(itembusqNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ItemOrtogramaPK id = itemOrtograma.getItemOrtogramaPK();
                if (findItemOrtograma(id) == null) {
                    throw new NonexistentEntityException("The itemOrtograma with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ItemOrtogramaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemOrtograma itemOrtograma;
            try {
                itemOrtograma = em.getReference(ItemOrtograma.class, id);
                itemOrtograma.getItemOrtogramaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemOrtograma with id " + id + " no longer exists.", enfe);
            }
            Ortograma ortograma1 = itemOrtograma.getOrtograma1();
            if (ortograma1 != null) {
                ortograma1.getItemOrtogramaCollection().remove(itemOrtograma);
                ortograma1 = em.merge(ortograma1);
            }
            Itembusq itembusq = itemOrtograma.getItembusq();
            if (itembusq != null) {
                itembusq.getItemOrtogramaCollection().remove(itemOrtograma);
                itembusq = em.merge(itembusq);
            }
            em.remove(itemOrtograma);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ItemOrtograma> findItemOrtogramaEntities() {
        return findItemOrtogramaEntities(true, -1, -1);
    }

    public List<ItemOrtograma> findItemOrtogramaEntities(int maxResults, int firstResult) {
        return findItemOrtogramaEntities(false, maxResults, firstResult);
    }

    private List<ItemOrtograma> findItemOrtogramaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemOrtograma.class));
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

    public ItemOrtograma findItemOrtograma(ItemOrtogramaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemOrtograma.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemOrtogramaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemOrtograma> rt = cq.from(ItemOrtograma.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
