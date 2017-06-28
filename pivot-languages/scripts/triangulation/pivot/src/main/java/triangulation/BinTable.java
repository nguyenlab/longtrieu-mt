/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class BinTable {

    public static final String PHRASE_TABLE_PATTERN = "[|]{3}";

    public static void extractBinPhraseTable(String phraseTableInputFile, String outDir, String srcPhrasesOutputFile, String trgPhrasesOutputFile,
            String phraseTableBinOutputFile) {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(phraseTableInputFile)), "utf-8"));
            Set<String> srcSet = new HashSet<>();
            Set<String> trgSet = new HashSet<>();

            utils.FileUtils.newDir(outDir);

            BufferedWriter srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, srcPhrasesOutputFile)), "utf-8"));
            BufferedWriter trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, trgPhrasesOutputFile)), "utf-8"));
            DataOutputStream binWriter = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, phraseTableBinOutputFile))));

            String line;
            int nline = 0;
            while ((line = reader.readLine()) != null) {
                String[] st = line.split(PHRASE_TABLE_PATTERN);
                String srcPhrase = st[0].trim();
                String trgPhrase = st[1].trim();
                if (!srcPhrase.isEmpty() && !srcSet.contains(srcPhrase)) {
                    srcSet.add(srcPhrase);
                    srcWriter.write(srcPhrase);
                    srcWriter.write(System.lineSeparator());
                }
                if (!trgPhrase.isEmpty() && !trgSet.contains(trgPhrase)) {
                    trgSet.add(trgPhrase);
                    trgWriter.write(trgPhrase);
                    trgWriter.write(System.lineSeparator());
                }
                binWriter.writeUTF(srcPhrase);
                binWriter.writeUTF(trgPhrase);
                String[] scores = st[2].trim().split(" ");
                for (int i = 0; i < 4; i++) {
                    binWriter.writeFloat(Float.parseFloat(scores[i]));
                }
                binWriter.writeUTF(st[3]);
                binWriter.writeUTF(st[4]);
                nline++;
            }
            srcWriter.close();
            trgWriter.close();
            binWriter.close();
            reader.close();

//            System.out.println("Phrase table to binary: completed!");
            System.out.println("Input phrase-table: " + phraseTableInputFile);
            System.out.println("Phrase pairs: " + nline);
            System.out.println("Src phrases: " + srcSet.size());
            System.out.println("Trg phrases: " + trgSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(BinTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
