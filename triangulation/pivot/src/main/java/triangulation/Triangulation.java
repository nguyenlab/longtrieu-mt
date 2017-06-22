/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Triangulation {

    public static void triangulation_full(String srcPhraseFile, String src_pvtTable, String pvt_trgTable, final String scoreDir, String outDir, String outFile, int threadNum) {

        try {
            //
            final ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
            //
            final Table<Integer, Integer, float[]> srcPvtScores = readScores_srcPvt(src_pvtTable);
            final Table<Integer, Integer, float[]> trgPvtScores = readScores_trgPvt(pvt_trgTable);
            final Table<Integer, Integer, int[]> srcPvtAlignment = Alignment.readAlignment_srcPvt(src_pvtTable);
            final Table<Integer, Integer, int[]> trgPvtAlignment = Alignment.readAlignment_trgPvt(pvt_trgTable);
            //
//            final List<Integer> srcPhrasesSet = utils.ReadUtils.read2_integerList(srcPhraseFile);
//            final Set<Integer> srcPhrasesSet = pretraining.Indexing.readIndex2Phrase(srcPhraseFile).keySet();
            final List<Integer> srcPhrasesSet = extractSrcIndex(srcPhraseFile);

            utils.FileUtils.newDir(scoreDir);

            //----------------------------------------------------------------------
            for (final int srcPhrase : srcPhrasesSet) {
                //----------------------------------------------------------------------                

                //====================================
                executorService.submit(new Runnable() {
                    //====================================                    
                    @Override
                    public void run() {
                        DataOutputStream scoresWriter = null;

                        try {
                            scoresWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(scoreDir, srcPhrase + ""))));
                            //----------------------------------------------------------------------                
                            for (int trgPhrase : trgPvtScores.rowKeySet()) {
                                //----------------------------------------------------------------------                                                
                                float[] srcTrgScores = new float[4];
//                                for (int i = 0; i < 4; i++) {
//                                    javiScores[i] = (float) 0;
//                                }
                                //
                                Set<String> srctrgAlignmentSet = new HashSet<>();
                                //
                                Set<Integer> PVTsrcSet = srcPvtScores.row(srcPhrase).keySet();
                                Set<Integer> PVTtrgSet = trgPvtScores.row(trgPhrase).keySet();
                                //
                                for (int pvtPhrase : PVTsrcSet) {
                                    if (PVTtrgSet.contains(pvtPhrase)) {
                                        //----------------------------------------------------------------------
                                        float[] srcpvtScores = srcPvtScores.get(srcPhrase, pvtPhrase);
                                        float[] trgpvtScores = trgPvtScores.get(trgPhrase, pvtPhrase);
                                        //
                                        for (int i = 0; i < 4; i++) {
                                            srcTrgScores[i] += srcpvtScores[i] * trgpvtScores[i];
                                        }
//                                        //----------------------------------------------------------------------                                        
                                        //No alignment induction here
                                        int[] srctrg_alignment = Alignment.alignmentInduction(srcPvtAlignment.get(srcPhrase, pvtPhrase), trgPvtAlignment.get(trgPhrase, pvtPhrase));
                                        for (int i = 0; i < srctrg_alignment.length; i += 2) {
                                            srctrgAlignmentSet.add(srctrg_alignment[i] + "-" + srctrg_alignment[i + 1]);
                                        }
                                        //----------------------------------------------------------------------                                                                                
                                    }
                                }
                                //----------------------------------------------------------------------
                                if ((srcTrgScores[0] * srcTrgScores[1] * srcTrgScores[2] * srcTrgScores[3]) > 0 && srctrgAlignmentSet.size() > 0) {
                                    scoresWriter.writeInt(trgPhrase);
                                    //
                                    for (int i = 0; i < 4; i++) {
                                        scoresWriter.writeFloat(srcTrgScores[i]);
                                    }
                                    //

                                    StringBuilder alignment = new StringBuilder();
                                    for (String srctrg : srctrgAlignmentSet) {
                                        alignment.append(srctrg).append(" ");
                                    }
                                    alignment.deleteCharAt(alignment.length() - 1);
//                                    binWriter.writeUTF("0-0");
                                    scoresWriter.writeUTF(alignment.toString());

                                }
                                //----------------------------------------------------------------------

                            }
                            //----------------------------------------------------------------------
                            //for all vi phrases
                        } catch (IOException ex) {
                            Logger.getLogger(Triangulation.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                scoresWriter.close();
                            } catch (IOException ex) {
                                Logger.getLogger(Triangulation.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
                //====================================                
            }
            //----------------------------------------------------------------------                            
            executorService.shutdown();

            try {
                executorService.awaitTermination(3, TimeUnit.DAYS);
            } catch (InterruptedException ex) {
                Logger.getLogger(Triangulation.class.getName()).log(Level.SEVERE, null, ex);

            }
            //----------------------------------------------------------------------                                        
            mergeSet(srcPhraseFile, scoreDir, outDir, outFile);
            //----------------------------------------------------------------------                          

        } catch (IOException ex) {
            Logger.getLogger(Triangulation.class.getName()).log(Level.SEVERE, null, ex);

        }
//        System.out.println("Triangulation: completed!");
//        System.out.println("Src phrase: "+srcPhraseFile);
//        System.out.println("");
        System.out.println("completed!");
        System.out.println("");

    }

    public static Table<Integer, Integer, float[]> readScores_srcPvt(String phraseTableInFile) throws IOException {
        Table<Integer, Integer, float[]> table = HashBasedTable.create();
        DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(phraseTableInFile)));
        //
        while (binReader.available() > 0) {
            int srcPhrase = binReader.readInt();
            int pvtPhrase = binReader.readInt();
            float[] scores = new float[4];
            for (int i = 0; i < 4; i++) {
                scores[i] = binReader.readFloat();
            }
            table.put(srcPhrase, pvtPhrase, scores);
            binReader.readUTF();
            binReader.readUTF();
        }
        binReader.close();
        //
        return table;
    }

    public static Table<Integer, Integer, float[]> readScores_trgPvt(String phraseTableInFile) throws IOException {
        Table<Integer, Integer, float[]> table = HashBasedTable.create();
        DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(phraseTableInFile)));
        //
        while (binReader.available() > 0) {
            int pvtPhrase = binReader.readInt();
            int trgPhrase = binReader.readInt();
            float[] scores = new float[4];
            for (int i = 0; i < 4; i++) {
                scores[i] = binReader.readFloat();
            }
            table.put(trgPhrase, pvtPhrase, scores);
            binReader.readUTF();
            binReader.readUTF();
        }
        binReader.close();
        //
        return table;
    }

    public static void mergeSet(String srcPhrasesFile, String scoreDir, String outDir, String outFile) throws IOException {

        utils.FileUtils.newDir(outDir);
        DataOutputStream binWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, outFile))));

