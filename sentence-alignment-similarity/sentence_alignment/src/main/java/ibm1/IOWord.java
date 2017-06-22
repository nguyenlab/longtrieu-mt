package ibm1;

import java.util.*;
import java.io.*;

/*
 */
public class IOWord {

    private int ns;
    private Sentences[] fs;
    private Word[] word;

    public void setNs(int ns1) {
        ns = ns1;
    }

    public void setWord(Word w[]) {
        word = w;
    }

    public void setFs(Sentences s, int i) {
        fs[i] = s;

    }

    public Word[] getWord() {
        return word;
    }

    public int getNs() {
        return ns;
    }

    public Sentences getFs(int i) {
        return fs[i];
    }

    public IOWord ReadFile(String FileName) {
        IOWord a = new IOWord();
        word = new Word[5000];

        for (int i = 0; i < 5000; i++) {
            word[i] = new Word();
        }
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(FileName)), "utf-8"));

            int Ns = 0;

            String line;
            while ((line = reader.readLine()) != null) {

                StringTokenizer words;
                words = new StringTokenizer(line);
                int nw = 0;
                String st[] = new String[1000];

                while (words.hasMoreTokens()) {
                    String w = words.nextToken();
                    st[nw] = w;
                    int i = 0;

                    nw++;
                }

                Sentences sp = new Sentences();
                sp.setNumberOfWord(nw);
                a.setFs(sp, Ns);
                Ns++;

            }

            a.setNs(Ns);

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        a.setWord(word);

        return a;
    }

    public void WriteFile(String filename, String content) {
        try {

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename)), "utf-8"));

            writer.write(content);
            writer.write("\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 
}
