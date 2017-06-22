/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package length_and_word;

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
import length_based.IOData;
import length_based.SentLengthRatio;

/**
 *
 * @author Long
 */
public class LengthAndWordBased_Phase {

    public static void lengthAndWordBased(String inDir, String srclang, String trglang, int searchinglimit, double threshold, String lengthBasedDir, String ibm1File, String alignedDir) {

        System.out.println("LENGTH AND WORD BASED PHASE:");
        System.out.println("");

        if (!new File(alignedDir).exists()) {
            new File(alignedDir).mkdirs();
        }

        ReadIBM ibm1Model = new ReadIBM();
        ibm1Model.readIBM1(ibm1File);

        List<String> fileNameSet = utils.FileUtils.getFileNameList(inDir);

        for (String prefixName : fileNameSet) {

            String srcInFile = inDir + "/" + String.format("%s_%s.snt", prefixName, srclang);
            String trgInFile = inDir + "/" + String.format("%s_%s.snt", prefixName, trglang);
            String nodeFile = lengthBasedDir + "/" + prefixName + ".node";
            String srcOutFile = alignedDir + "/" + String.format("%s_%s.aligned", prefixName, srclang);
            String trgOutFile = alignedDir + "/" + String.format("%s_%s.aligned", prefixName, trglang);

            lengthAndWordBased(ibm1Model, srcInFile, trgInFile, nodeFile, threshold, srcOutFile, trgOutFile, searchinglimit);

        }

    }

    public static void lengthAndWordBased(
            ReadIBM ibm1Model,
            String srcInFile,
            String trgInFile,
            String nodeFile,
            double threshold,
            String srcOutFile,
            String trgOutFile,
            int searchinglimit
    ) {

        IOData sourceData = new IOData();
        IOData targetData = new IOData();

        sourceData.SetIODataComplex(srcInFile);
        targetData.SetIODataComplex(trgInFile);
        SentLengthRatio sentenceLength = new SentLengthRatio();
        sentenceLength.ComputeSentLengthRatio(sourceData, targetData);

        CountWords countWords = new CountWords();

        countWords.SetWord(srcInFile, trgInFile);
        countWords.CountWord(sourceData, targetData, ibm1Model);

        LengthAndWordBased lengthAndWord = new LengthAndWordBased();
        lengthAndWord.SentScore(sourceData, targetData, countWords);
        lengthAndWord.ReadNode(nodeFile, sourceData.getNs(), targetData.getNs());
        lengthAndWord.InitPlus(sourceData, targetData, sentenceLength, ibm1Model);

        lengthAndWord.Forward_Backward(
                searchinglimit,
                threshold
        );

        writeComplex(lengthAndWord.getBead1(), lengthAndWord.getBead2(), srcInFile, trgInFile, srcOutFile, trgOutFile);
    }

    public static void writeComplex(Map<Integer, Double> bead1, Map<Integer, Double> bead2,
            String srcInFile, String trgInFile,
            String srcOutFile, String trgOutFile
    ) {

        int ns1 = 0;
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(srcOutFile)), "utf-8"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(srcInFile)), "utf-8"));

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

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write to Target File
        int ns2 = 0;
        try {

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(trgOutFile)), "utf-8"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(trgInFile)), "utf-8"));

            String line;
            while ((line = reader.readLine()) != null) {

                if (bead2.containsKey(ns2)) {

                    writer.write(line);
                    writer.write("\n");
                }
                ns2++;
            }
            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
