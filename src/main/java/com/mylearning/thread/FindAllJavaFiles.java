package com.mylearning.thread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.testng.annotations.Test;

public class FindAllJavaFiles {

    @Test
    public void runTest() throws IOException {
        String path = "C:\\Users\\EKUIWAG\\git\\spring\\src\\main\\java";
        findAndCheckAllJavaFiles(path);
    }
    
    @Test
    public void runTestThread() throws IOException {
        String path = "C:\\Users\\EKUIWAG\\git\\spring\\src\\main\\java";
        findAndCheckAllJavaFiles(path);
    }

    protected void findAndCheckAllJavaFiles(String tempPath) throws IOException {
        File file = new File(tempPath);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                if (f.getName().endsWith(".java")) {
                    System.out.println(f.getAbsolutePath() + f.getName());
                }
            } else {
                findAndCheckAllJavaFiles(f.getAbsolutePath());
            }
        }     
  
    }
    protected void findAndCheckAllJavaFilesWithThread(String tempPath) throws IOException {
        File file = new File(tempPath);
        File[] files = file.listFiles();
        List<File> fileList = new ArrayList<>(Arrays.asList(files));
        
        fileList.stream().forEach(f -> {
            if (f.isFile()) {
                if (f.getName().endsWith(".java")) {
                    Thread thread = new Thread(() -> System.out.println(f.getAbsolutePath() + f.getName()));
                    thread.start();
                }
            } else {
                try {
                    findAndCheckAllJavaFiles(f.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        });        
    }
}
