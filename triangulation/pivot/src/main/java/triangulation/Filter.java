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
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Filter {

    public static void filterPhraseTable_SrcPvt(String inTableFile, String srcFile, String outDir, String outTableFile, String pvtFile) {

        DataOutputStream writer = null;
        try {

            TreeSet<String> pvtSet = new TreeSet<>();
            utils.FileUtils.newDir(outDir);
            writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, outTableFile))));
            int nsaved = 0;

            Set<String> srcSet = utils.ReadUtils.read2set(srcFile);
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(inTableFile)));

            while (binReader.available() > 0) {

                String src = binReader.readUTF();
                String pvt = binReader.readUTF();

                if (srcSet.contains(src)) {

                    writer.writeUTF(src);
                    writer.writeUTF(pvt);
                    for (int i = 0; i < 4; i++) {
                        writer.writeFloat(binReader.readFloat());
                    }
                    writer.writeUTF(binReader.readUTF());
                    writer.writeUTF(binReader.readUTF());

                    nsaved++;
                    pvtSet.add(pvt);

                } else {
                    for (int i = 0; i < 4; i++) {
                        binReader.readFloat();
                    }
                    binReader.readUTF();
                    binReader.readUTF();
                }

            }

            binReader.close();

            BufferedWriter pvtWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, pvtFile)), "utf-8"));
            for (String pvt : pvtSet) {
                pvtWriter.write(pvt);
                pvtWriter.write("\n");
            }
            pvtWriter.close();

            System.out.println("Extract src-pvt phrase table via filtered src: completed!");
            System.out.println("Extracted phrase pairs: " + nsaved);
            System.out.println("Src phrases: " + srcSet.size());
            System.out.println("Pvt phrases: " + pvtSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void filterPhraseTable_PvtTrg(String inTableFile, String trgFile, String outDir, String outTableFile, String pvtFile) {

        DataOutputStream writer = null;
        try {

            TreeSet<String> pvtSet = new TreeSet<>();
            utils.FileUtils.newDir(outDir);
            writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, outTableFile))));
            int nsaved = 0;

            Set<String> trgSet = utils.ReadUtils.read2set(trgFile);
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(inTableFile)));

            while (binReader.available() > 0) {

                String pvt = binReader.readUTF();
                String trg = binReader.readUTF();

                if (trgSet.contains(trg)) {

                    writer.writeUTF(pvt);
                    writer.writeUTF(trg);
                    for (int i = 0; i < 4; i++) {
                        writer.writeFloat(binReader.readFloat());
                    }
                    writer.writeUTF(binReader.readUTF());
                    writer.writeUTF(binReader.readUTF());

                    nsaved++;
                    pvtSet.add(pvt);

                } else {
                    for (int i = 0; i < 4; i++) {
                        binReader.readFloat();
                    }
                    binReader.readUTF();
                    binReader.readUTF();
                }

            }

            binReader.close();

            BufferedWriter pvtWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, pvtFile)), "utf-8"));
            for (String pvt : pvtSet) {
                pvtWriter.write(pvt);
                pvtWriter.write("\n");
            }
            pvtWriter.close();

            System.out.println("Extract pvt-trg phrase table via filtered src: completed!");
            System.out.println("Extracted phrase pairs: " + nsaved);
            System.out.println("Pvt phrases: " + pvtSet.size());
            System.out.println("Trg phrases: " + trgSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void extractOverlapPhrases(String phraseFile, String testFile, String outDir, String extractedPhraseFile) {

        BufferedWriter writer = null;
        try {
            utils.FileUtils.newDir(outDir);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, extractedPhraseFile)), "utf-8"));

            List<String> phraseList = utils.ReadUtils.read2list(phraseFile);
            List<String> testList = utils.ReadUtils.read2list(testFile);

            int nphrase = 0;
            for (String phrase : phraseList) {
                String phrase2 = " " + phrase + " ";
                for (String testSent : testList) {
                    String testSent2 = " " + testSent + " ";
                    if (testSent2.contains(phrase2)) {
                        writer.write(phrase);
                        writer.write("\n");
                        nphrase++;
                        break;
                    }
                }
            }

            System.out.println("Extracted phrases by test data: completed!");
            System.out.println("Test set: " + testFile);
            System.out.println("Phrases: " + phraseFile);
            System.out.println("Phrases input: " + phraseList.size());
            System.out.println("Phrases extracted: " + nphrase);
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void normalizeFilteredScores(String workDir, String srcPhrasesFile, String trgPhrasesFile, String scoredFile, String normalizedFile) {
        try {
            Map<Integer, Float> jaTotalPhraseScoreMap = new HashMap<>();
            Map<Integer, Float> jaTotalLexScoreMap = new HashMap<>();
            Map<Integer, Float> viTotalPhraseScoreMap = new HashMap<>();
            Map<Integer, Float> viTotalLexScoreMap = new HashMap<>();
            //
//            List<Integer> srcList = utils.ReadUtils.read2_integerList(new File(workDir, srcPhrasesFile));
//            List<Integer> trgList = utils.ReadUtils.read2_integerList(new File(workDir, trgPhrasesFile));
            List<Integer> srcList = triangulation.Triangulation.extractSrcIndex(srcPhrasesFile);
            List<Integer> trgList = triangulation.Triangulation.extractSrcIndex(trgPhrasesFile);

            //
            for (int src : srcList) {
                jaTotalPhraseScoreMap.put(src, (float) 0);
                jaTotalLexScoreMap.put(src, (float) 0);
            }   //
            for (int trg : trgList) {
                viTotalPhraseScoreMap.put(trg, (float) 0);
                viTotalLexScoreMap.put(trg, (float) 0);
            }   //
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(workDir, scoredFile))));
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
                binReader.readUTF();//added this line compared with the normalize in post train     

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
                binWriter.writeUTF(binReader.readUTF());//added this line compared with the normalize in post train

            }   //
            binWriter.close();
            binReader.close();

//            System.out.println("Normalize scores: completed!");
//            System.out.println("File: " + scoredFile);
//            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Filter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
