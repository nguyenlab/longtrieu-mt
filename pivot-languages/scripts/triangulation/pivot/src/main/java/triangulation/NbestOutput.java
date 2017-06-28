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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class NbestOutput {
    
    public static final String PHRASE_TABLE_PATTERN = "[|]{3}";
    final static String PUNCTUATION = "[\\p{Punct}\\d]+";
    
    public static void extractNbest(String inTableFile, int NBEST, String outDir, String outTableFile, String srcFile, String trgFile) {
        
        try {
            
            utils.FileUtils.newDir(outDir);
            DataOutputStream binWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, outTableFile))));
            
            Table<Integer, Integer, Integer> indexTables = sortScores(inTableFile, NBEST);
            
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(inTableFile))));
            
            int npairs = 0;
            int savedpairs = 0;
            
            TreeSet<Integer> srcSet = new TreeSet<>();
            TreeSet<Integer> trgSet = new TreeSet<>();
            
            while (binReader.available() > 0) {
                
                int srcPhrase = binReader.readInt();
                int trgPhrase = binReader.readInt();
                
                if (indexTables.contains(srcPhrase, trgPhrase)) {
                    
                    binWriter.writeInt(srcPhrase);
                    binWriter.writeInt(trgPhrase);
                    
                    for (int i = 0; i < 4; i++) {
                        binWriter.writeFloat(binReader.readFloat());
                    }
                    
                    binWriter.writeUTF(binReader.readUTF());
                    
                    srcSet.add(srcPhrase);
                    trgSet.add(trgPhrase);
                    savedpairs++;
                    
                } else {
                    for (int i = 0; i < 4; i++) {
                        binReader.readFloat();
                    }
                    binReader.readUTF();
                }
                
                npairs++;
            }
            binWriter.close();
            binReader.close();
            
            BufferedWriter srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, srcFile)), "utf-8"));
            for (int src : srcSet) {
                srcWriter.append(String.format("%d\n", src));
            }
            srcWriter.close();
            
            BufferedWriter trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, trgFile)), "utf-8"));
            for (int trg : trgSet) {
                trgWriter.append(String.format("%d\n", trg));
            }
            trgWriter.close();
            
//            System.out.println("Extract n-best output: completed");
            System.out.println("Extract n-best output");
            System.out.println("N=" + NBEST);
