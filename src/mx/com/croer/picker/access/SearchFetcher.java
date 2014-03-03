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
import mx.com.croer.picker.mvc.Item;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 *
 * @author elialva
 */
public class SearchFetcher extends JdbcDaoSupport {

    @PersistenceContext(unitName = "JavaProject1PU3")
    private EntityManager em;

    @Autowired
    private BusinessFetcher bf;

    private static final String CandidateFile = "C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\access\\CandidateSql.txt";
    private static String CandidateSql = "";

    private static final String XHintFile = "C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\access\\XHintSql.txt";
    private static String XHintSql = "";

    private Class type;

    static {
        try {
            List<String> readLines = FileUtils.readLines(new File(CandidateFile));
            for (String string : readLines) {
                CandidateSql += string;
            }
            readLines = FileUtils.readLines(new File(XHintFile));
            for (String string : readLines) {
                XHintSql += string;
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchFetcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
                    System.out.println(">>> Candidate " + candidate.getContext() + "-" + hint + "-" + xHint.getOrtograma());
                    System.out.println("Context. " + context);
                    if (xHint.getHint().equals(hint)) {
                        sections.add(calculateSection(context, xHint)); //Calcula sección para añadirla a delimitOk
                        xHintList.remove(i);                            //Se remueve para asegurar la concordancia en número para los numegramas duplicados
                        System.out.println("Context, " + context);
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
        String query = CandidateSql.replaceAll("<entity>", type.getSimpleName());
        query = query.replaceAll("<where>", where).replaceAll("<numOfWords>", hints.length + "");

        System.out.println("BEG------------------------");

        Query q = em.createNativeQuery(query).setMaxResults(Integer.MAX_VALUE).setFirstResult(0); //Manejar paginación aqui
        List<Candidate> list = new ArrayList<>();
        List<Object[]> resultList = q.getResultList();
        for (Object[] row : resultList) {
            Candidate candidate = new Candidate();
            for (int i = 0; i < row.length; i++) {
                switch (i) {
                    case 0:
                        candidate.setIdBean(new Integer(row[0].toString()));
                    case 1:
                        candidate.setContext((String) row[1]);
                }
            }
            list.add(candidate);
        }
//        Query q = em.createNativeQuery(query, "CandidateResult"); //Manejar paginación aqui
//        List<Candidate> list = q.getResultList();
        for (Candidate candidate : list) {
            System.out.println("CanditateMOOF " + candidate + " - " + candidate.getContext() + candidate.getIdBean());
        }
        System.out.println("END------------------------");

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

        String tmpQuery = XHintSql.replaceAll("<entity>", type.getSimpleName());
        tmpQuery = tmpQuery.replaceAll("<idBean>", key.toString());

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
        String orthogram = xHint.getOrtograma();
        String alineacion = xHint.getAlineacion();

        int j = contexto.indexOf(orthogram);

        int i = numegrama.indexOf(hint);
        if (i < 0 || j < 0) {
            System.out.println("ABNORMAL ENDING - LOGIC ERROR");
            System.exit(0);
        }

        contexto.replace(j, j + orthogram.length(), StringUtils.repeat(" ", orthogram.length()));

        char[] c = alineacion.substring(i, i + hint.length()).toCharArray();
        int a = j + c[0];
        int b = j + c[c.length - 1];

        return new SimpleEntry(a, b);
    }

    private List<Item> assembleItems(List<Entry<Candidate, List>> selectedList, Class type) {
        List<Item> itemList = new ArrayList();
        for (Entry<Candidate, List> entry : selectedList) {
            Candidate candidate = entry.getKey();
            List alignment = entry.getValue();
            Object item = bf.createBusinessObject(candidate.getIdBean(), type);
            Item itemProxy = bf.createItem(item);
            itemProxy.setContext(candidate.getContext());
            itemProxy.setAlignment(alignment);
            itemList.add(itemProxy);
        }
        return itemList;
    }

    public List<Item> getPage(String input, Class type) {

        this.type = type;

        //Prepara, Valida y Divide la entrada
        String[] hints = StringUtils.split(input);

        //Select the candidates
        List<Entry<Candidate, List>> selectedList = selectedOnes(hints);

        //Create the items
        List<Item> assembleItems = assembleItems(selectedList, type);

        return assembleItems;
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        SearchFetcher searchFetcher = (SearchFetcher) context.getBean("searchFetcher");

        List<Item> page2 = searchFetcher.getPage("72 ", Marca.class);
        for (Item itemProxy : page2) {
            System.out.println(" Fuente " + itemProxy.getSource() + " Alineación " + itemProxy.getAlignment() + " Contexto " + itemProxy.getContext());
        }
        List<Item> page = searchFetcher.getPage("2  ", Producto.class);
        for (Item itemProxy : page) {
            System.out.println(" Fuente " + itemProxy.getSource() + " Alineación " + itemProxy.getAlignment() + " Contexto " + itemProxy.getContext());
        }

        List<Item> page3 = searchFetcher.getPage("62 8 ", Producto.class);
        for (Item itemProxy : page3) {
            System.out.println(" Fuente " + itemProxy.getSource() + " Alineación " + itemProxy.getAlignment() + " Contexto " + itemProxy.getContext());
        }
    }
}
