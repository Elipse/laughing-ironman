/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author elialva
 */
public class CandidateK extends NamedParameterJdbcDaoSupport {

    public static String queryCandidates = "";
    public static String queryOrtogramas = "";

    public List<Map<String, Object>> candidates(String[] hints) {

        //Sustituye <where> & <numOfWords>
        String where = "";
        String like = "where s.numegrama like '%<numegrama>%'";
        for (String hint : hints) {
            where += like.replaceAll("<numegrama>", hint);
            like = " or s.numegrama like '%<numegrama>%'";
        }
        String query = queryCandidates.replaceAll("<where>", where);
        query = query.replaceAll("<numOfWords>", hints.length + "");
        query = StringUtils.normalizeSpace(query);

        List<Map<String, Object>> delimit = getJdbcTemplate().queryForList(query);
        List<Map<String, Object>> delimitOk = new ArrayList<>();
        
        candidates: //Evalúa todos los registros candidatos
        for (Map<String, Object> map : delimit) {
            List<Entry<Integer,Integer>> sections = new ArrayList<>();
            StringBuilder contexto = new StringBuilder(((String) map.get("contexto")).toLowerCase());
            List<Map<String, Object>> ortogramas = ortogramas(map.get("idBean"), hints); //Obtiene numegramas y anexas?!
            hints: //Debe hallar todos los numegramas en los registros candidatos
            for (String hint : hints) {
                for (int i = 0; i < ortogramas.size(); i++) {
                    Map<String, Object> ortoMap = ortogramas.get(i);
                    if (ortoMap.get("hint").equals(hint)) {
                        sections.add(section(contexto, ortoMap)); //Calcula sección para añadirla a delimitOk
                        ortogramas.remove(i);      //Se remueve para asegurar la concordancia en número para los numegramas duplicados
                        continue hints;            //Continua con el siguiente numegrama
                    }
                }
                continue candidates; //Llego aquí sin encontrar coincidencias, continua con el siguiente candidato
            }
            System.out.println("Candidato " + map + " - " + sections);
            //Recupera source, new a Item
            delimitOk.add(map);
        }
        return delimitOk;
    }

    private List<Map<String, Object>> ortogramas(Object get, String[] hints) {
        List list = new ArrayList();

        String tmpQuery = queryOrtogramas.replaceAll("<idBean>", get.toString());

        for (String hint : hints) {
            String bo = tmpQuery.replaceAll("<numegrama>", hint);
            List<Map<String, Object>> queryForList = getJdbcTemplate().queryForList(bo);
            for (Map<String, Object> map : queryForList) {
                list.add(map);
            }
        }
        return list;
    }

    public List<Map<String, Object>> getPage(String input, String entity) {
        try {
            List<String> readLines = FileUtils.readLines(new File("C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\access\\candidate.txt"));
            for (String string : readLines) {
                queryCandidates += string;
                queryCandidates = queryCandidates.replaceAll("<entity>", "Producto");
            }
            readLines = FileUtils.readLines(new File("C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\access\\ortogramas.txt"));
            for (String string : readLines) {
                queryOrtogramas += string;
                queryOrtogramas = queryOrtogramas.replaceAll("<entity>", "Producto");
            }
        } catch (IOException ex) {
            Logger.getLogger(CandidateK.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] hints = StringUtils.split(input);
        List<Map<String, Object>> delimit = candidates(hints);

        return delimit;
    }

    public static void main(String[] args) {
        CandidateK candidateK = new CandidateK();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        candidateK.setDataSource((DataSource) context.getBean("dataSourceSearch"));
        List<Map<String, Object>> delimit = candidateK.getPage("62 8 ", "Producto");
        for (Map<String, Object> map : delimit) {
            System.out.println("MapNor " + map);
        }
    }

    private Entry<Integer, Integer> section(StringBuilder contexto, Map<String, Object> ortoMap) {

        String hint = (String) ortoMap.get("hint");
        String numegrama = (String) ortoMap.get("numegrama");
        String ortograma = (String) ortoMap.get("ortograma");
        String alineacion = (String) ortoMap.get("alineacion");

        int j = contexto.indexOf(ortograma);

        int i = numegrama.indexOf(hint);
        char[] c = alineacion.substring(i, i + hint.length()).toCharArray();
        int a = j + c[0];
        int b = j + c[c.length - 1];

        return new SimpleEntry(a, b);
    }
}
