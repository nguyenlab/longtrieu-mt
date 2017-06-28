/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Reordering {

    public static final String PHRASE_TABLE_PATTERN = "[|]{3}";

    public static void filterReorderingTable(String phraseTableFile, String reorderingTableFile, String outDir, String outPhraseTable, String outReorderingTable) {

        try {
            utils.FileUtils.newDir(outDir);

            BufferedWriter ptableWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, outPhraseTable)), "utf-8"));
            BufferedWriter reWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, outReorderingTable)), "utf-8"));

            Table<String, String, String> reorderingTable = readReordering(reorderingTableFile);

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(phraseTableFile)), "utf-8"));
            String line;
            int nlines = 0;
            int nsaved = 0;
            while ((line = reader.readLine()) != null) {

                nlines++;
                String[] st = line.split(PHRASE_TABLE_PATTERN);
                String srcPhrase = st[0].trim();
                String trgPhrase = st[1].trim();

                if (reorderingTable.contains(srcPhrase, trgPhrase)) {
                    nsaved++;
                    ptableWriter.write(line);
                    ptableWriter.write("\n");

                    reWriter.write(reorderingTable.get(srcPhrase, trgPhrase));
                    reWriter.write("\n");
                }

            }

            reader.close();
            ptableWriter.close();
            reWriter.close();

//            utils.Gzip.gzipIt(outDir, outPhraseTable, outPhraseTable + ".gz");
//            utils.Gzip.gzipIt(outDir, outReorderingTable, outReorderingTable + ".gz");
//            System.out.println("Filter reordering table: completed!");
//            System.out.println("Phrase table: " + nlines);
//            System.out.println("Reordering table: " + reorderingTable.size());
//            System.out.println("Filtered: " + nsaved);
            System.out.println("completed!");
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Reordering.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Table<String, String, String> readReordering(String reorderingTable) {
        Table<String, String, String> table = HashBasedTable.create();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(reorderingTable)), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] st = line.split(PHRASE_TABLE_PATTERN);
                String srcPhrase = st[0].trim();
                String trgPhrase = st[1].trim();
                table.put(srcPhrase, trgPhrase, line);
            }
            reader.close();

        } catch (IOException ex) {
            Logger.getLogger(Reordering.class.getName()).log(Level.SEVERE, null, ex);
        }
        return table;

    }

}
