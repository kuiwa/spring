package com.hibernate.exceltojavabean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateBean {

    public static void writeBeans(String path, String fileName, String beanText, boolean overRide) throws IOException {
        File file = new File(path+fileName);
        FileWriter fWriter;
        File fileParent = file.getParentFile();
        if (!fileParent.exists())
            fileParent.mkdirs();
        if (!file.exists())
            file.createNewFile();
        fWriter = new FileWriter(file, overRide);
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
