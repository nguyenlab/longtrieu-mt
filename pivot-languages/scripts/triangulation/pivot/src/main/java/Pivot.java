/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Long
 */
public class Pivot {

    public static void main(String[] args) {

        String option = args[0];

        if (option.matches("triangulation")) {
            int workers = Integer.parseInt(args[3]);
            int nbest = Integer.parseInt(args[4]);
            triangulation(args[1], args[2], workers, nbest);

        } //else if (option.matches("triangulation-filter")) {
        //            int workers = Integer.parseInt(args[5]);
        //            int nbest = Integer.parseInt(args[6]);
        //            triangulation_filter(args[1], args[2], args[3], args[4], workers, nbest);
        //
        //        }
        else if (option.matches("triangulation-filter")) {
            int workers = Integer.parseInt(args[4]);
            int nbest = Integer.parseInt(args[5]);
            triangulation_filter(args[1], args[2], args[3], workers, nbest, args[6]);

        } else if (option.matches("reordering")) {
            filterReorderingTable(args[1], args[2], args[3], args[4], args[5]);
        } else if (option.matches("moses-ini")) {
            moses_ini(args[1], args[2], args[3], args[4]);
        }

    }

    public static void triangulation_filter(String src_pvtTable, String pvt_trgTable, String src_test, int workers, int nbest, String outDir) {
        
                
        utils.FileUtils.newDir(outDir);

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("PRE-TRAINING\n");

        phrasetable2binary(src_pvtTable, outDir+"/bin-table", "phrases.src", "phrases.src-pvt.pvt", "phrase-table.src-pvt");
        phrasetable2binary(pvt_trgTable, outDir+"/bin-table", "phrases.pvt-trg.pvt", "phrases.trg", "phrase-table.pvt-trg");

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("FILTERING PHRASE TABLES BY TEST SETS\n");

        extractOverlapPhrases(outDir+"/bin-table/phrases.src", src_test, outDir+"/filter", "phrases.src");
//        extractOverlapPhrases(outDir+"/bin-table/phrases.trg", trg_test, outDir+"/filter", "phrases.trg");

        filterPhraseTable_SrcPvt(outDir+"/bin-table/phrase-table.src-pvt", outDir+"/filter/phrases.src", outDir+"/filter", "phrase-table.src-pvt", "phrases.src-pvt.pvt");
//        filterPhraseTable_PvtTrg(outDir+"/bin-table/phrase-table.pvt-trg", outDir+"/filter/phrases.trg", outDir+"/filter", "phrase-table.pvt-trg", "phrases.pvt-trg.pvt");

        System.out.println("-----------------------------------------------------------------\n");

//        commonPivot(outDir+"/filter/phrases.src-pvt.pvt", outDir+"/filter/phrases.pvt-trg.pvt", outDir+"/common", "phrases.common.pvt");
        commonPivot(outDir+"/filter/phrases.src-pvt.pvt", outDir+"/bin-table/phrases.pvt-trg.pvt", outDir+"/common", "phrases.common.pvt");

        commonPivot_SrcPvtTable(outDir+"/filter/phrase-table.src-pvt", outDir+"/common/phrases.common.pvt", outDir+"/common", "phrase-table.src-pvt", "phrases.src");
//        commonPivot_PvtTrgTable(outDir+"/filter/phrase-table.pvt-trg", outDir+"/common/phrases.common.pvt", outDir+"/common", "phrase-table.pvt-trg", "phrases.trg");
        commonPivot_PvtTrgTable(outDir+"/bin-table/phrase-table.pvt-trg", outDir+"/common/phrases.common.pvt", outDir+"/common", "phrase-table.pvt-trg", "phrases.trg");


        indexPhrases(outDir+"/common/phrases.src", outDir+"/index/phrases", "phrases.src.index", "phrases.src.index.bin");
        indexPhrases(outDir+"/common/phrases.common.pvt", outDir+"/index/phrases", "phrases.pvt.index", "phrases.pvt.index.bin");
        indexPhrases(outDir+"/common/phrases.trg", outDir+"/index/phrases", "phrases.trg.index", "phrases.trg.index.bin");
        indexPhraseTable(outDir+"/common/phrase-table.src-pvt", outDir+"/index/phrases/phrases.src.index.bin", outDir+"/index/phrases/phrases.pvt.index.bin", outDir+"/index/tables", "phrase-table.src-pvt.index.bin");
        indexPhraseTable(outDir+"/common/phrase-table.pvt-trg", outDir+"/index/phrases/phrases.pvt.index.bin", outDir+"/index/phrases/phrases.trg.index.bin", outDir+"/index/tables", "phrase-table.pvt-trg.index.bin");

        normalizeFilteredScores(outDir+"/index/tables", outDir+"/index/phrases/phrases.src.index.bin", outDir+"/index/phrases/phrases.pvt.index.bin", "phrase-table.src-pvt.index.bin", "phrase-table.src-pvt.index.norm.bin");
        normalizeFilteredScores(outDir+"/index/tables", outDir+"/index/phrases/phrases.pvt.index.bin", outDir+"/index/phrases/phrases.trg.index.bin", "phrase-table.pvt-trg.index.bin", "phrase-table.pvt-trg.index.norm.bin");

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("TRANING: TRIANGULATION\n");
        System.out.println("Threads: " + workers);

        triangulation(outDir+"/index/phrases/phrases.src.index.bin", outDir+"/index/tables/phrase-table.src-pvt.index.norm.bin", outDir+"/index/tables/phrase-table.pvt-trg.index.norm.bin", outDir+"/pivot/scores", outDir+"/pivot", "triangulation.scores", workers);
        nbestOutput(outDir+"/pivot/triangulation.scores", nbest, outDir+"/pivot", "triangulation.scores.nbest", "phrases.src", "phrases.trg");
        normalizeScores(outDir+"/pivot", outDir+"/pivot/phrases.src", outDir+"/pivot/phrases.trg", "triangulation.scores.nbest", "triangulation.scores.nbest.normalized");

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("MODEL: \n");
        inducePhraseTable(outDir+"/pivot/triangulation.scores.nbest.normalized", outDir+"/index/phrases/phrases.src.index.bin", outDir+"/index/phrases/phrases.trg.index.bin", outDir+"/pivot", "phrase-table.model");

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("TRAINING: REORDERING TABLE \n");
      
    }

