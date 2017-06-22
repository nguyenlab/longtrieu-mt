/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package similarity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class NbestIBM1 {

    final static String PUNCTUATION = "[\\p{Punct}\\d]+";
    final static String LETTERS = "[a-zA-Z]";

    public static void nbestScoresIBM1(String ibm1File, int NBEST, double threshold, String outDir, String ibm1OutFile, String srcFile, String trgFile) {

        try {

            utils.FileUtils.newDir(outDir);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, ibm1OutFile)), "utf-8"));
            Set<String> srcSet = new HashSet<>();
            Set<String> trgSet = new HashSet<>();

            Table<String, String, Float> scoresTable = readScoresIBM1(ibm1File);
            int nbest_pairs = 0;

            for (String src : scoresTable.rowKeySet()) {
                Map<String, Float> trgMap = scoresTable.row(src);

                List<Object[]> sortList = new ArrayList<>();

                for (String trg : trgMap.keySet()) {
                    sortList.add(new Object[]{trg, trgMap.get(trg)});
                }

                Collections.sort(sortList, new Comparator<Object[]>() {
                    @Override
                    public int compare(Object[] o1, Object[] o2) {
                        return -Float.compare((float) o1[1], (float) o2[1]);
                    }
                });

                int count = 0;
                for (Object[] obj : sortList) {//nbest
                    String trg = (String) obj[0];
                    float score = (Float) obj[1];

                    if (!src.trim().isEmpty() && !trg.trim().isEmpty()) {//empty
                        if (score >= threshold) {//threshold
                            if (puncFilter(src) && puncFilter(trg)) {//punctuation
                                if (!src.contains(trg) && !trg.contains(src)) {//maching
                                    if (letterFilter(src) && letterFilter(trg)) {//letters
                                        if (!src.contains("(other)") && !trg.contains("(other)")) {//remove (other)
                                            writer.write(String.format("%s %s %s\n", score, src, trg));
                                            trgSet.add(trg);
                                            srcSet.add(src);
                                            nbest_pairs++;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    count++;
                    if (count == NBEST) {
                        break;
                    }
                }

            }
            writer.close();

            BufferedWriter srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, srcFile)), "utf-8"));
            for (String src : srcSet) {
                srcWriter.write(src);
                srcWriter.write("\n");
            }
            srcWriter.close();

            BufferedWriter trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, trgFile)), "utf-8"));
            for (String trg : trgSet) {
                trgWriter.write(trg);
                trgWriter.write("\n");
            }
            trgWriter.close();

            System.out.println("Extract n-best alignment pairs: completed");
            System.out.println("Nbest=" + NBEST);
            System.out.println("Extracted pairs " + nbest_pairs);
            System.out.println("Src: " + srcSet.size());
            System.out.println("Trg: " + trgSet.size());

        } catch (IOException ex) {
            Logger.getLogger(NbestIBM1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static boolean puncFilter(String word) {
        if (word.matches(PUNCTUATION)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean letterFilter(String word) {
        if (word.length() == 1 && word.matches(LETTERS)) {
            return false;
        } else {
            return true;
        }
    }

    public static Table<String, String, Float> readScoresIBM1(String ibm1File) {

        Table<String, String, Float> scoresTable = HashBasedTable.create();
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(ibm1File)), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                String[] words = line.trim().split(" ");
                if (words.length == 3) {
                    float score = Float.parseFloat(words[0]);
                    String src = words[1];
                    String trg = words[2];

                    scoresTable.put(src, trg, score);
                } else {
                    System.out.println(words.length + " " + line);
                }
            }

            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(NbestIBM1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scoresTable;
    }

    public static Table<String, String, Float> readScoresIBM1_inverse(Table<String, String, Float> scoresTable) {

        Table<String, String, Float> inverseScoresTable = HashBasedTable.create();

        for (String src : scoresTable.rowKeySet()) {
            for (String trg : scoresTable.row(src).keySet()) {
                inverseScoresTable.put(trg, src, scoresTable.get(src, trg));
            }
        }

        return inverseScoresTable;
    }

    public static void readIBM1(String ibm1File) {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(ibm1File)), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] words = line.split(" ");
                float score = Float.parseFloat(words[0]);
                String src = words[1];
                String trg = words[2];
                System.out.println(score + " " + src + " " + trg);

            }

            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(NbestIBM1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
