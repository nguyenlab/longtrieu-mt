/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package similarity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Paraphrase {

    public static void extractIBM1Paraphrase(String ibm1File, String outDir, String outFile, String outTextFile) {

        try {
            Table<String, String, Float> scoresTable = similarity.NbestIBM1.readScoresIBM1(ibm1File);
            Table<String, String, Float> inverseScoresTable = similarity.NbestIBM1.readScoresIBM1_inverse(scoresTable);

            utils.FileUtils.newDir(outDir);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, outTextFile)), "utf-8"));
            DataOutputStream binWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, outFile))));

            int id = 0;
            for (String trg : inverseScoresTable.rowKeySet()) {
                Set<String> simSet = new HashSet<>();
                for (String src : inverseScoresTable.row(trg).keySet()) {
                    for (String src_trans : scoresTable.row(src).keySet()) {
                        simSet.add(src_trans);
                    }
                }
                simSet.remove(trg);
                if (simSet.size() > 0) {
                    writer.write(id + "\n");
                    writer.write(trg + "\n");
                    for (String trg_sim : simSet) {
                        writer.write(trg_sim + " ");
                        binWriter.writeUTF(trg);
                        binWriter.writeUTF(trg_sim);
                    }
                    writer.write("\n");
                    id++;
                }
            }
            writer.close();
            binWriter.close();
            System.out.println("Extract IBM1 paraphrase: completed!");
            System.out.println("Vocab: " + id);
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Paraphrase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void extractIBM1ParaphraseW2V(String paraPhraseFile, String similarityFile, String outDir, String outFile) {

        try {
            utils.FileUtils.newDir(outDir);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, outFile)), "utf-8"));

            Table<String, String, Integer> paraTable = readParaphrase(paraPhraseFile);

            Table<String, String, Float> similarityTable = similarity.Word2VecSimilarity.readSimilarityScores(similarityFile);

            Table<String, String, Float> extractedTable = HashBasedTable.create();

            int npairs = 0;
            for (String word : similarityTable.rowKeySet()) {
                for (String word_sim : similarityTable.row(word).keySet()) {
                    if (paraTable.contains(word, word_sim) || paraTable.contains(word_sim, word)) {
//                        writer.write(String.format("%s %s %f\n", word,word_sim,similarityTable.get(word, word_sim)));                        
                        extractedTable.put(word, word_sim, similarityTable.get(word, word_sim));
                        npairs++;
                    }
                }
            }

            Table<String, String, Float> normTable = similarity.Word2VecSimilarity.normalizeSimilarityScores(extractedTable);

            for (String word : normTable.rowKeySet()) {
                for (String word_sim : normTable.row(word).keySet()) {
                    writer.write(String.format("%s %s %f\n", word, word_sim, normTable.get(word, word_sim)));
                }
            }

            writer.close();
            System.out.println("Filter w2v by paraphrase: completed!");
            System.out.println("Input similarity pairs: " + similarityTable.size());
            System.out.println("Paraphrase pairs: " + paraTable.size());
            System.out.println("Extracted pairs: " + npairs);
            System.out.println("");
        } catch (IOException ex) {
            Logger.getLogger(Paraphrase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Table<String, String, Integer> readParaphrase(String inFile) {
        Table<String, String, Integer> paraTable = HashBasedTable.create();

        try {
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(inFile))));

            while (binReader.available() > 0) {

                paraTable.put(binReader.readUTF(), binReader.readUTF(), 0);
            }

            binReader.close();

        } catch (IOException ex) {
            Logger.getLogger(Paraphrase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paraTable;
    }
}
