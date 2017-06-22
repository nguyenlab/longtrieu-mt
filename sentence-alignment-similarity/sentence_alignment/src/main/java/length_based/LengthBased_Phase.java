/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package length_based;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class LengthBased_Phase {

    public static void length_based_dir(String inDir, String srclang, String trglang, int limit, double threshold, String outDir, String mergeDir, String srcFile, String trgFile) {

        System.out.println("LENGTH BASED PHASE:");
        System.out.println("");

        if (!new File(outDir).exists()) {
            new File(outDir).mkdirs();
        }

        List<String> fileNameSet = utils.FileUtils.getFileNameList(inDir);

        for (String prefixName : fileNameSet) {

            String srcInFile = inDir + "/" + String.format("%s_%s.snt", prefixName, srclang);
            String trgInFile = inDir + "/" + String.format("%s_%s.snt", prefixName, trglang);
            String nodeFile = outDir + "/" + prefixName + ".node";
            String srcOutFile = outDir + "/" + String.format("%s_%s.length-based", prefixName, srclang);
            String trgOutFile = outDir + "/" + String.format("%s_%s.length-based", prefixName, trglang);

            length_based(srcInFile, trgInFile, limit, threshold, nodeFile, srcOutFile, trgOutFile);

        }

        mergeLengthBasedFiles(inDir, outDir, srclang, trglang, mergeDir, srcFile, trgFile);

    }

    public static void length_based(
            String srcInFile,
            String trgInFile,
            int limit,
            double threshold,
            String nodeOutFile,
            String srcOutFile,
            String trgOutFile
    ) {

        LengthBased c = new LengthBased();
        IOData a1 = new IOData();
        IOData a2 = new IOData();
        SentLengthRatio b1 = new SentLengthRatio();
        a1.SetIODataComplex(srcInFile);
        a2.SetIODataComplex(trgInFile);

        b1.ComputeSentLengthRatio(a1, a2);

        c.Init(a1, a2, b1);
        c.Forward_Backward(limit, threshold);

        c.writeNode(nodeOutFile);

        writeLengthBased(c.getBead1(), c.getBead2(), srcInFile, trgInFile, srcOutFile, trgOutFile);

    }

    public static void writeLengthBased(
            Map<Integer, Double> bead1, Map<Integer, Double> bead2, String srcInFile, String trgInFile, String srcOutFile, String trgOutFile) {

        try {
            int ns1 = 0;

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcInFile)), "utf-8"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(srcOutFile)), "utf-8"));

            String line;

            while ((line = reader.readLine()) != null) {

                if (bead1.containsKey(ns1)) {
                    writer.write(line);
                    writer.write("\n");

                }
                ns1++;
            }
            writer.close();
            reader.close();

            //Write to Target File
            int ns2 = 0;

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(trgInFile)), "utf-8"));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(trgOutFile)), "utf-8"));

            while ((line = reader.readLine()) != null) {

                if (bead2.containsKey(ns2)) {
                    writer.write(line);
                    writer.write("\n");
                }
                ns2++;
            }

            writer.close();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(LengthBased_Phase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void mergeLengthBasedFiles(String inDir, String lengthBasedDir, String srclang, String trglang, String ibm1Dir, String srcFile, String trgFile) {

        try {
            System.out.println("IBM1, making training data:");
            System.out.println("");
            if (!new File(ibm1Dir).exists()) {
                new File(ibm1Dir).mkdirs();
            }
            BufferedWriter srcWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ibm1Dir, srcFile)), "utf-8"));
            BufferedWriter trgWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(ibm1Dir, trgFile)), "utf-8"));
            Set<String> fileNameSet = utils.FileUtils.getFileNameSet(inDir);
            BufferedReader reader;
            int nSrcSents = 0;
            int nTrgSents = 0;
            for (String prefixName : fileNameSet) {
                String srcLengthOutFile = lengthBasedDir + "/" + String.format("%s_%s.length-based", prefixName, srclang);
                String trgLengthOutFile = lengthBasedDir + "/" + String.format("%s_%s.length-based", prefixName, trglang);

                //
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcLengthOutFile)), "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    srcWriter.write(line);
                    srcWriter.write("\n");
                    nSrcSents++;
                }
                reader.close();
                //
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(trgLengthOutFile)), "utf-8"));
                while ((line = reader.readLine()) != null) {
                    trgWriter.write(line);
                    trgWriter.write("\n");
                    nTrgSents++;
                }
                reader.close();
                //

            }
            srcWriter.close();
            trgWriter.close();
            //
            System.out.println("src sents: " + nSrcSents);
            System.out.println("trg sents: " + nTrgSents);
            System.out.println("--------------");
            System.out.println("");
        } catch (IOException ex) {
            Logger.getLogger(LengthBased_Phase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