//            System.out.println("File: " + inTableFile);
            System.out.println("Input pairs: " + npairs);
            System.out.println("Extracted pairs: " + savedpairs);
            System.out.println("Src phrases: " + srcSet.size());
            System.out.println("Trg phrases: " + trgSet.size());
            System.out.println("");
            
        } catch (IOException ex) {
            Logger.getLogger(NbestOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static Table<Integer, Integer, Integer> sortScores(String inTableFile, int NBEST) {
        
        Table<Integer, Integer, Integer> indexTables = HashBasedTable.create();
        Table<Integer, Integer, Float> scoresTable = getScores(inTableFile);
        
        for (int srcPhrase : scoresTable.rowKeySet()) {
            
            List<Object[]> sortList = new ArrayList<>();
            Map<Integer, Float> trgMap = scoresTable.row(srcPhrase);
            
            for (int trgPhrase : trgMap.keySet()) {
                sortList.add(new Object[]{trgPhrase,trgMap.get(trgPhrase)});
            }
            
            Collections.sort(sortList, new Comparator<Object[]>() {
                @Override
                public int compare(Object[] o1, Object[] o2) {
                    return -Float.compare((float) o1[1], (float) o2[1]);
                }
            });
            
            int count = 0;
            for (Object[] obj : sortList) {
                indexTables.put(srcPhrase, (int) obj[0], 0);
                count++;
                if (count == NBEST) {
                    break;
                }
            }
            
            
        }
        
        return indexTables;
        
    }
    
    public static Table<Integer, Integer, Float> getScores(String inTableFile) {
        Table<Integer, Integer, Float> scoresTable = HashBasedTable.create();
        
        try {
            
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(inTableFile))));

//            int npairs = 0;
            while (binReader.available() > 0) {
                
                int srcPhrase = binReader.readInt();
                int trgPhrase = binReader.readInt();
                
                float scores = 1;
                
                for (int i = 0; i < 4; i++) {
                    scores *= binReader.readFloat();
                }
                scoresTable.put(srcPhrase, trgPhrase, scores);
                binReader.readUTF();

//                npairs++;
            }
            
            binReader.close();
            
        } catch (IOException ex) {
            Logger.getLogger(NbestOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scoresTable;
        
    }
    
    public static void extractNbestOutput(String inFile, final int nbest, String outDir, String outFile, String srcFile, String trgFile) {
        
        BufferedReader reader = null;
        try {
            
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile)), "utf-8"));
            Set<String> srcSet = new HashSet<>();
            Set<String> trgSet = new HashSet<>();
            utils.FileUtils.newDir(outDir);
            BufferedWriter tableWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, outFile)), "utf-8"));
            String line;
            long nline = 0;
            long nsaved = 0;
            //        Set<String> savedSet = new HashSet<>();
            String previousPhrase = "";
            List<Object[]> list = new ArrayList<>();
            Table<String, String, String> linesTable = HashBasedTable.create();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] st = line.split(PHRASE_TABLE_PATTERN);
                
                String srcPhrase = st[0].trim();
                String trgPhrase = st[1].trim();
                
                if (!srcPhrase.matches(PUNCTUATION) && !trgPhrase.matches(PUNCTUATION)) {
                    String[] stscores = st[2].trim().split(" ");

//            float[] scores = new float[4];
                    float scores = 1;
                    for (int i = 0; i < 4; i++) {
//                scores[i] = Float.parseFloat(stscores[i]);
                        scores *= Float.parseFloat(stscores[i]);
                    }
                    
                    if (!srcPhrase.contains(previousPhrase) && !previousPhrase.contains(srcPhrase)) {
//                list.add(new Object[]{trgPhrase, scores[0], scores[1], scores[2], scores[3], scores[0] * scores[1] * scores[2] * scores[3], ""});
                        list.add(new Object[]{trgPhrase, scores});
                        linesTable.put(srcPhrase, trgPhrase, line);
                        
                    } else {
                        
                        Collections.sort(list, new Comparator<Object[]>() {
                            @Override
                            public int compare(Object[] o1, Object[] o2) {
                                return -Float.compare((float) o1[1], (float) o2[1]);
                            }
                        });

                        //write here
                        int count = 0;
                        for (Object[] t : list) {
                            tableWriter.write(linesTable.get(srcPhrase, (String) t[0]));
                            tableWriter.write("\n");
                            srcSet.add(srcPhrase);
                            trgSet.add((String) t[0]);
                            
                            nsaved++;
                            
                            count++;
                            if (count == nbest) {
                                break;
                            }
                        }
                        // new list
                        list = new ArrayList<>();
                        list.add(new Object[]{trgPhrase, scores});
                        linesTable = HashBasedTable.create();
                        linesTable.put(srcPhrase, trgPhrase, line);
                        
                    }
                    previousPhrase = srcPhrase;
                    
                }
                nline++;
                
            }
            tableWriter.close();
            reader.close();
            BufferedWriter srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, srcFile)), "utf-8"));
            for (String srcPhrase : srcSet) {
                srcWriter.write(srcPhrase);
                srcWriter.write("\n");
            }
            srcWriter.close();
            //
            BufferedWriter trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, trgFile)), "utf-8"));
            for (String trgPhrase : trgSet) {
                trgWriter.write(trgPhrase);
                trgWriter.write("\n");
            }
            trgWriter.close();
            System.out.println("Extract nbest phrases: completed!");
            System.out.println("Nbest=" + nbest);
            System.out.println("Input pairs: " + nline);
            System.out.println("Saved pairs: " + nsaved);
            System.out.println("Src phrases: " + srcSet.size());
            System.out.println("Trg phrases: " + trgSet.size());
            
        } catch (IOException ex) {
            Logger.getLogger(NbestOutput.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(NbestOutput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
