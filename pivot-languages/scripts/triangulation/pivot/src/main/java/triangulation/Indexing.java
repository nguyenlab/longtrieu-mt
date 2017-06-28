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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Indexing {

    public static void similarityScores(String inFile, String indexFile, String outDir, String outFile) {

        try {
            Map<String, Integer> indexMap = readPhrase2Index(indexFile);

            utils.FileUtils.newDir(outDir);
            DataOutputStream binWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, outFile))));

            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(inFile)));

            int npairs = 0;
            while (binReader.available() > 0) {
                binWriter.writeInt(indexMap.get(binReader.readUTF()));
                binWriter.writeInt(indexMap.get(binReader.readUTF()));
//                binWriter.writeFloat((float) binReader.readDouble());
                binWriter.writeFloat(binReader.readFloat());
                npairs++;
            }

            binReader.close();
            binWriter.close();
            System.out.println("Indexing similarity file: completed!");
            System.out.println("File: " + inFile);
            System.out.println("Pairs: " + npairs);
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Indexing.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void phraseTableIndex(String phraseTableInFile, String srcIndexInFile, String trgIndexInFile, String outDir, String phraseTableIndexOutFile) {

        try {
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(phraseTableInFile)));
            Map<String, Integer> srcIndexMap = readPhrase2Index(srcIndexInFile);
            Map<String, Integer> trgIndexMap = readPhrase2Index(trgIndexInFile);

            utils.FileUtils.newDir(outDir);
            DataOutputStream binWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, phraseTableIndexOutFile))));
            int npairs = 0;

            while (binReader.available() > 0) {

                binWriter.writeInt(srcIndexMap.get(binReader.readUTF()));
                binWriter.writeInt(trgIndexMap.get(binReader.readUTF()));

                for (int i = 0; i < 4; i++) {
                    binWriter.writeFloat(binReader.readFloat());
                }

                binWriter.writeUTF(binReader.readUTF());
                binWriter.writeUTF(binReader.readUTF());

                npairs++;

            }

            binReader.close();

//            System.out.println("Indexing phrase-table: completed");
//            System.out.println("File: " + phraseTableInFile);
//            System.out.println("Phrase pairs: " + npairs);
//            System.out.println("Src phrases: " + srcIndexMap.size());
//            System.out.println("Trg phrases: " + trgIndexMap.size());
//            System.out.println("");
            binWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Indexing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void phrasesIndex(String phrasesInFile, String outDir, String indexOutFile, String indexBinOutFile) {

        try {
            utils.FileUtils.newDir(outDir);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, indexOutFile)), "utf-8"));
            DataOutputStream binWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, indexBinOutFile))));

            List<String> phrasesList = utils.ReadUtils.read2list(phrasesInFile);

            for (int id = 0; id < phrasesList.size(); id++) {
                String phrase = phrasesList.get(id);
                writer.write(String.format("%d %s\n", id, phrase));
                binWriter.writeInt(id);
                binWriter.writeUTF(phrase);
            }

            binWriter.close();
//            System.out.println("Indexing phrases: completed");
//            System.out.println("File: " + phrasesInFile);
//            System.out.println("Phrases: " + phrasesList.size());
//            System.out.println("");

            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Indexing.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Map<Integer, String> readIndex2Phrase(String indexInputFile) {
        Map<Integer, String> indexMap = new HashMap<>();

        try {

            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(indexInputFile)));
            while (binReader.available() > 0) {
                int key = binReader.readInt();
                String phrase = binReader.readUTF();
                indexMap.put(key, phrase);
            }

            binReader.close();

        } catch (IOException ex) {
            Logger.getLogger(Indexing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return indexMap;

    }

    public static Map<String, Integer> readPhrase2Index(String indexInputFile) {
        Map<String, Integer> indexMap = new HashMap<>();

        try {

            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(indexInputFile)));
            while (binReader.available() > 0) {
                int key = binReader.readInt();
                String phrase = binReader.readUTF();
                indexMap.put(phrase, key);
            }
            binReader.close();

        } catch (IOException ex) {
            Logger.getLogger(Indexing.class.getName()).log(Level.SEVERE, null, ex);

        }

        return indexMap;

    }

}
