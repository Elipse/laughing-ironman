/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package creabusqueda;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.com.croer.entities.busqueda.Alineacion;
import mx.com.croer.entities.busqueda.ItemOrtograma;
import mx.com.croer.entities.busqueda.Itembusq;
import mx.com.croer.entities.busqueda.Ortograma;
import mx.com.croer.entities.busqueda.Simigrama;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elialva
 */
public class Context {

    private static String createNumegrama(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        text = StringUtils.replaceChars(text, "abcdefghijklmnopqrstuvwxyz", "22233344455566677778889999");
        return text;
    }

    private static List<Ortograma> createOrtogramaList(Itembusq itembusq) {
        List<Ortograma> list = new ArrayList<>();

        String[] split = StringUtils.split(itembusq.getContexto());
        for (String string : split) {
            String ortogramaStr = StringUtils.lowerCase(string);
            String numegramaStr = createNumegrama(ortogramaStr);
            Ortograma ortograma = new Ortograma(ortogramaStr, numegramaStr);
            list.add(ortograma);
        }

        return list;
    }

    private static List<Simigrama> createSimigramaList(List<Ortograma> list) {

        return null;
    }

    private static List<Alineacion> createAlineacionList(List<Ortograma> ortogramaList, List<Simigrama> simigramaList) {
        return null;
    }

    private static List<ItemOrtograma> createItemOrtogramaList(Itembusq itembusq, List<Ortograma> ortogramaList) {
        return null;
    }

    private static Itembusq createItembusq(String type, String key, String context) {
        System.out.println("type    " + type);
        System.out.println("key     " + key);
        System.out.println("context " + context);
        Itembusq itembusq = new Itembusq(key, type);
        itembusq.setContexto(context);
        itembusq.setPrioridad(0);
        return itembusq;
    }

    private static String createKey(Object bean, List<String> idProps) {
        String key = "";
        for (String name : idProps) {
            try {
                String tmp = BeanUtils.getProperty(bean, name);
                if (key.isEmpty()) {
                    key = tmp;
                } else {
                    key = key + ":" + tmp;
                }
            } catch (Exception ex) {
                Logger.getLogger(Context.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return key;
    }

    private static String createContext(Object bean, List<String> contextProps) {
        String context = "";
        for (String name : contextProps) {
            try {
                String tmp = BeanUtils.getProperty(bean, name);
                if (context.isEmpty()) {
                    context = tmp;
                } else {
                    context = context + " " + tmp;
                }
            } catch (Exception ex) {
                Logger.getLogger(Context.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return context;
    }

    public static void saveContext(Object bean, List<String> idProps, List<String> contextProps) {
        String key = createKey(bean, idProps);
        String context = createContext(bean, contextProps);
        Itembusq itembusq = createItembusq(bean.getClass().getName(), key, context);
        List<Ortograma> ortogramaList = createOrtogramaList(itembusq);
        List<Simigrama> simigramaList = createSimigramaList(ortogramaList);
        List<Alineacion> alineacionList = createAlineacionList(ortogramaList, simigramaList);
        List<ItemOrtograma> itemOrtogramaList = createItemOrtogramaList(itembusq, ortogramaList);
    }
}
