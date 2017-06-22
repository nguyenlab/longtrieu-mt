/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static Set<String> read2set(String inFile) {
        Set<String> set = new HashSet<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile)), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                set.add(line);
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

        return set;
    }
    
    public static List<String> read2list(String inFile) {
        List<String>list=new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inFile)), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
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

        return list;
    }
    
    public static List<Integer> read2_integerList(File inFile) {
        List<Integer>list=new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Integer.parseInt(line));
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

        return list;
    }

}
