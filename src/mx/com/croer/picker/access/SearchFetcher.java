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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import mx.com.croer.entities.catalogodigital.Marca;
import mx.com.croer.entities.catalogodigital.Producto;
import mx.com.croer.entities.proxy.Item;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author elialva
 */
public class SearchFetcher extends DataPicker {

    @PersistenceContext(unitName = "JavaProject1PU3")
    private EntityManager em;

    @Autowired
    private FactoryFetcher ff;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    private String input;

    private List<Entry<Candidate, List>> selectedOnes(String[] hints, int pageNumber) {
        List<Candidate> candidateList = searchCandidates(hints, pageNumber);
        List<Entry<Candidate, List>> selectedList = new ArrayList<>();

        next_candidate:                 //Assess each candidate bean
        for (Candidate candidate : candidateList) {
            List<Entry<Integer, Integer>> sections = new ArrayList<>();
            StringBuilder context = new StringBuilder(candidate.getContext().toLowerCase());
//            System.out.println(">>>Candidate " + candidate.getContext());
            for (String hint : hints) { //All of hints must be discovered inside the candidate bean's context
//                System.out.println(">>> >Busca " + hint);
                List<XHint> xHintList = extendHints(hint, candidate.getIdBean()); //¿Deberia bastar xHintList.isEmpty para next_candidate?
                Entry<Integer, Integer> section = calculateSection(context, xHintList);
                if (section == null) {
                    continue next_candidate;
                }
                sections.add(section);
            }
            selectedList.add(new SimpleEntry(candidate, sections));
        }
        return selectedList;
    }

