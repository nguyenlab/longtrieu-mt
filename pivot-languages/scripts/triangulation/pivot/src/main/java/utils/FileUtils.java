/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.regex.Pattern;

/**
 *
 * @author Long
 */
public class FileUtils {

    final static String ALPHABET = "[a-zA-Z]+.*";

    public static void newDir(String dirName) {
        if (!new File(dirName).exists()) {
            new File(dirName).mkdirs();
        }
    }

    public static void extractVocab(String inFile, String outDir, String outFile) {

        try {

            utils.FileUtils.newDir(outDir);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, outFile)), "utf-8"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile)), "utf-8"));

            Set<String> vocabSet = new HashSet<>();

            String line;
            while ((line = reader.readLine()) != null) {

                String[] words = line.trim().split(" ");
                for (String word : words) {
                    if (Pattern.matches(ALPHABET, word)) {
                        if (!vocabSet.contains(word)) {
                            vocabSet.add(word);
                            writer.write(word);
                            writer.write("\n");
                        }
                    }
                }

            }

            writer.close();

            reader.close();

            System.out.println("Extract voab: completed!");
            System.out.println("Input file: " + inFile);
            System.out.println("Vocab: " + vocabSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
