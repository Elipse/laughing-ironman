/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.breakdown;

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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elialva
 */
public class Cue {

    public static Map<String, List<Entry<String, String>>> FONEMAS = new HashMap<>();

    static {
        String path = "C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\breakdown\\fonemas.txt";
        List<String> readLines = null;
        try {
            readLines = FileUtils.readLines(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(Cue.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String string : readLines) {
            String[] split = StringUtils.split(string);
            System.out.println("string " + string);
            if (FONEMAS.containsKey(split[0])) {
                List<Entry<String, String>> variations = FONEMAS.get(split[0]);
                variations.add(new SimpleEntry<>(split[1], split[2]));
            } else {
                List<Entry<String, String>> variations = new ArrayList<>();
                variations.add(new SimpleEntry<>(split[1], split[2]));
                FONEMAS.put(split[0], variations);
            }
        }
        System.out.println("Fonemas " + FONEMAS);
    }

    public static void main(String[] args) throws IOException {
    }

}
