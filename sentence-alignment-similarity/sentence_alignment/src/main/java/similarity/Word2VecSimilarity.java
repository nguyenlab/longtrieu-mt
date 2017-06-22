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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Word2VecSimilarity {

    public static void filterIBM1_Other(String ibm1File, String outDir, String ibm1OutFile) {
        try {

            utils.FileUtils.newDir(outDir);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, ibm1OutFile)), "utf-8"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(ibm1File)), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] words = line.split(" ");
                String src = words[1];
                String trg = words[2];
                float score = Float.parseFloat(words[0]);

                if (!src.contains("(other)") && !trg.contains("(other)")) {//remove (other)
                    writer.write(String.format("%s %s %s\n", score, src, trg));
                }
            }
            writer.close();

            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(Word2VecSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void extendIBM1Similarity(String ibm1File, String ibm1NbestFile, String srcSimilarityFile, String trgSimilarityFile, String outDir, String ibm1OutFile) {

        Table<String, String, Float> ibm1Table = similarity.NbestIBM1.readScoresIBM1(ibm1File);
        Table<String, String, Float> ibm1NbestTable = similarity.NbestIBM1.readScoresIBM1(ibm1NbestFile);

        Table<String, String, Float> srcTable = readSimilarityScores(srcSimilarityFile);
        Table<String, String, Float> trgTable = readSimilarityScores(trgSimilarityFile);

        int oldsize = ibm1Table.size();
        int newpairs = 0;
        int newsrc = 0;
        int newtrg = 0;
        int newsrctrg = 0;
        for (String src : ibm1NbestTable.rowKeySet()) {
            Set<String> srcSimSet = new HashSet<>();
            if (srcTable.rowKeySet().contains(src)) {
                srcSimSet = srcTable.row(src).keySet();
                for (String src_sim : srcSimSet) {
                    float sim_score = srcTable.get(src, src_sim);
                    for (String trg : ibm1NbestTable.row(src).keySet()) {
                        ibm1Table.put(src_sim, trg, sim_score * ibm1Table.get(src, trg));
                        newsrc++;
                        newpairs++;
                    }
                }
            }
            for (String trg : ibm1NbestTable.row(src).keySet()) {
                float score = ibm1Table.get(src, trg);
                if (trgTable.rowKeySet().contains(trg)) {
                    for (String trg_sim : trgTable.row(trg).keySet()) {
                        ibm1Table.put(src, trg_sim, score * trgTable.get(trg, trg_sim));

                        newtrg++;
                        newpairs++;
                    }
                    if (srcSimSet.size() > 0) {
                        for (String src_sim : srcSimSet) {
                            float sim_score = srcTable.get(src, src_sim);
                            for (String trg_sim : trgTable.row(trg).keySet()) {
                                ibm1Table.put(src_sim, trg_sim, score * trgTable.get(trg, trg_sim) * sim_score);
                                newsrctrg++;
                                newpairs++;
                            }
                        }
                    }
                }
            }
        }

        Table<String, String, Float> ibm1NormTable = similarity.Word2VecSimilarity.normalizeSimilarityScores(ibm1Table);

        writeIBM1Scores(ibm1NormTable, outDir, ibm1OutFile);

        System.out.println("Enrich IBM1 similarity: completed!");

    }

    public static void writeIBM1Scores(Table<String, String, Float> inTable, String outDir, String outFile) {

        try {
            utils.FileUtils.newDir(outDir);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, outFile)), "utf-8"));

            for (String src : inTable.rowKeySet()) {
                for (String trg : inTable.row(src).keySet()) {
                    writer.write(String.format("%f %s %s\n", inTable.get(src, trg), src, trg));
                }
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Word2VecSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Table<String, String, Float> normalizeSimilarityScores(Table<String, String, Float> inTable) {

        Table<String, String, Float> normTable = HashBasedTable.create();

        for (String word0 : inTable.rowKeySet()) {

            Map<String, Float> simMap = inTable.row(word0);
            float total = 0;
            for (String sim : simMap.keySet()) {
                total += simMap.get(sim);
            }
            if (total == 0) {
                total = 1;
            }

            for (String sim : simMap.keySet()) {
                normTable.put(word0, sim, simMap.get(sim) / total);
            }
        }

        return normTable;

    }

    public static Table<String, String, Float> readSimilarityScores(String ibm1File) {

        Table<String, String, Float> scoresTable = HashBasedTable.create();
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(ibm1File)), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] words = line.split(" ");
                String src = words[0];
                String trg = words[1];
                float score = Float.parseFloat(words[2]);

                scoresTable.put(src, trg, score);

            }

            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(Word2VecSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scoresTable;
    }

    public static void extractNewAlignedName(String baselineDir, String simDir, String outDir, String outSimFile) {

        try {
            Set<String> baseSet = new HashSet<>();
            Set<String> simSet = new HashSet<>();

            for (File file : new File(baselineDir).listFiles()) {
                String fileName = file.getName().split("_")[0];
                baseSet.add(fileName);
            }
            for (File file : new File(simDir).listFiles()) {
                String fileName = file.getName().split("_")[0];
                simSet.add(fileName);
            }
            int nsim = 0;
            utils.FileUtils.newDir(outDir);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, outSimFile)), "utf-8"));

            TreeSet<Integer> newSimSet = new TreeSet<>();
            for (String sim : simSet) {
                if (!baseSet.contains(sim)) {
//                    writer.write(sim);
//                    writer.write("\n");
                    newSimSet.add(Integer.parseInt(sim));
                    nsim++;
                }
            }

            for (int sim : newSimSet) {
                writer.write(String.format("%d\n", sim));
            }
            writer.close();

            System.out.println("Extract new aligned file pairs: completed!");
            System.out.println("Baseline file pairs: " + baseSet.size());
            System.out.println("Similarity file pairs: " + simSet.size());
            System.out.println("New file pairs: " + nsim);
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Word2VecSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void extractNewAlignedPairs(String srcBaselineFile, String trgBaselineFile, String srcSimFile, String trgSimFile,
            String outDir, String srcBaselineOutFile, String trgBaselineOutFile, String srcSimOutFile, String trgSimOutFile
    ) {

        try {
            List<String> srcBaseList = utils.ReadUtils.read2list(new File(srcBaselineFile));
            List<String> srcSimList = utils.ReadUtils.read2list(new File(srcSimFile));
            List<String> trgBaseList = utils.ReadUtils.read2list(new File(trgBaselineFile));
            List<String> trgSimList = utils.ReadUtils.read2list(new File(trgSimFile));

            System.out.println("baseline sent: " + srcBaseList.size());
            System.out.println("similarity sents: " + srcSimList.size());

            utils.FileUtils.newDir(outDir);
            BufferedWriter srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, srcBaselineOutFile)), "utf-8"));
            BufferedWriter trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, trgBaselineOutFile)), "utf-8"));

            int count = 0;
            for (int id = 0; id < srcBaseList.size(); id++) {
                String src = srcBaseList.get(id);
                if (!srcSimList.contains(src)) {
                    srcWriter.write(src);
                    srcWriter.write("\n");
                    trgWriter.write(trgBaseList.get(id));
                    trgWriter.write("\n");
                    count++;
                }

            }

            srcWriter.close();
            trgWriter.close();
            System.out.println("sents in baseline but not in similarity: " + count);

            srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, srcSimOutFile)), "utf-8"));
            trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, trgSimOutFile)), "utf-8"));

            count = 0;
            for (int id = 0; id < srcSimList.size(); id++) {
                String src = srcSimList.get(id);
                if (!srcBaseList.contains(src)) {
                    srcWriter.write(src);
                    srcWriter.write("\n");
                    trgWriter.write(trgSimList.get(id));
                    trgWriter.write("\n");
                    count++;
                }

            }

            srcWriter.close();
            trgWriter.close();

            System.out.println("sents in similarity but not in baseline: " + count);

        } catch (IOException ex) {
            Logger.getLogger(Word2VecSimilarity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