    public static void triangulation(String src_pvtTable, String pvt_trgTable, int workers, int nbest) {

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("PRE-TRAINING\n");

        phrasetable2binary(src_pvtTable, "triangulation/bin-table", "phrases.src", "phrases.src-pvt.pvt", "phrase-table.src-pvt");
        phrasetable2binary(pvt_trgTable, "triangulation/bin-table", "phrases.pvt-trg.pvt", "phrases.trg", "phrase-table.pvt-trg");

        System.out.println("-----------------------------------------------------------------\n");

        commonPivot("triangulation/bin-table/phrases.src-pvt.pvt", "triangulation/bin-table/phrases.pvt-trg.pvt", "triangulation/common", "phrases.common.pvt");
        commonPivot_SrcPvtTable("triangulation/bin-table/phrase-table.src-pvt", "triangulation/common/phrases.common.pvt", "triangulation/common", "phrase-table.src-pvt", "phrases.src");
        commonPivot_PvtTrgTable("triangulation/bin-table/phrase-table.pvt-trg", "triangulation/common/phrases.common.pvt", "triangulation/common", "phrase-table.pvt-trg", "phrases.trg");
        indexPhrases("triangulation/common/phrases.src", "triangulation/index/phrases", "phrases.src.index", "phrases.src.index.bin");
        indexPhrases("triangulation/common/phrases.common.pvt", "triangulation/index/phrases", "phrases.pvt.index", "phrases.pvt.index.bin");
        indexPhrases("triangulation/common/phrases.trg", "triangulation/index/phrases", "phrases.trg.index", "phrases.trg.index.bin");
        indexPhraseTable("triangulation/common/phrase-table.src-pvt", "triangulation/index/phrases/phrases.src.index.bin", "triangulation/index/phrases/phrases.pvt.index.bin", "triangulation/index/tables", "phrase-table.src-pvt.index.bin");
        indexPhraseTable("triangulation/common/phrase-table.pvt-trg", "triangulation/index/phrases/phrases.pvt.index.bin", "triangulation/index/phrases/phrases.trg.index.bin", "triangulation/index/tables", "phrase-table.pvt-trg.index.bin");

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("TRANING: TRIANGULATION\n");
        System.out.println("Threads: " + workers);
        triangulation("triangulation/index/phrases/phrases.src.index.bin", "triangulation/index/tables/phrase-table.src-pvt.index.bin", "triangulation/index/tables/phrase-table.pvt-trg.index.bin", "triangulation/pivot/scores", "triangulation/pivot", "triangulation.scores", workers);
        nbestOutput("triangulation/pivot/triangulation.scores", nbest, "triangulation/pivot", "triangulation.scores.nbest", "phrases.src", "phrases.trg");
        normalizeScores("triangulation/pivot", "triangulation/pivot/phrases.src", "triangulation/pivot/phrases.trg", "triangulation.scores.nbest", "triangulation.scores.nbest.normalized");

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("MODEL: \n");
        inducePhraseTable("triangulation/pivot/triangulation.scores.nbest.normalized", "triangulation/index/phrases/phrases.src.index.bin", "triangulation/index/phrases/phrases.trg.index.bin", "triangulation/pivot", "phrase-table.model");

        System.out.println("-----------------------------------------------------------------\n");
        System.out.println("TRAINING: REORDERING TABLE \n");
    }

