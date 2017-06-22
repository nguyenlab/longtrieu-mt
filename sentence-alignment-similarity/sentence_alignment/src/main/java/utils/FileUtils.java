/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Long
 */
public class FileUtils {

    public static List<String> getFileNameList(String inDir) {

        List<String> nameList = new ArrayList<>();
        TreeSet<String> nameSet = new TreeSet<>();

        for (File file : new File(inDir).listFiles()) {
            String fileName = file.getName().split("_")[0];
            nameSet.add(fileName);
        }

        for (String fileName : nameSet) {
            nameList.add(fileName);
        }

        return nameList;
    }

    public static void newDir(String dirName) {
        if (!new File(dirName).exists()) {
            new File(dirName).mkdirs();
        }
    }

    public static Set<String> getFileNameSet(String inDir) {

        Set<String> fileNameSet = new HashSet<>();

        for (File file : new File(inDir).listFiles()) {
            String fileName = file.getName();
            String prefixName = fileName.substring(0, fileName.length() - 7);
            fileNameSet.add(prefixName);
        }

        return fileNameSet;

    }

    public static String makeFilePath(String inDir, String name, String type, String lang, String extension) {

        StringBuilder fileName = new StringBuilder(100);

        fileName.append(inDir).append("/")
                .append(name).append(".")
                .append(type).append("_").append(lang)
                .append(".").append(extension);

        return fileName.toString();

    }

    public static List<String> getFileList(String inDir) {

        List<String> fileList = new ArrayList<>();

        TreeSet<String> fileNameSet = new TreeSet<>();

        for (File file : new File(inDir).listFiles()) {
            String fileName = file.getName();
            if (fileName.endsWith(".test_en.snt")) {
                fileName = fileName.replace(".test_en.snt", "");
                fileNameSet.add(fileName);
            } else if (fileName.endsWith(".test_vi.snt")) {
                fileName = fileName.replace(".test_vi.snt", "");
                fileNameSet.add(fileName);
            }
        }

        for (String fileName : fileNameSet) {
            fileList.add(fileName);
        }

        return fileList;

    }

}
