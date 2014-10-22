/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.breakdown;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elialva
 */
public class NewJFrame extends javax.swing.JFrame {

    private static List<List<String>> getVariations(List<String> hangers) {

        List<List<String>> variations = new ArrayList<>();

        for (String hanger : hangers) {
            if (Cue.FONEMAS.containsKey(hanger)) {
                List<Entry<String, String>> list = Cue.FONEMAS.get(hanger);
                List tmpList = new ArrayList(list);
                tmpList.add(0, hanger);
                variations.add(tmpList);
            } else {
                variations.add(Collections.singletonList(hanger));
            }
        }
        return variations;
    }

    private static String sequence(int from, int to) {
        String s = "";

        for (int i = from; i < to; i++) {
            s = s + i;
        }
        System.out.println("SWE " + s);
        return s;
    }

    private static List<List<Entry<String, String>>> getVariationsAndAlignments(List<String> hangers) {
        List<List<Entry<String, String>>> variations = new ArrayList<>();

        int i = 1;
        for (String hanger : hangers) {
            List tmpList = new ArrayList();
            String sequence = sequence(i, i + hanger.length());

            if (Cue.FONEMAS.containsKey(hanger)) {
                List<Entry<String, String>> list = Cue.FONEMAS.get(hanger);
                for (Entry<String, String> entry : list) {
                    System.out.println("ACambiar " + entry.getValue() + " list " + list);
                    char[] charA = entry.getValue().toCharArray();
                    String seqMod = offset(sequence, entry.getValue());
//                    String seqMod = "";
//                    for (int j = 0; j < charA.length; j++) {
//                        if (charA[j] == '_') {
//                            seqMod = seqMod + charA[j];
//                        } else {
//                            int a = new Integer(charA[j] + "") - 1;
//                            seqMod = seqMod + sequence.substring(a, a + 1);
//                        }
//                    }

                    tmpList.add(new SimpleEntry(entry.getKey(), seqMod));
                }
            }

            tmpList.add(0, new SimpleEntry(hanger, sequence));
            System.out.println("tempooo " + tmpList);
            variations.add(tmpList);

            i = i + hanger.length();
        }

        return variations;
    }

    private static List<String> generateWords(List<List<String>> variations) {
        List resultList = new ArrayList();
        resultList.add("");
        for (List variation : variations) {
            List tmp = new ArrayList();
            for (Object object : resultList) {
                for (Object object1 : variation) {
                    String cadena = object.toString() + object1.toString();
                    tmp.add(cadena);
                }
            }
            resultList = tmp;
        }
        System.out.println("resultList" + resultList);
        return resultList;
    }

    private static List<Entry<String, String>> generateWordsX(List<List<Entry<String, String>>> variations) {
//            
        List<Entry<String, String>> resultList = new ArrayList();
        resultList.add(new SimpleEntry("", ""));
        for (List<Entry<String, String>> variation : variations) {
            List<Entry<String, String>> tmp = new ArrayList();
            for (Entry<String, String> entry1 : resultList) {
                for (Entry<String, String> entry2 : variation) {
                    String cadena = entry1.getKey() + entry2.getKey();
                    String cadena2 = entry1.getValue() + entry2.getValue();
                    tmp.add(new SimpleEntry<>(cadena, cadena2));
                }
            }
            resultList = tmp;
        }
        System.out.println("resultListXXX " + resultList);
        return resultList;
    }

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }

    /**
     * 
     * @param text 
     * @return A sorted map with word-frequency pairs
     */
    private static TreeMap<String, Integer> breakDownText(String text) {
        String[] split = StringUtils.split(text);
        TreeMap<String, Integer> map = new TreeMap();
        for (String string : split) {
            int frecuency = map.containsKey(string) ? map.get(string) + 1 : 1;
            map.put(string, frecuency);
        }
        return map;
    }

    private static List<String> breakDownWord(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        Map<String, List<Entry<String, String>>> fonemas = Cue.FONEMAS;
        Map<Integer, String> map = new TreeMap();
        for (Map.Entry<String, List<Entry<String, String>>> entry : fonemas.entrySet()) {
            String key = entry.getKey();
            int pos = stringBuilder.indexOf(key);
            while (pos >= 0) {
                map.put(pos, key);
                stringBuilder.replace(pos, pos + key.length(), StringUtils.repeat(" ", key.length()));
                pos = stringBuilder.indexOf(key);
            }
        }

        List p = new ArrayList();
        int i = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            Integer pos = entry.getKey();
            String key = entry.getValue();
            String ini = string.substring(i, pos);
            if (!ini.isEmpty()) {
                p.add(ini);
            }
            String mid = string.substring(pos, pos + key.length());
            if (!mid.isEmpty()) {
                p.add(mid);
            }
            i = pos + key.length();
        }
        if (!string.substring(i).isEmpty()) {
            p.add(string.substring(i));
        }

        System.out.println("Map " + map);
        System.out.println("Colgantes " + p);

        return p;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.setText("Alta");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jTextField2.setText("Busqueda");

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(96, 96, 96))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private static String offset(String count, String offset) {
        System.out.println("count " + count + " :f: " + offset);
        String[] split = StringUtils.split(offset, ":");
        char[] charA = split[0].toCharArray();
        char[] charB = split[1].toCharArray();
        int j = 0;
        String tmp = "";
        for (int i = 0; i < charA.length; i++) {
            if (charA[i] != '_') {
                j++;
            }

            if (charB[i] == '_') {
                continue;
            }

            if (charA[i] == '_') {
                tmp = tmp + '_';
            } else {
//                tmp = tmp + (char)j;
                tmp = tmp + count.charAt(j - 1);   //Con j recupera la sequence asignada a este hanger
            }
        }
        return tmp;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
        //--------------------------------------------------------
        TreeMap<String, Integer> breakDownText = breakDownText("artiqueso artikeso llano botiyegua llano");
        System.out.println("breakDownText "  + breakDownText);
        Map map = new TreeMap();
        for (Map.Entry<String, Integer> entry : breakDownText.entrySet()) {
            String orthogram = entry.getKey();
            List<String> hangers = breakDownWord(orthogram);
            System.out.println("hangers " + hangers);
//            List<List<String>> variations = getVariations(hangers);
//            System.out.println("variations " + variations);
//            List<String> words = generateWords(variations);

            List<List<Entry<String, String>>> variationsAlignments = getVariationsAndAlignments(hangers);
            System.out.println("variationsAlignments " + variationsAlignments);
            List<Entry<String, String>> wordsX = generateWordsX(variationsAlignments);
            System.out.println("wordsX " + wordsX);

            map.put(orthogram, wordsX);

            System.out.println("offset " + offset("789", "que:_ke"));
            System.out.println("offset " + offset("45", "k_e:que"));
            System.out.println("offset " + offset("789", "que:k_e"));
        }

//        getXHintList(map);            genera una lista de XHint ortograma * simigramas (calcula numegramas, alineacion)
//        recordBusq(List<XHint>)       con la lista de XHint graba en la BD / puede ser directo sin el meth anterior  
        System.out.println("Mapot " + map);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
