/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 *
 * @author Long
 */
public class IBM1 {

    public static void build_IBM1(String srcTrainFile, String trgTrainFile, int loop, String outDir, String ibm1File) {

        System.out.println("Building IBM1...");
        buildModelOne(
                srcTrainFile,
                trgTrainFile,
                loop,
                outDir,
                ibm1File
        );

        System.out.println("success!");
        System.out.println("-----------------------------------------");
        System.out.println("");

    }

    public static void buildModelOne(String filenamesource, String filenametarget, int loop, String outDir, String modelFile) {
        
        utils.FileUtils.newDir(outDir);

        IBM1 c = new IBM1();

        ArrayList arraywords = new ArrayList();

        HashMap hashmaps = new HashMap();
        hashmaps = c.readFile(filenamesource);
        hashmaps = c.filterWord(hashmaps, 1);
        arraywords = c.generateTrain(filenamesource, hashmaps, true);

        ArrayList arraywordt = new ArrayList();
        HashMap hashmapt = new HashMap();

        hashmapt = c.readFile(filenametarget);
        hashmapt = c.filterWord(hashmapt, 1);
        arraywordt = c.generateTrain(filenametarget, hashmapt, false);

        if (arraywords.size() != arraywordt.size()) {
            System.out.print("ERROR: \n");
            return;
        }

        HashMap word = c.setWord(arraywords, arraywordt);

        HashMap pairword = ((HashMap) word.get("pword"));
        HashMap targetword = ((HashMap) word.get("tword"));

        pairword = c.doMaxProb(pairword, targetword);

        for (int i = 0; i < loop; i++) {

            pairword = c.mainLoop(pairword, arraywords, arraywordt);

        }

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outDir, modelFile)), "utf-8"));

            //-------write
            Iterator i = pairword.keySet().iterator();
            while (i.hasNext()) {
                String me = (String) i.next();
                HashMap oldtar = (HashMap) pairword.get(me);
                Iterator j = oldtar.keySet().iterator();
                while (j.hasNext()) {

                    String you = (String) j.next();
                    writer.write(String.format("%s %s %s\n", oldtar.get(you), me, you));

                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return;

    }

    public HashMap setWord(ArrayList<Sentences> ssentences, ArrayList<Sentences> tsentences) {

        HashMap pwordarray = new HashMap();
        HashMap twordarray = new HashMap();
        HashMap result = new HashMap();

        for (int r = 0; r < ssentences.size(); r++) {

            Sentences ssentence = (Sentences) ssentences.get(r);
            Sentences tsentence = (Sentences) tsentences.get(r);
            ArrayList swords = ssentence.getVecto();

            for (int k = 0; k < swords.size(); k++) {

                String sword = (String) swords.get(k);
                ArrayList twords = tsentence.getVecto();

                for (int j = 0; j < twords.size(); j++) {

                    String tword = (String) twords.get(j);
                    pwordarray = addPairWord(pwordarray, sword, tword, (double) 1 / ssentence.getNumberOfWord());
                    twordarray = addTargetWord(twordarray, sword, (double) 1 / ssentence.getNumberOfWord());

                }

            }
        }

        result.put("pword", pwordarray);
        result.put("tword", twordarray);
        return result;

    }

    /**
     * *********************
     */
    public HashMap readFile(String filename) {
        HashMap hashmap = new HashMap();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)), "utf-8"));

            String line;
            while ((line = reader.readLine()) != null) {

                StringTokenizer words;
                words = new StringTokenizer(line);
                int nw = 0;

                while (words.hasMoreTokens()) {
                    nw++;
                    String w = words.nextToken();

                    if (hashmap.containsKey(w)) {
                        int count = ((Integer) hashmap.get(w));

                        hashmap.put(w, new Integer(count + 1));
                    } else {

                        hashmap.put(w, new Integer(1));
                    }

                }

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashmap;
    }

    public HashMap filterWord(HashMap words, int countlimit) {

        HashMap result = new HashMap();
        int othercount = 0;

        Iterator i = words.keySet().iterator();

        while (i.hasNext()) {

            String me = (String) i.next();
            int countme = (Integer) words.get(me);
            if (countme <= countlimit) {

                othercount = othercount + countme;
            } else {
                result.put(me, countme);
            }

        }

        String othertext = new String("(other)");
        result.put(othertext, othercount);

        return result;

    }

    public ArrayList<Sentences> generateTrain(String filenameE, HashMap E, boolean last) {

        ArrayList result = new ArrayList();

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filenameE)), "utf-8"));

            String line;
            while ((line = reader.readLine()) != null) {

                StringTokenizer words;
                words = new StringTokenizer(line);
                Sentences stn = new Sentences();

                ArrayList string = new ArrayList();

                while (words.hasMoreTokens()) {

                    String w = words.nextToken();

                    if (E.containsKey(w)) {
                        if ((Integer) E.get(w) > 0) {

                            string.add(w);

                        } else {

                            string.add("(other)");
                        }
                    } else {

                        string.add("(other)");

                    }

                }
                if (last == true) {
                    string.add("(empty)");

                }
                stn.setVecto(string);
                stn.setNumberOfWord(string.size());
                result.add(stn);

            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    public HashMap mainLoop(HashMap proword, ArrayList<Sentences> ssentences, ArrayList<Sentences> tsentences) {

        HashMap apairword = new HashMap();
        HashMap atargetword = new HashMap();
        HashMap result = new HashMap();
        for (int a = 0; a < ssentences.size(); a++) {

            Sentences ss = (Sentences) ssentences.get(a);
            Sentences ts = (Sentences) tsentences.get(a);

            double fract_count_limit = (double) 1 / ss.getNumberOfWord();
            ArrayList twords1 = ts.getVecto();

            for (int j = 0; j < twords1.size(); j++) {

                double trans_prob_sum = 0;
                String tword = (String) twords1.get(j);
                ArrayList swords1 = ss.getVecto();

                for (int k = 0; k < swords1.size(); k++) {

                    String sword = (String) swords1.get(k);
                    trans_prob_sum += this.getTransProb(sword, tword, proword);

                }

                ArrayList swords2 = ss.getVecto();

                for (int l = 0; l < swords2.size(); l++) {

                    String sword = (String) swords2.get(l);
                    double fractcount = (double) this.getTransProb(sword, tword, proword) / trans_prob_sum;

                    if (fractcount > fract_count_limit) {

                        apairword = addPairWord(apairword, sword, tword, fractcount);
                        atargetword = addTargetWord(atargetword, sword, fractcount);
                    } else {
                        apairword = addPairWord(apairword, "(empty)", tword, fractcount);
                        atargetword = addTargetWord(atargetword, "(empty)", fractcount);

                    }

                }

            }

        }

        Iterator i = apairword.keySet().iterator();

        while (i.hasNext()) {

            String me = (String) i.next();
            HashMap newtar = new HashMap();
            HashMap oldtar = (HashMap) apairword.get(me);

            Iterator j = oldtar.keySet().iterator();
            double count_sum = this.getProTarWord(atargetword, me);

            while (j.hasNext()) {

                String you = (String) j.next();
                newtar.put(you, ((Double) oldtar.get(you) / count_sum));

            }

            result.put(me, newtar);
        }

        return result;

    }

    public double getTransProb(String sword, String tword, HashMap proword) {

        if (proword.containsKey(sword)) {
            HashMap targets = (HashMap) proword.get(sword);
            if (targets.containsKey(tword)) {

                return ((Double) targets.get(tword));

            }
        }
        return 0;

    }

    public HashMap addPairWord(HashMap pairwords, String sword, String tword, double fract_count) {

        if (pairwords.containsKey(sword)) {
            HashMap pairword = (HashMap) pairwords.get(sword);
            if (pairword.containsKey(tword)) {
                double oldvalue = (Double) pairword.get(tword);
                pairword.put(tword, new Double(oldvalue + fract_count));

            } else {
                pairword.put(tword, new Double(fract_count));

            }
            pairwords.put(sword, pairword);
        } else {
            HashMap pairword = new HashMap();
            pairword.put(tword, fract_count);
            pairwords.put(sword, pairword);

        }
        return pairwords;
    }

    public HashMap addTargetWord(HashMap targetwords, String sword, double fract_count) {

        if (targetwords.containsKey(sword)) {
            double count = ((Double) targetwords.get(sword));

            targetwords.put(sword, new Double(count + fract_count));
        } else {

            targetwords.put(sword, new Double(fract_count));
        }
        return targetwords;
    }

    public double getProTarWord(HashMap tarword, String word) {

        if (tarword.containsKey(word)) {
            double count = ((Double) tarword.get(word));
            return count;
        } else {
            return 0.0;
        }

    }

    public void writeOutput(String filename, HashMap pairword) {

        String content = "";
        Iterator i = pairword.keySet().iterator();
        while (i.hasNext()) {
            String me = (String) i.next();
            HashMap oldtar = (HashMap) pairword.get(me);
            Iterator j = oldtar.keySet().iterator();
            while (j.hasNext()) {

                String you = (String) j.next();
                content += (Double) oldtar.get(you) + " " + me + " " + you + "\n";
            }
        }

        IOWord ioword = new IOWord();
        ioword.WriteFile(filename, content);
        return;

    }

    public HashMap doMaxProb(HashMap pairwords, HashMap targetwords) {

        Iterator i = pairwords.keySet().iterator();
        while (i.hasNext()) {

            String me = (String) i.next();
            HashMap oldtar = (HashMap) pairwords.get(me);

            if (targetwords.containsKey(me)) {
                double count = ((Double) targetwords.get(me));

                Iterator j = oldtar.keySet().iterator();

                while (j.hasNext()) {

                    String you = (String) j.next();
                    oldtar.put(you, ((Double) oldtar.get(you) / count));
                }
                pairwords.put(me, oldtar);
            }
        }
        return pairwords;

    }

}
