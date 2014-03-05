/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.com.croer.entities.catalogodigital.Marca;
import mx.com.croer.entities.catalogodigital.Producto;
import mx.com.croer.entities.proxy.Item;
import mx.com.croer.entities.proxy.MarcaW;
import mx.com.croer.entities.proxy.ProductoW;

public class BusinessFetcherImpl implements BusinessFetcher {

    @PersistenceContext(unitName = "JavaProject1PU2")
    private EntityManager em;

    @Override
    public Object createBusinessObject(Object key, Class type) {
        if (type == Producto.class) {
            TypedQuery<Producto> query = em.createNamedQuery("Producto.findByIdProducto", Producto.class);
            query.setParameter("idProducto", key);
            return query.getSingleResult();
//            return new ArrayList();
        }
        if (type == Marca.class) {
            TypedQuery<Marca> query = em.createNamedQuery("Marca.findByIdMarca", Marca.class);
            query.setParameter("idMarca", key);
            return query.getSingleResult();
        }
        return null;
    }

    @Override
    public Item createItem(Object item) {
        if (item.getClass() == Producto.class) {
            return new ProductoW((Producto) item);
        }
        if (item.getClass() == Marca.class) {
            return new MarcaW((Marca) item);
        }
        return null;
    }
}
