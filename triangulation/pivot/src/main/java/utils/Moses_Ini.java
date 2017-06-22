/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Long
 */
public class Moses_Ini {

    public static void moses_ini(String modelDir, String phraseTableName, String reorderingTableName, String languageModelPath) {

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(modelDir, "moses.ini")), "utf-8"));

            writer.write(String.format("%s\n", "#########################"));
            writer.write(String.format("%s\n", "### MOSES CONFIG FILE ###"));
            writer.write(String.format("%s\n\n", "#########################"));

            writer.write(String.format("%s\n", "# input factors"));
            writer.write(String.format("%s\n", "[input-factors]"));
            writer.write(String.format("%s\n\n", "0"));

            writer.write(String.format("%s\n", "# mapping steps"));
            writer.write(String.format("%s\n", "[mapping]"));
            writer.write(String.format("%s\n\n", "0 T 0"));

            writer.write(String.format("%s\n", "[distortion-limit]"));
            writer.write(String.format("%s\n\n", "6"));

            writer.write(String.format("%s\n", "# feature functions"));
            writer.write(String.format("%s\n", "[feature]"));
            writer.write(String.format("%s\n", "UnknownWordPenalty"));
            writer.write(String.format("%s\n", "WordPenalty"));

            writer.write(String.format("%s\n", "PhrasePenalty"));
            writer.write(String.format("PhraseDictionaryMemory name=TranslationModel0 num-features=4 path=%s input-factor=0 output-factor=0\n", modelDir + "/" + phraseTableName));
            writer.write(String.format("LexicalReordering name=LexicalReordering0 num-features=6 type=wbe-msd-bidirectional-fe-allff input-factor=0 output-factor=0 path=%s\n", modelDir + "/" + reorderingTableName));
            writer.write(String.format("%s\n", "Distortion"));
            writer.write(String.format("KENLM name=LM0 factor=0 path=%s order=5\n\n", languageModelPath));
            writer.write(String.format("%s\n", "# dense weights for feature functions"));
            writer.write(String.format("%s\n", "[weight]"));
            writer.write(String.format("%s\n", "# The default weights are NOT optimized for translation quality. You MUST tune the weights."));
            writer.write(String.format("%s\n", "# Documentation for tuning is here: http://www.statmt.org/moses/?n=FactoredTraining.Tuning "));
            writer.write(String.format("%s\n", "UnknownWordPenalty0= 1"));
            writer.write(String.format("%s\n", "WordPenalty0= -1"));
            writer.write(String.format("%s\n", "PhrasePenalty0= 0.2"));
            writer.write(String.format("%s\n", "TranslationModel0= 0.2 0.2 0.2 0.2"));
            writer.write(String.format("%s\n", "LexicalReordering0= 0.3 0.3 0.3 0.3 0.3 0.3"));
            writer.write(String.format("%s\n", "Distortion0= 0.3"));
            writer.write(String.format("%s\n", "LM0= 0.5"));
//            writer.write(String.format("%s\n", ""));

            writer.close();
            System.out.println("-----------------------------------------------------------------\n");
            System.out.println("created: triangulation/model/moses.ini");
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Moses_Ini.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
