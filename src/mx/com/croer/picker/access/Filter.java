/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import mx.com.croer.catalogodigital.entities.Marca;
import mx.com.croer.catalogodigital.entities.Producto;
import mx.com.croer.picker.mvc.ItemProxy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 *
 * @author elialva
 */
public class Filter extends JdbcDaoSupport {

    @PersistenceContext(unitName = "JavaProject1PU3")
    private EntityManager em;

    @Autowired
    private ItemCreator itemC;

    private String CandidateSql;

    private String XHintSql;

    public List<Entry<Candidate, List>> selectedOnes(String[] hints) {
        List<Candidate> candidateList = searchCandidates(hints);
        List<Entry<Candidate, List>> selectedList = new ArrayList<>();

        //Assess each candidate bean
        candidates:
        for (Candidate candidate : candidateList) {
            List<Entry<Integer, Integer>> sections = new ArrayList<>();
            StringBuilder context = new StringBuilder(candidate.getContext().toLowerCase());
            List<XHint> xHintList = extendHints(hints, candidate.getIdBean());
            //All of hints must be discovered inside the candidate bean's context
            hints:
            for (String hint : hints) {
                for (int i = 0; i < xHintList.size(); i++) {
                    XHint xHint = xHintList.get(i);
                    if (xHint.getHint().equals(hint)) {
                        sections.add(calculateSection(context, xHint)); //Calcula sección para añadirla a delimitOk
                        xHintList.remove(i);                            //Se remueve para asegurar la concordancia en número para los numegramas duplicados
                        continue hints;                                 //Continua con el siguiente numegrama
                    }
                }
                continue candidates; //Llego aquí sin encontrar coincidencias, continua con el siguiente candidato
            }
            selectedList.add(new SimpleEntry(candidate, sections));
        }
        return selectedList;
    }

    private List<Candidate> searchCandidates(String[] hints) {
        //Sustituye <where> & <numOfWords>
        String where = "";
        String like = "where s.numegrama like '%<numegrama>%'";
        for (String hint : hints) {
            where += like.replaceAll("<numegrama>", hint);
            like = " or s.numegrama like '%<numegrama>%'";
        }
        String query = CandidateSql.replaceAll("<where>", where);
        query = query.replaceAll("<numOfWords>", hints.length + "");

        Query q = em.createNativeQuery(query, "CandidateResult"); //Manejar paginación aqui
        List<Candidate> list = q.getResultList();

        return list;
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
    /**
     * Given a hint set, extend each hint with its numegraph, simigraph,
     * orthograph and alignment.
     * <p>
     * Extend each hint inside the scope of candidate bean.
     * <p>
     * @param hints Set of hints to extend
     * @param key Candidate bean's key
     * @return an List that contains a Map per hint
     */
    //</editor-fold>
    private List<XHint> extendHints(String[] hints, Object key) {

        String tmpQuery = XHintSql.replaceAll("<idBean>", key.toString());

        List<XHint> list = new ArrayList();
        for (String hint : hints) {
            List<Map<String, Object>> queryForList = getJdbcTemplate().queryForList(tmpQuery.replaceAll("<hint>", hint));
            for (Map<String, Object> map : queryForList) {
                XHint xHint = new XHint();
                xHint.setHint((String) map.get("hint"));
                xHint.setNumegrama((String) map.get("numegrama"));
                xHint.setSimigrama((String) map.get("simigrama"));
                xHint.setOrtograma((String) map.get("ortograma"));
                xHint.setAlineacion((String) map.get("alineacion"));
                list.add(xHint);
            }
        }
        return list;
    }

    private static Entry<Integer, Integer> calculateSection(StringBuilder contexto, XHint xHint) {

        String hint = xHint.getHint();
        String numegrama = xHint.getNumegrama();
        String ortograma = xHint.getOrtograma();
        String alineacion = xHint.getAlineacion();

        int j = contexto.indexOf(ortograma);

        int i = numegrama.indexOf(hint);
        char[] c = alineacion.substring(i, i + hint.length()).toCharArray();
        int a = j + c[0];
        int b = j + c[c.length - 1];

        return new SimpleEntry(a, b);
    }

    private List<ItemProxy> assembleItems(List<Entry<Candidate, List>> selectedList, Class type) {
        List<ItemProxy> itemList = new ArrayList();
        for (Entry<Candidate, List> entry : selectedList) {
            Candidate candidate = entry.getKey();
            List alignment = entry.getValue();
            Object item = itemC.createItem(candidate.getIdBean(), type);
            ItemProxy itemProxy = itemC.createItemProxy(item);
            itemProxy.setContext(candidate.getContext());
            itemProxy.setAlignment(alignment);
            itemList.add(itemProxy);
        }
        return itemList;
    }

    public List<ItemProxy> getPage(String input, Class type) {

        CandidateSql = itemC.getCandidateSql().replaceAll("<entity>", type.getSimpleName());
        XHintSql = itemC.geXHintSql().replaceAll("<entity>", type.getSimpleName());

        //Valida y divide la entrada
        String[] hints = StringUtils.split(input);
        //Selecciona los candidatos
        List<Entry<Candidate, List>> selectedList = selectedOnes(hints);
        //Crea los Items
        List<ItemProxy> assembleItems = assembleItems(selectedList, type);

        return assembleItems;
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        Filter candidateK = (Filter) context.getBean("candidateK");

        List<ItemProxy> page2 = candidateK.getPage("72 ", Marca.class);
        for (ItemProxy itemProxy : page2) {
            System.out.println(" Fuente " + itemProxy.getSource() + " Alineación " + itemProxy.getAlignment() + " Contexto " + itemProxy.getContext());
        }

        List<ItemProxy> page = candidateK.getPage("62  8", Producto.class);
        for (ItemProxy itemProxy : page) {
            System.out.println(" Fuente " + itemProxy.getSource() + " Alineación " + itemProxy.getAlignment() + " Contexto " + itemProxy.getContext());
        }
    }
}
