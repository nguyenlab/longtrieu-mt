/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Model {

    public static final String PHRASE_TABLE_PATTERN = "[|]{3}";

    public static void inducePhraseTable(String normFile, String srcIndexInFile, String trgIndexInFile, String outDir, String phraseTableOutFile) {

        try {
            new File(outDir).mkdirs();
            Map<Integer, String> srcMap = triangulation.Indexing.readIndex2Phrase(srcIndexInFile);
            Map<Integer, String> trgMap = triangulation.Indexing.readIndex2Phrase(trgIndexInFile);
            Set<String> srcSet = new HashSet<>();
            Set<String> trgSet = new HashSet<>();
            //
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(normFile)));
            //
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, phraseTableOutFile)), "utf-8"));
            int npairs = 0;
            while (binReader.available() > 0) {

                String srcPhrase = srcMap.get(binReader.readInt());
                String trgPhrase = trgMap.get(binReader.readInt());

                writer.write(String.format("%s ||| %s ||| ", srcPhrase, trgPhrase));
                //
                for (int i = 0; i < 4; i++) {
                    writer.write(String.format("%e ", binReader.readFloat()));
                    //%e for scores instead of %f (this will lead to some zero scores)
                }
                //
                writer.write(String.format("||| %s ||| 1 1 1 ||| |||\n", binReader.readUTF()));
                //

                npairs++;
                srcSet.add(srcPhrase);
                trgSet.add(trgPhrase);

            }

            writer.close();
            binReader.close();

//            System.out.println("------------------------------");
            System.out.println("Pivot model: completed");
            System.out.println("Phrase pairs: " + npairs);
            System.out.println("Src phrases: " + srcSet.size());
            System.out.println("Trg phrases: " + trgSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
