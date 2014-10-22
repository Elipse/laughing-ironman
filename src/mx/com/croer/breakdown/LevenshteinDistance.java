//http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
package mx.com.croer.breakdown;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class LevenshteinDistance {

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private static int[][] computeMatrix(String x, String y) {
        int[][] distance = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= y.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= x.length(); i++) {
            for (int j = 1; j <= y.length(); j++) {
                distance[i][j] = minimum(
                        /*Del*/distance[i - 1][j] + 1,
                        /*Ins*/ distance[i][j - 1] + 1,
                        /*Sub*/ distance[i - 1][j - 1] + ((x.charAt(i - 1) == y.charAt(j - 1)) ? 0 : 1));
            }
        }
        return distance;
    }

    public static int computeLevenshteinDistance(String x, String y) {
        return computeMatrix(x, y)[x.length()][y.length()];
    }

    public static List<Entry<String, String>> allAlignments(String str1, String str2) {
        int[][] matrix = computeMatrix(str1, str2);
        String zx = "", zy = "";
        List list = new ArrayList();
        List<Entry<String, String>> allAlignment = allAlignment(str1, str1.length(), str2, str2.length(), zx, zy, matrix, list);

        return allAlignment;
    }

    public static Entry<String, String> oneAlignment(String x, String y) {
        int[][] T = computeMatrix(x, y);
        String zx = "", zy = "";
        int i = x.length();
        int j = y.length();
        while (i > 0 && j > 0) {
            if (T[i][j] == T[i - 1][j - 1] + ((x.charAt(i - 1) == y.charAt(j - 1)) ? 0 : 1)) {
                zx = x.charAt(--i) + zx;
                zy = y.charAt(--j) + zy;;
            } else {
                if (T[i][j] == T[i - 1][j] + 1) {
                    zx = x.charAt(--i) + zx;
                    zy = "-" + zy;
                } else {
                    if (T[i][j] == T[i][j - 1] + 1) {
                        zx = "+" + zx;
                        zy = y.charAt(--j) + zy;
                    }
                }
            }
        }

        while (i > 0) {
            if (T[i][j] == T[i - 1][j] + 1) {
                zx = x.charAt(--i) + zx;
                zy = "-" + zy;
            }
        }

        while (j > 0) {
            if (T[i][j] == T[i][j - 1] + 1) {
                zx = "+" + zx;
                zy = y.charAt(--j) + zy;
            }
        }

        return new SimpleEntry(zx, zy);
    }

    public static void main(String[] args) {
        System.out.println("c " + computeLevenshteinDistance("HOLA", "OLA"));
        List<Entry<String, String>> allAlignments = allAlignments("QUETAS", "KETASX");
        for (Entry<String, String> entry : allAlignments) {
            System.out.println(":" + entry.getKey() + "\n " + entry.getValue());
        }
        allAlignments = allAlignments("UEVO", "HUEVO");
        for (Entry<String, String> entry : allAlignments) {
            System.out.println(":" + entry.getKey() + "\n " + entry.getValue());
        }
//        Entry<String, String> oneAlignments = oneAlignment("MORRON", "MORON"); //12?345 Der-Izq
//        Entry<String, String> oneAlignments = oneAlignment("MORON", "MORRRON");  //12567  Cuando R pinta de 3 a 5
//        Entry<String, String> oneAlignments = oneAlignment("QUESO", "KESO");  //?1234 Der-Izq (u ES q,k) 
//        Entry<String, String> oneAlignments = oneAlignment("KESO", "QUESO");  //2345 Cuando K pinta de 1 a 2
//        Entry<String, String> oneAlignments = oneAlignment("ODET", "ODETT");  //1235 Cuando T pinta de 4 a 5
//        Entry<String, String> oneAlignments = oneAlignment("GUERANIO", "GERANIO"); //1?234567 Izq-Der
        Entry<String, String> oneAlignments = oneAlignment("GERRERO", "GUERRERO"); //1345678 Cuando G pinta de 1 a 2

        System.out.println("oneA " + oneAlignments.getKey() + "\noneB " + oneAlignments.getValue());
        String s = computeAlignment(oneAlignments.getKey(), oneAlignments.getValue());

        System.out.println("alineacionBD " + s);
    }

    private static List<Entry<String, String>> allAlignment(String x, int i, String y, int j, String zx, String zy, int[][] T, List list) {

        if (i > 0) {
            if (j > 0) {
                if (T[i][j] == T[i - 1][j - 1] + ((x.charAt(i - 1) == y.charAt(j - 1)) ? 0 : 1)) {
                    allAlignment(x, i - 1, y, j - 1, x.charAt(i - 1) + zx, y.charAt(j - 1) + zy, T, list);
                }
                if (T[i][j] == T[i - 1][j] + 1) {
                    allAlignment(x, i - 1, y, j, x.charAt(i - 1) + zx, "-" + zy, T, list);
                }
                if (T[i][j] == T[i][j - 1] + 1) {
                    allAlignment(x, i, y, j - 1, "+" + zx, y.charAt(j - 1) + zy, T, list);
                }
            } else {
                allAlignment(x, i - 1, y, 0, x.charAt(i - 1) + zx, "-" + zy, T, list);
            }
        } else {
            if (j > 0) {
                allAlignment(x, 0, y, j - 1, "+" + zx, y.charAt(j - 1) + zy, T, list);
            } else {
                list.add(new SimpleEntry(zx, zy));
            }
        }
        return list;
    }

    private static String computeAlignment(String simi, String orto) {
        char[] tmpAlineacion = new char[simi.length()];
        char[] simiA = simi.toCharArray();
        char[] ortoA = orto.toCharArray();
        int o = -1;
        for (int i = 0; i < simiA.length; i++) {
            if (ortoA[i] != '+' && ortoA[i] != '-') {
                o++;
            }

            if (simiA[i] == '+' || simiA[i] == '-' || ortoA[i] == '+' || ortoA[i] == '-') {
                tmpAlineacion[i] = '?';
                continue;
            }

            tmpAlineacion[i] = (char) (o + 1 + 48);
        }

        char[] simiOk = new char[simi.length()];
        char[] alineacion = new char[simi.length()];
        int j = 0;
        for (int i = 0; i < simiA.length; i++) {
            if (simiA[i] != '+' && simiA[i] != '-') {
                simiOk[j] = simiA[i];
                alineacion[j] = tmpAlineacion[i];
                j++;
            }
        }

        System.out.println("Stringi " + simi);
        System.out.println("Stringa " + new String(tmpAlineacion));
        System.out.println("---------------------------------");
        System.out.println("Stringi " + new String(simiOk));
        System.out.println("Stringa " + new String(alineacion));
        return new String(alineacion);
    }
}
