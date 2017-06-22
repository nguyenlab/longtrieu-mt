/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Long
 */
public class Alignment {
    public static int[] extractAlignment(String s) {
        String[] st = s.trim().split("[- ]");
        int[] alignment = new int[st.length];

        for (int i = 0; i < st.length; i++) {
            alignment[i] = Integer.valueOf(st[i]);
        }

        return alignment;

    }
    
     public static int[] alignmentInduction(int[] jaen_alignment, int[] envi_alignment) {

        int[] t_alignment = new int[jaen_alignment.length / 2 * envi_alignment.length / 2 * 2];
        int t_size = 0;
        for (int i = 0; i < jaen_alignment.length; i += 2) {
            for (int j = 0; j < envi_alignment.length; j += 2) {
                if (jaen_alignment[i + 1] == envi_alignment[j]) {
                    t_alignment[t_size] = jaen_alignment[i];
                    t_alignment[t_size + 1] = envi_alignment[j + 1];
                    t_size += 2;
                }
            }
        }
        int[] re_alignment = new int[t_size];
        System.arraycopy(t_alignment, 0, re_alignment, 0, t_size);
        return re_alignment;
    }
     
     /**
     * alignment format: "1-2 3-5"
     *
     * @param phraseTableInFile
     * @return int[]: [1,2,3,5]
     * @throws IOException
     */
    public static Table<Integer, Integer, int[]> readAlignment_trgPvt(String phraseTableInFile) throws IOException {
        Table<Integer, Integer, int[]> table = HashBasedTable.create();
        DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(phraseTableInFile)));
        //
        while (binReader.available() > 0) {
            int enPhrase = binReader.readInt();
            int viPhrase = binReader.readInt();
            //
            for (int i = 0; i < 4; i++) {
                binReader.readFloat();
            }
            //
            String[] alignment_st = binReader.readUTF().trim().split("[- ]");
            int[] alignment = new int[alignment_st.length];
            for (int i = 0; i < alignment_st.length; i++) {
                alignment[i] = Integer.valueOf(alignment_st[i]);
            }
            table.put(viPhrase, enPhrase, alignment);
            //
            binReader.readUTF();
        }
        binReader.close();
        //
        return table;
    }

    /**
     * alignment format: "1-2 3-5"
     *
     * @param phraseTableInFile
     * @return int[]: [1,2,3,5]
     * @throws IOException
     */
    public static Table<Integer, Integer, int[]> readAlignment_srcPvt(String phraseTableInFile) throws IOException {
        Table<Integer, Integer, int[]> table = HashBasedTable.create();
        DataInputStream binReader = new DataInputStream(new BufferedInputStream(new FileInputStream(phraseTableInFile)));
        //
        while (binReader.available() > 0) {
            int jaPhrase = binReader.readInt();
            int enPhrase = binReader.readInt();
            //
            for (int i = 0; i < 4; i++) {
                binReader.readFloat();
            }
            //
            String[] alignment_st = binReader.readUTF().trim().split("[- ]");
            int[] alignment = new int[alignment_st.length];
            for (int i = 0; i < alignment_st.length; i++) {
                alignment[i] = Integer.valueOf(alignment_st[i]);
            }
            table.put(jaPhrase, enPhrase, alignment);
            //
            binReader.readUTF();
        }
        binReader.close();
        //
        return table;
    }

}
