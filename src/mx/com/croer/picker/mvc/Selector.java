/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
////import org.jdesktop.application.Application;
//import org.jdesktop.application.ResourceMap;

/**
 *
 * @author elialva
 */
public class Selector {

    private String input;
    private String propOrthogramLink;
    private String propIdBean;
    private String propDescription;
    private Object bean;
    public static String queryCandidates
            = "select idBean"
            + "      from ("
            + "      select distinct o.ortograma, o.idBean,  o.frecuencia"
            + "      from _simigrama as s inner join _alineacion as a "
            + "                                        on a.simigrama = s.simigrama"
            + "                                        inner join ortobean as o"
            + "                                        on o.ortograma = a.ortograma"
            + "      <where> "
            + "      ) as v1"
            + " group by idBean"
            + " having sum(frecuencia) >= <numOfWords>;";
    public static String queryOrthograms
            = "select \'<numegrama>\', s.numegrama, s.simigrama, a.ortograma, a.alineacion "
            + "from _simigrama as s inner join _alineacion as a "
            + "                     on         s.simigrama = a.simigrama "
            + "                     inner join ortobean as o"
            + "                     on         a.ortograma = o.ortograma"
            + " where s.numegrama like '%<numegrama>%'"
            + "  and idBean = <idBean>;";
    private final EntityManager em;

    public Selector() {
//        ResourceMap resourceMap = Application.getInstance(DesktopApplication1.class).getContext().getResourceMap(DesktopApplication1View.class);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("busquedaPU");
        System.out.println("emf " + emf);
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    public List<SimpleEntry<Integer, Integer>> calculateSection() {
        return null;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(Object bean) {
        this.bean = bean;
    }

//    public SimpleEntry<Integer, Integer> getSection(StringBuilder description, List<List<String>> dataset) {
    public SimpleEntry<Integer, Integer> getSection(StringBuilder description, List dataset) {
        int orthopos = description.length();
        Object[] tempRow = null;

        for (Object object : dataset) {
            Object[] row = (Object[]) object;
            String orthogram = (String) row[3];
            int pos = description.indexOf(orthogram);

            if (pos >= 0 && pos < orthopos) {
                orthopos = pos;
                tempRow = row;
            }
        }

        if (orthopos < description.length()) {
            String orthogram = (String) tempRow[3];

            description.replace(orthopos, orthopos + orthogram.length(), StringUtils.repeat(" ", orthogram.length()));

            String inkling = (String) tempRow[0];
            String numegram = (String) tempRow[1];
            String alignment = (String) tempRow[4];

            int inklpos = numegram.indexOf(inkling);

            int decena = 0;
            char[] toCharArray = alignment.toCharArray();
            int[] posic = new int[toCharArray.length];
            for (int i = 0; i < toCharArray.length; i++) {
                posic[i] = Integer.parseInt("" + toCharArray[i]) + decena;
                if (i + 1 < toCharArray.length && toCharArray[i] > toCharArray[i + 1]) {
                    decena += 10;
                }
            }

            String charpos = alignment.substring(inklpos, inklpos + inkling.length());

            System.out.println("charpos " + charpos + ":" + posic[inklpos] + ":" + posic[inklpos + inkling.length() - 1]);
            int begin = posic[inklpos];
            int end = posic[inklpos + inkling.length() - 1];

            return new SimpleEntry(orthopos + inklpos, end - begin + 1);
        } else {
            return null;
        }
    }

    public List<Integer> getCandidates(String[] inklings) {

        String where = "";
        String like = "where s.numegrama like '%<numegrama>%'";
        for (String inkling : inklings) {
            where += like.replaceAll("<numegrama>", inkling);
            like = " or s.numegrama like '%<numegrama>%'";
        }

        String query = queryCandidates.replaceAll("<where>", where);
        query = query.replaceAll("<numOfWords>", inklings.length + "");
        query = StringUtils.normalizeSpace(query);
        Query createNativeQuery = em.createNativeQuery(query);

        System.out.println("QueryOfCandidates " + query);
        System.out.println("ResultOfCandidates " + createNativeQuery.getResultList().getClass());

        return createNativeQuery.getResultList();
    }

    public List<List> getOrthograms(String[] inklings, Object key) {
        String queryBean = queryOrthograms.replaceAll("<idBean>", key.toString());

        ArrayList ortolist = new ArrayList();

        for (String inkling : inklings) {
            String query = queryBean.replaceAll("<numegrama>", inkling);
            Query createNativeQuery = em.createNativeQuery(query);
            List dataset = createNativeQuery.getResultList();
            for (Object object : dataset) {
                Object[] row = (Object[]) object;
                for (Object column : row) {
                    System.out.println("object1 Orht " + column);
                }                
            }
            System.out.println("inkling " + inkling);
            ortolist.add(dataset);
        }

        return ortolist;
    }

    public List<ProductoSearch> getPage(String input) {
        System.out.println("inputPeroo " + input);
        Query createNamedQuery = em.createNamedQuery("Busqproducto.findByIdBean");

        ArrayList<ProductoSearch> listaSelect = new ArrayList<ProductoSearch>();

        String[] split = StringUtils.split(input);
        List<Integer> candidates = getCandidates(split);

        for (Integer idItem : candidates) {
            System.out.println("debug " + split + ":" + input);
            List<List> orthograms = getOrthograms(split, idItem);
            createNamedQuery.setParameter("idBean", idItem);
            ProductoSearch busqproducto = (ProductoSearch) createNamedQuery.getSingleResult();
            String s = busqproducto.getDescripcion().toLowerCase();
            StringBuilder sb = new StringBuilder(s);
            ArrayList sections = new ArrayList();
            for (List dataset : orthograms) {
                SimpleEntry<Integer, Integer> section = getSection(sb, dataset);
                if (section != null) {
                    sections.add(section);
                }
            }

            System.out.println("sections.size() " + sections.size() + " orthograms.size() " + orthograms.size());
            if (sections.size() == orthograms.size()) {
//                busqproducto.setAlignment(sections);
                listaSelect.add(busqproducto);
            }
        }
        return listaSelect;
    }

    public static void main(String[] args) {
        Selector selector = new Selector();
        List<ProductoSearch> page = selector.getPage("2 9");
        for (ProductoSearch busqproducto : page) {
            System.out.println("busqproducto " + busqproducto.getIdproducto() + ":" + busqproducto.getDescripcion());
        }
    }
}
