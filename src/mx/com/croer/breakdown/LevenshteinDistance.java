//http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
package mx.com.croer.breakdown;

import org.apache.commons.lang3.StringUtils;

public class LevenshteinDistance {

    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static int computeLevenshteinDistance(String str1, String str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= str2.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1, //Del
                        distance[i][j - 1] + 1, //Ins
                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));     //Sub
            }
        }

        String string = "";
        allAlignment(str1, str1.length(), str2, str2.length(), string, distance);

        return distance[str1.length()][str2.length()];
    }

    public static void main(String[] args) {
        int computeLevenshteinDistance = computeLevenshteinDistance("YWCQPGK", "LAWYQQKPGKA");
        System.out.println("c " + computeLevenshteinDistance);
    }

    private static void allAlignment(String x, int i, String y, int j, String z, int[][] T) {
//        System.out.println("i " + i + " j " + j);
        if (i > 0) {
            if (j > 0) {
                if (T[i][j] == T[i - 1][j - 1] + ((x.charAt(i - 1) == y.charAt(j - 1)) ? 0 : 1)) {
//                    z = x.charAt(i - 1) + ":" + y.charAt(j - 1) + "," + z;
//                    System.out.println("cresendo: " + z);
                    allAlignment(x, i - 1, y, j - 1, x.charAt(i - 1) + ":" + y.charAt(j - 1) + "," + z, T);
                }
//                System.out.println("Evaluando i: " + 1 + " j: " + j);
                if (T[i][j] == T[i - 1][j] + 1) {
//                    z = x.charAt(i - 1) + ":" + "-," + z;
//                    System.out.println("restando: " + z);
                    allAlignment(x, i - 1, y, j, x.charAt(i - 1) + ":" + "-," + z, T);
                }
                if (T[i][j] == T[i][j - 1] + 1) {
//                    z = "+" + ":" + y.charAt(j - 1) + "," + z;
                    allAlignment(x, i, y, j - 1, "+" + ":" + y.charAt(j - 1) + "," + z, T);
                }
            } else {
//                z = x.charAt(i - 1) + ":" + "-," + z;
                allAlignment(x, i - 1, y, 0, x.charAt(i - 1) + ":" + "-," + z, T);
            }
        } else {
            if (j > 0) {
//                z = "+" + ":" + y.charAt(j - 1) + "," + z;
                allAlignment(x, 0, y, j - 1, "+" + ":" + y.charAt(j - 1) + "," + z, T);
            } else {
                System.out.println("z: " + z);
            }
        }
    }

}