    private List<Candidate> searchCandidates(String[] hints, int pageNumber) {
        //Sustituye <where> & <numOfWords>
        String where = "";
        String like = "where s.numegrama like '%<numegrama>%'";
        for (String hint : hints) {
            where += like.replaceAll("<numegrama>", hint);
            like = " or s.numegrama like '%<numegrama>%'";
        }

        System.out.println("getType: " + getType().getSimpleName());

        String query = CandidateSql.
                replaceAll("<entity>", getType().getSimpleName()).
                replaceAll("<where>", where).
                replaceAll("<numOfWords>", hints.length + "").
                replaceAll("<offset>", createPageSize() * --pageNumber + "").
                replaceAll("<rows>", createPageSize() + 1 + "");

        System.out.println("BEG------------------------" + query);

        Query q = em.createNativeQuery(query).setMaxResults(Integer.MAX_VALUE).setFirstResult(0); //Manejar paginación aqui
        List<Candidate> list = new ArrayList<>();
        List<Object[]> resultList = q.getResultList();
        for (Object[] row : resultList) {
            Candidate candidate = new Candidate();
            for (int i = 0; i < row.length; i++) {
                switch (i) {
                    case 0:
                        candidate.setIdBean(row[0]);
                    case 1:
                        candidate.setContext((String) row[1]);
                }
            }
            list.add(candidate);
        }
//        Query q = em.createNativeQuery(query, "CandidateResult"); //Manejar paginación aqui
//        List<Candidate> list = q.getResultList();
        for (Candidate candidate : list) {
//            System.out.println("CanditateMOOF " + candidate + " - " + candidate.getContext() + candidate.getIdBean());
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
    private List<XHint> extendHints(String hint, Object key) {

        String tmpQuery = XHintSql.
                replaceAll("<entity>", getType().getSimpleName()).
                replaceAll("<idBean>", key.toString());

        List<XHint> list = new ArrayList();
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(tmpQuery.replaceAll("<hint>", hint));
        for (Map<String, Object> map : queryForList) {
            XHint xHint = new XHint();
            xHint.setHint((String) map.get("hint"));
            xHint.setNumegrama((String) map.get("numegrama"));
            xHint.setSimigrama((String) map.get("simigrama"));
            xHint.setOrtograma((String) map.get("ortograma"));
            xHint.setAlineacion((String) map.get("alineacion"));
//            System.out.println("Display xHint " + xHint.getHint() + " " + xHint.getOrtograma());
            list.add(xHint);
        }
        return list;
    }

    private static Entry<Integer, Integer> calculateSection(StringBuilder contexto, List<XHint> xHints) {

        XHint leftXHint = null;
        int leftPos = contexto.length();
        for (XHint xHint : xHints) {
            String orthogram = xHint.getOrtograma();
            int i = contexto.indexOf(orthogram);
            if (i >= 0 && i < leftPos) {
                leftPos = i;
                leftXHint = xHint;
            }

            if (i >= 0) {
//                System.out.println(">>> >>Halle:   " + xHint.getHint() + "-" + xHint.getOrtograma());
            } else {
//                System.out.println(">>> >>NoHalle: " + xHint.getHint() + "-" + xHint.getOrtograma());
            }
        }

        if (leftXHint == null) {
//            System.out.println(">>> >>Sin coincidencias.");
            return null;
        }

//        System.out.println(">>> >>CoincidenciaX: " + leftXHint.getHint() + "-" + leftXHint.getOrtograma());
        String hint = leftXHint.getHint();
        String numegrama = leftXHint.getNumegrama();
        String orthogram = leftXHint.getOrtograma();
        String alineacion = leftXHint.getAlineacion();

        int offset = numegrama.indexOf(hint);

        contexto.replace(leftPos, leftPos + orthogram.length(), StringUtils.repeat(" ", orthogram.length()));

        char[] c = alineacion.substring(offset, offset + hint.length()).toCharArray();
        int a = leftPos + c[0];
        int b = leftPos + c[c.length - 1];

//        new SimpleEntry(orthopos + inklpos, end - begin + 1);
        return new SimpleEntry(a, b - a + 1);
    }

    private List<Item> assembleItems(List<Entry<Candidate, List>> selectedList) {
        List<Item> itemList = new ArrayList();
        for (Entry<Candidate, List> entry : selectedList) {
            Candidate candidate = entry.getKey();
            List alignment = entry.getValue();
            Object businessObj = ff.createBusinessObject(candidate.getIdBean(), getType());
            Item item = ff.createItem(businessObj);
            item.setContext(candidate.getContext());
            item.setAlignment(alignment);
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public List<Item> createPage(Object pageHeader, int pageNumber) {

        System.out.println("pageClass " + pageHeader.getClass() + " +++ " + this.toString());

        if (pageHeader.getClass() == String.class) {
            input = pageHeader.toString();
        }

        System.out.println("iputtttt " + input);

        //Prepara, Valida y Divide la entrada
        String[] hints = StringUtils.split(input);

        //Select the candidates
        List<Entry<Candidate, List>> selectedList = selectedOnes(hints, pageNumber);

        //Create the items
        List<Item> assembleItems = assembleItems(selectedList);

        int indexOf = assembleItems.indexOf(pageHeader);

        System.out.println("Fetch with " + pageHeader + " * " + pageNumber + "###" + indexOf);
        
        try {
            System.out.println("ThreadRRR "  + Thread.currentThread());
            Thread.sleep(2000);
            System.out.println("BoteroGordo " + Thread.interrupted());
        } catch (InterruptedException ex) {
            Logger.getLogger(SearchFetcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (indexOf >= 0) {
            int i = indexOf + createPageSize() + 1;
            if (i > assembleItems.size()) {
                i = assembleItems.size();
            }
            return assembleItems.subList(indexOf, i);
        } else {
            return assembleItems;
        }

//        return assembleItems;
    }

    @Override
    public Icon createIcon(Item item) {
        Icon image = new ImageIcon("C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\mvc\\Banana-icon.png");
        if (item.getSource().getClass() == Producto.class) {
            //Baja al directorio y busca con la llave el icono
        }
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(SearchFetcher.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return image;
    }

    @Override
    public int createPageSize() {
        if (type == Producto.class) {
            return 3; //TODO probar con 4
        }
        return 100000;
    }

//<editor-fold defaultstate="collapsed" desc="comment">
    /**
     * @return the type
     */
    public Class getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Class type) {
        this.type = type;
    }
//</editor-fold>

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        SearchFetcher searchFetcher = (SearchFetcher) context.getBean("searchFetcher");
        searchFetcher.setType(Marca.class);

        List<Item> page2 = searchFetcher.createPage("72 ", 0);
        for (Item itemProxy : page2) {
            System.out.println(" Fuente " + itemProxy.getSource() + " Alineación " + itemProxy.getAlignment() + " Contexto " + itemProxy.getContext());
        }

        searchFetcher.setType(Producto.class);
        List<Item> page = searchFetcher.createPage("2  ", 0);
        for (Item itemProxy : page) {
            System.out.println(" Fuente " + itemProxy.getSource() + " Alineación " + itemProxy.getAlignment() + " Contexto " + itemProxy.getContext());
        }

        List<Item> page3 = searchFetcher.createPage("62 8 ", 0);
        for (Item itemProxy : page3) {
            System.out.println(" Fuente " + itemProxy.getSource() + " Alineación " + itemProxy.getAlignment() + " Contexto " + itemProxy.getContext());
        }
    }
}
