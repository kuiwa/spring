package com.hibernate.exceltojavabean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.testng.annotations.Test;

public class GenerateBean {

    static void generatNewBean(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++)
                out.write(buffer[i]);
        }

        in.close();
        out.close();
    }

    public static void writeBeans(String src, String beanText) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(src, true)));
            out.write(beanText);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    static void writeEndBeans(String src) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(src, true)));
            out.write("</beans>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public static void test() throws IOException {
        generatNewBean("C:\\Users\\EKUIWAG\\Desktop\\TMU_R2.xml", "C:\\Users\\EKUIWAG\\Desktop\\bean2.xml");
//        writeEndBeans("C:\\Users\\EKUIWAG\\Desktop\\bean2.xml");
    }
}
