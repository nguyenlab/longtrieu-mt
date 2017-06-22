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
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class CommonPivot {

    public static void commonPivotPhrases(String pivotFile1, String pivotFile2, String outDir, String commonPivotFile) {

        BufferedWriter writer = null;
        try {

            Set<String> set1 = utils.ReadUtils.read2set(pivotFile1);
            Set<String> set2 = utils.ReadUtils.read2set(pivotFile2);

            TreeSet<String> commonSet = new TreeSet<>();

            for (String pivot : set1) {
                if (set2.contains(pivot)) {
                    commonSet.add(pivot);
                }
            }

            utils.FileUtils.newDir(outDir);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, commonPivotFile)), "utf-8"));
            for (String pivot : commonSet) {
                writer.write(pivot);
                writer.write("\n");
            }

            System.out.println("Extract common pivot phrases: completed!");
            System.out.println("Phrases pivot1: " + set1.size());
            System.out.println("Phrases pivot2: " + set2.size());
            System.out.println("Common pivot phrases: " + commonSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(CommonPivot.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(CommonPivot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void extractPhraseTable_SrcPvt(String phraseTableInFile, String pivotFile,
            String outDir, String phraseTableOutFile, String srcFile) {

        DataOutputStream writer = null;
        try {

            TreeSet<String> srcSet = new TreeSet<>();
            utils.FileUtils.newDir(outDir);
            writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, phraseTableOutFile))));
            int nsaved = 0;

            Set<String> pivotSet = utils.ReadUtils.read2set(pivotFile);
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(phraseTableInFile)));

            while (binReader.available() > 0) {

                String src = binReader.readUTF();
                String pvt = binReader.readUTF();

                if (pivotSet.contains(pvt)) {
                    writer.writeUTF(src);
                    writer.writeUTF(pvt);
                    for (int i = 0; i < 4; i++) {
                        writer.writeFloat(binReader.readFloat());
                    }
                    writer.writeUTF(binReader.readUTF());
                    writer.writeUTF(binReader.readUTF());

                    nsaved++;
                    srcSet.add(src);

                } else {
                    for (int i = 0; i < 4; i++) {
                        binReader.readFloat();
                    }
                    binReader.readUTF();
                    binReader.readUTF();
                }

            }

            binReader.close();

            BufferedWriter srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, srcFile)), "utf-8"));

            for (String src : srcSet) {
                srcWriter.write(src);
                srcWriter.write("\n");
            }
            srcWriter.close();

            System.out.println("Extract src-pvt phrase table via common pivot: completed!");
            System.out.println("Extracted phrase pairs: " + nsaved);
            System.out.println("Src phrases: " + srcSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(CommonPivot.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(CommonPivot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void extractPhraseTable_PvtTrg(String phraseTableInFile, String pivotFile,
            String outDir, String phraseTableOutFile, String trgFile) {

        DataOutputStream writer = null;
        try {

            TreeSet<String> trgSet = new TreeSet<>();
            utils.FileUtils.newDir(outDir);
            writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outDir, phraseTableOutFile))));
            int nsaved = 0;

            Set<String> pivotSet = utils.ReadUtils.read2set(pivotFile);
            DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(phraseTableInFile)));

            while (binReader.available() > 0) {

                String pvt = binReader.readUTF();
                String trg = binReader.readUTF();

                if (pivotSet.contains(pvt)) {
                    writer.writeUTF(pvt);
                    writer.writeUTF(trg);
                    for (int i = 0; i < 4; i++) {
                        writer.writeFloat(binReader.readFloat());
                    }
                    writer.writeUTF(binReader.readUTF());
                    writer.writeUTF(binReader.readUTF());

                    nsaved++;
                    trgSet.add(trg);

                }
                else {
                    for (int i = 0; i < 4; i++) {
                        binReader.readFloat();
                    }
                    binReader.readUTF();
                    binReader.readUTF();
                }

            }

            binReader.close();

            BufferedWriter trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, trgFile)), "utf-8"));

            for (String trg : trgSet) {
                trgWriter.write(trg);
                trgWriter.write("\n");
            }
            trgWriter.close();

            System.out.println("Extract pvt-trg phrase table via common pivot: completed!");
            System.out.println("Extracted phrase pairs: " + nsaved);
            System.out.println("Trg phrases: " + trgSet.size());
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(CommonPivot.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(CommonPivot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
