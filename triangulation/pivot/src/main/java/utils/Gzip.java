/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author Long
 */
public class Gzip {

    public static void gzipIt(String outDir, String SOURCE_FILE, String OUTPUT_GZIP_FILE) {
        byte[] buffer = new byte[1024];

        try {

            GZIPOutputStream gzos
                    = new GZIPOutputStream(new FileOutputStream(new File(outDir, OUTPUT_GZIP_FILE)));

            FileInputStream in = new FileInputStream(new File(outDir, SOURCE_FILE));

            int len;
            while ((len = in.read(buffer)) > 0) {
                gzos.write(buffer, 0, len);
            }

            in.close();

            gzos.finish();
            gzos.close();

        } catch (IOException ex) {
            Logger.getLogger(Gzip.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