    public static void phrasetable2binary(String phraseTableInputFile, String outDir, String srcPhrasesOutputFile, String trgPhrasesOutputFile,
            String phraseTableBinOutputFile) {
        triangulation.BinTable.extractBinPhraseTable(phraseTableInputFile, outDir, srcPhrasesOutputFile, trgPhrasesOutputFile, phraseTableBinOutputFile);
    }

    public static void extractOverlapPhrases(String phraseFile, String testFile, String outDir, String extractedPhraseFile) {
        triangulation.Filter.extractOverlapPhrases(phraseFile, testFile, outDir, extractedPhraseFile);
    }

    public static void filterPhraseTable_SrcPvt(String inTableFile, String srcFile, String outDir, String outTableFile, String pvtFile) {
        triangulation.Filter.filterPhraseTable_SrcPvt(inTableFile, srcFile, outDir, outTableFile, pvtFile);
    }

    public static void filterPhraseTable_PvtTrg(String inTableFile, String trgFile, String outDir, String outTableFile, String pvtFile) {
        triangulation.Filter.filterPhraseTable_PvtTrg(inTableFile, trgFile, outDir, outTableFile, pvtFile);
    }

    public static void indexPhraseTable(String phraseTableInFile, String srcIndexInFile, String trgIndexInFile, String outDir, String phraseTableIndexOutFile) {
        triangulation.Indexing.phraseTableIndex(phraseTableInFile, srcIndexInFile, trgIndexInFile, outDir, phraseTableIndexOutFile);
    }

    public static void indexPhrases(String phrasesInFile, String outDir, String indexOutFile, String indexBinOutFile) {
        triangulation.Indexing.phrasesIndex(phrasesInFile, outDir, indexOutFile, indexBinOutFile);
    }

    public static void commonPivot_SrcPvtTable(String phraseTableInFile, String triangulationFile, String outDir, String phraseTableOutFile, String srcFile) {
        triangulation.CommonPivot.extractPhraseTable_SrcPvt(phraseTableInFile, triangulationFile, outDir, phraseTableOutFile, srcFile);
    }

    public static void commonPivot_PvtTrgTable(String phraseTableInFile, String triangulationFile, String outDir, String phraseTableOutFile, String trgFile) {
        triangulation.CommonPivot.extractPhraseTable_PvtTrg(phraseTableInFile, triangulationFile, outDir, phraseTableOutFile, trgFile);
    }

    public static void commonPivot(String file1, String file2, String outDir, String filetriangulation) {
        triangulation.CommonPivot.commonPivotPhrases(file1, file2, outDir, filetriangulation);
    }

    public static void inducePhraseTable(String normFile, String srcIndexInFile, String trgIndexInFile, String outDir, String phraseTableOutFile) {

        triangulation.Model.inducePhraseTable(normFile, srcIndexInFile, trgIndexInFile, outDir, phraseTableOutFile);
    }

    public static void nbestOutput(String inFile, int nbest, String outDir, String outFile, String srcFile, String trgFile) {
        triangulation.NbestOutput.extractNbest(inFile, nbest, outDir, outFile, srcFile, trgFile);
    }

    public static void normalizeScores(String workDir, String srcPhrasesFile, String trgPhrasesFile, String scoredFile, String normalizedFile) {
        triangulation.Normalize.normalizeScores(workDir, srcPhrasesFile, trgPhrasesFile, scoredFile, normalizedFile);
    }

    public static void triangulation(String srcPhraseFile, String src_pvtTable, String pvt_trgTable, final String scoreDir, String outDir, String outFile, int threadNum) {
        triangulation.Triangulation.triangulation_full(srcPhraseFile, src_pvtTable, pvt_trgTable, scoreDir, outDir, outFile, threadNum);
    }

    public static void filterReorderingTable(String phraseTableFile, String reorderingTableFile, String outDir, String outPhraseTable, String outReorderingTable) {
        utils.Reordering.filterReorderingTable(phraseTableFile, reorderingTableFile, outDir, outPhraseTable, outReorderingTable);
    }

    public static void normalizeFilteredScores(String workDir, String srcPhrasesFile, String trgPhrasesFile, String scoredFile, String normalizedFile) {
        triangulation.Filter.normalizeFilteredScores(workDir, srcPhrasesFile, trgPhrasesFile, scoredFile, normalizedFile);
    }

    public static void moses_ini(String modelDir, String phraseTableName, String reorderingTableName, String languageModelPath) {
        utils.Moses_Ini.moses_ini(modelDir, phraseTableName, reorderingTableName, languageModelPath);
    }

}
