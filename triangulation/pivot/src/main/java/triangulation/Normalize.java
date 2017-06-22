/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Normalize {

    public static void normalize(String workDir, String srcPhrase, String trgPhrase, String scoredTable, String normedFile) {

//        extractScoredPhrases(workDir, scoredTable, srcPhrase, trgPhrase);
        normalizeScores(workDir, srcPhrase, trgPhrase, scoredTable, normedFile);

    }

    public static void normalizeScores(String workDir, String srcPhrasesFile, String trgPhrasesFile, String scoredFile, String normalizedFile) {

        try {
            Map<Integer, Float> jaTotalPhraseScoreMap = new HashMap<>();
            Map<Integer, Float> jaTotalLexScoreMap = new HashMap<>();
            Map<Integer, Float> viTotalPhraseScoreMap = new HashMap<>();
            Map<Integer, Float> viTotalLexScoreMap = new HashMap<>();
            //
            List<Integer> srcList = utils.ReadUtils.read2_integerList(new File(srcPhrasesFile));
            List<Integer> trgList = utils.ReadUtils.read2_integerList(new File(trgPhrasesFile));
            //
            for (int src : srcList) {
                jaTotalPhraseScoreMap.put(src, (float) 0);
                jaTotalLexScoreMap.put(src, (float) 0);
            }   //
            for (int trg : trgList) {
                viTotalPhraseScoreMap.put(trg, (float) 0);
                viTotalLexScoreMap.put(trg, (float) 0);
            }   //
            DataInputStream binReader = null;
            binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(workDir, scoredFile))));
            //
            while (binReader.available() > 0) {
                int jaPhrase = binReader.readInt();
                int viPhrase = binReader.readInt();
                //
                viTotalPhraseScoreMap.put(viPhrase, binReader.readFloat() + viTotalPhraseScoreMap.get(viPhrase));//vija phrase score
                viTotalLexScoreMap.put(viPhrase, binReader.readFloat() + viTotalLexScoreMap.get(viPhrase));//vija lex score
                jaTotalPhraseScoreMap.put(jaPhrase, binReader.readFloat() + jaTotalPhraseScoreMap.get(jaPhrase));//javi phrase score
                jaTotalLexScoreMap.put(jaPhrase, binReader.readFloat() + jaTotalLexScoreMap.get(jaPhrase));//javi lex score

                //
                binReader.readUTF();
            }   //
            binReader.close();
            //------------------------------------------------------
            binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(workDir, scoredFile))));
            DataOutputStream binWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(workDir, normalizedFile))));
            //
            while (binReader.available() > 0) {
                int jaPhrase = binReader.readInt();
                int viPhrase = binReader.readInt();
                binWriter.writeInt(jaPhrase);
                binWriter.writeInt(viPhrase);
                //
                float vijaTotalPhraseScore = viTotalPhraseScoreMap.get(viPhrase);
                float vijaTotalLexScore = viTotalLexScoreMap.get(viPhrase);
                float javiTotalPhraseScore = jaTotalPhraseScoreMap.get(jaPhrase);
                float javiTotalLexScore = jaTotalLexScoreMap.get(jaPhrase);
                //
                if (vijaTotalPhraseScore > 0) {
                    binWriter.writeFloat(binReader.readFloat() / vijaTotalPhraseScore);
                } else {
                    binWriter.writeFloat(binReader.readFloat());
                }
                //
                if (vijaTotalLexScore > 0) {
                    binWriter.writeFloat(binReader.readFloat() / vijaTotalLexScore);
                } else {
                    binWriter.writeFloat(binReader.readFloat());
                }
                //
                if (javiTotalPhraseScore > 0) {
                    binWriter.writeFloat(binReader.readFloat() / javiTotalPhraseScore);
                } else {
                    binWriter.writeFloat(binReader.readFloat());
                }
                //
                if (javiTotalLexScore > 0) {
                    binWriter.writeFloat(binReader.readFloat() / javiTotalLexScore);
                } else {
                    binWriter.writeFloat(binReader.readFloat());
                }
                //
                binWriter.writeUTF(binReader.readUTF());
            }   //
            binWriter.close();
            binReader.close();

//            System.out.println("Normalize scores: completed!");
//            System.out.println("File: " + scoredFile);
//            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Normalize.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void extractScoredPhrases(String workDir, String scoredTable, String srcPhrasesFile, String trgPhrasesFile) {

        DataInputStream binReader = null;
        try {

            utils.FileUtils.newDir(workDir);
            BufferedWriter srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(workDir, srcPhrasesFile)), "utf-8"));
            BufferedWriter trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(workDir, trgPhrasesFile)), "utf-8"));

            TreeSet<Integer> srcSet = new TreeSet<>();
            TreeSet<Integer> trgSet = new TreeSet<>();

            binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(workDir, scoredTable))));

            while (binReader.available() > 0) {
                srcSet.add(binReader.readInt());
                trgSet.add(binReader.readInt());
                for (int i = 0; i < 4; i++) {
                    binReader.readFloat();
                }
                binReader.readUTF();
            }

            for (int src : srcSet) {
                srcWriter.write(String.format("%d\n", src));
            }

            for (int trg : trgSet) {
                trgWriter.write(String.format("%d\n", trg));
            }

            srcWriter.close();
            trgWriter.close();

            System.out.println("Extract scored phrases: completed!");
            System.out.println("Src phrases: " + srcSet.size());
            System.out.println("Trg phrases:  " + trgSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Normalize.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                binReader.close();
            } catch (IOException ex) {
                Logger.getLogger(Normalize.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