//        List<Integer> srcList = utils.ReadUtils.read2_integerList(srcPhrasesFile);
        List<Integer> srcList = extractSrcIndex(srcPhrasesFile);

        for (int srcPhrase : srcList) {
//            System.out.print(jaPhrase + " ");
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(scoreDir, srcPhrase + ""))));
            while (binReader.available() > 0) {
                binWriter.writeInt(srcPhrase);
                binWriter.writeInt(binReader.readInt());
                //
                for (int i = 0; i < 4; i++) {
                    binWriter.writeFloat(binReader.readFloat());
                }
                //
                binWriter.writeUTF(binReader.readUTF());
            }
            binReader.close();
//            System.out.println("ok:" + jaPhrase);
        }

        binWriter.close();
//        System.out.println("Merged scores: completed");
//        System.out.println("Dir: "+scoreDir);
//        System.out.println("Src phrases: "+srcList.size());
//        System.out.println("");
    }

    public static List<Integer> extractSrcIndex(String srcIndexFile) {
        List<Integer> indexList = new ArrayList<>();
        Set<Integer> indexSet = triangulation.Indexing.readIndex2Phrase(srcIndexFile).keySet();
        TreeSet<Integer> sortedSet = new TreeSet<>(indexSet);
        for (int id : sortedSet) {
            indexList.add(id);
        }
        return indexList;
    }

}
