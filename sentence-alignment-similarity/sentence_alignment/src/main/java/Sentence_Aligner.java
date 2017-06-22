/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Long
 */
public class Sentence_Aligner {

    public static void main(String[] args) {
        String option = args[0];

        if (option.matches("length-based")) {
            length_based(args[1], args[2], args[3], Integer.parseInt(args[4]), Double.parseDouble(args[5]), args[6], args[7], args[8], args[9]);
        } else if (option.matches("ibm1")) {
            ibm1(args[1], args[2], Integer.parseInt(args[3]), args[4], args[5]);
        } else if (option.matches("nbest-ibm1")) {
            nbest_ibm1(args[1], Integer.parseInt(args[2]), Double.parseDouble(args[3]), args[4], args[5], args[6], args[7]);
        } else if (option.matches("ibm1-paraphrase")) {
            extractIBM1Paraphrase(args[1], args[2], args[3], args[4]);
        } else if (option.matches("filter-w2v-similarity-paraphrase")) {
            extractIBM1ParaphraseW2V(args[1], args[2], args[3], args[4]);
        } else if (option.matches("enrich-ibm1-w2v")) {
            extendIBM1Similarity(args[1], args[2], args[3], args[4], args[5], args[6]);
        }
        else if (option.matches("length-and-word-based")) {
            length_and_word_based(args[1], args[2], args[3], Integer.parseInt(args[4]), Double.parseDouble(args[5]), args[6], args[7], args[8]);
        }
    }

    public static void length_based(String inDir, String srclang, String trglang, int searchinglimit, double threshold1, String lengthBasedDir, String ibm1Dir, String mergedSrcFile, String mergedTrgFile) {
        length_based.LengthBased_Phase.length_based_dir(inDir, srclang, trglang, searchinglimit, threshold1, lengthBasedDir, ibm1Dir, mergedSrcFile, mergedTrgFile);
    }

    public static void ibm1(String mergedSrcFile, String mergedTrgFile, int loop, String ibm1Dir, String ibm1File) {
        ibm1.IBM1.build_IBM1(mergedSrcFile, mergedTrgFile, loop, ibm1Dir, ibm1File);
    }

    public static void nbest_ibm1(String ibm1File, int nbest, double threshold, String outDir, String outIBM1File, String srcFile, String trgFile) {
        similarity.NbestIBM1.nbestScoresIBM1(ibm1File, nbest, threshold, outDir, outIBM1File, srcFile, trgFile);
    }

    public static void extractIBM1Paraphrase(String ibm1File, String outDir, String outFile, String outTextFile) {

        similarity.Paraphrase.extractIBM1Paraphrase(ibm1File, outDir, outFile, outTextFile);
    }

    public static void extractIBM1ParaphraseW2V(String paraPhraseFile, String similarityFile, String outDir, String outFile) {
        similarity.Paraphrase.extractIBM1ParaphraseW2V(paraPhraseFile, similarityFile, outDir, outFile);
    }

    public static void extendIBM1Similarity(String ibm1File, String ibm1NbestFile, String srcSimilarityFile, String trgSimilarityFile, String outDir, String ibm1OutFile) {
        similarity.Word2VecSimilarity.extendIBM1Similarity(ibm1File, ibm1NbestFile, srcSimilarityFile, trgSimilarityFile, outDir, ibm1OutFile);
    }
    
     public static void length_and_word_based(String inDir, String srclang, String trglang, int searchinglimit, double threshold2, String lengthBasedDir, String ibm1File, String alignedDir) {
        length_and_word.LengthAndWordBased_Phase.lengthAndWordBased(inDir, srclang, trglang, searchinglimit, threshold2, lengthBasedDir, ibm1File, alignedDir);
    }

}
