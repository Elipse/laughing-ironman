/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.com.croer.catalogodigital.entities.Marca;
import mx.com.croer.catalogodigital.entities.Producto;
import mx.com.croer.picker.mvc.ItemProxy;
import mx.com.croer.picker.mvc.MarcaW;
import mx.com.croer.picker.mvc.ProductoW;
import static mx.com.croer.picker.mvc.Selector.queryCandidates;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;

public class ItemCreatorImpl implements ItemCreator {

    @PersistenceContext(unitName = "JavaProject1PU2")
    private EntityManager em;

//    @Value("${CandidateFile}")
    private static final String CandidateFile = "C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\access\\CandidateSql.txt";
    private static String CandidateSql = "";

//    @Value("${XHintFile}")
    private static final String XHintFile = "C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\access\\XHintSql.txt";
    private static String XHintSql = "";

    static {
        try {
            List<String> readLines = FileUtils.readLines(new File(CandidateFile));
            for (String string : readLines) {
                CandidateSql += string;
//                queryCandidates = queryCandidates.replaceAll("<entity>", type.getSimpleName());
            }
            readLines = FileUtils.readLines(new File(XHintFile));
            for (String string : readLines) {
                XHintSql += string;
//                queryXHints = queryXHints.replaceAll("<entity>", type.getSimpleName());
            }
        } catch (IOException ex) {
            Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object createItem(Object key, Class type) {
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
//            return new ArrayList();
        }
        return null;
    }

    @Override
    public ItemProxy createItemProxy(Object item) {
        if (item.getClass() == Producto.class) {
            return new ProductoW((Producto) item);
        }
        if (item.getClass() == Marca.class) {
            return new MarcaW((Marca) item);
        }
        return null;
    }

    @Override
    public String getCandidateSql() {
        return CandidateSql;
    }

    @Override
    public String geXHintSql() {
        return XHintSql;
    }

}
