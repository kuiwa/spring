package com.hibernate.exceltojavabean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateBean {

    public static void writeBeans(String src, String beanText) throws IOException {
        File file = new File(src);
        FileWriter fWriter;
        if (!file.exists())
            file.createNewFile();
        fWriter = new FileWriter(file, true);
        fWriter.write(beanText);
        fWriter.close();
        //        BufferedWriter out = null;
        //        try {
        //            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(src, true)));
        //            out.write(beanText);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        } finally {
        //            try {
        //                out.close();
        //            } catch (IOException e) {
        //                e.printStackTrace();
        //            }
        //        }
    }
}
