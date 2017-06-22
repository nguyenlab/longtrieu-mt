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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class ReadUtils {

    public static List<String> read2list(File inFile) {

        List<String> outList = new ArrayList<>();

        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "utf-8"));

            String line;
            while ((line = reader.readLine()) != null) {

                outList.add(line);

            }

        } catch (IOException ex) {
            Logger.getLogger(ReadUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return outList;

    }

    
    public static Set<String> getVocab(String inFile) {

        Set<String> vocabSet = new HashSet<>();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "utf-8"));
            while ((line = reader.readLine()) != null) {
                for (String word : line.split(" ")) {
                    vocabSet.add(word);
                }
            }

            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(ReadUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return vocabSet;

    }

    public static void read(String inFile, String outFile) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile)), "utf-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outFile)), "utf-8"));

            String line;
            while ((line = reader.readLine()) != null) {
            }

            writer.close();
            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(ReadUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Set<String> read2set(String inFile) {

        Set<String> vocabSet = new HashSet<>();

        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "utf-8"));
            while ((line = reader.readLine()) != null) {
                vocabSet.add(line);
            }

            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(ReadUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return vocabSet;

    }

}
