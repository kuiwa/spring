//package com.hibernate.exceltojavabean;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.ResourceBundle;
//
//import org.apache.poi.hpsf.Variant;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.DateUtil;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Parameters;
//import org.testng.annotations.Test;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//
//public class ExcelToBeanOriginal
//{
//
//    private static final String UNSET = "Unset";
//    private static ResourceBundle modelPropertiesBundle;
//    private List<String> referenceIdStrings = new ArrayList<>();
//    private BeanGenerator beanGeneratorSwitchBoxConfigurtion = new BeanGenerator();
//    private BeanGenerator beanGeneratorSwitchChild = new BeanGenerator();
//    private BeanGenerator beanGeneratorSwitch = new BeanGenerator();
//    private BeanGenerator beanGeneratorSwitchPath = new BeanGenerator();
//    private String[][] dataStrings;
//    private String beanDirectory = "";
//    private String boxName = "";
//    private String variant = "";
//    private String productNumber = UNSET;
//    private String revision = "";
//    private String beanId;
//
//    static {
//        //  
//        modelPropertiesBundle = ResourceBundle.getBundle("model");
//    }
//
//    /** 
//     * 
//     * */
//    public static List<Map<String, Object>> parseExcel(Workbook workbook) {
//        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
//        int excleRowLength = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();
//        String[] columnName = new String[excleRowLength]; //  
//        for (int i = 0; i < columnName.length; i++) { //
//            if (modelPropertiesBundle.containsKey((String.valueOf(i)))) {
//                columnName[i] = modelPropertiesBundle.getString(String.valueOf(i));
//            }
//        }
//        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) { //sheet  
//            HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(sheetIndex);
//            for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) { //row  
//                HSSFRow row = sheet.getRow(rowIndex);
//                Map<String, Object> map = new HashMap<String, Object>();
//                for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) { //cell  
//                    HSSFCell cell = row.getCell(cellIndex);
//                    if (columnName[cellIndex] != null && columnName[cellIndex].trim().length() > 0) { //  
//                        //  
//                        map.put(columnName[cellIndex].trim(), getCellValue(cell));
//                    }
//                }
//                result.add(map);
//            }
//        }
//        System.out.println(JSON.toJSONString("List<>=" + result));
//        result.forEach((e) -> System.out.print(e.toString()));
//        return result;
//    }
//
//    private void parseExcelCustom(Workbook workbook, String path, String sheetName)
//            throws IOException {
//        List<String> switchParentName = new ArrayList<>();
//        System.out.println(workbook.getSheetName(1));
//        String head = getCellValue(workbook.getSheetAt(0).getRow(0).getCell(0));
//        setXmlName(path, head);
//        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) { //sheet  
//            HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(sheetIndex);
//            for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) { //row  
//                if (rowIndex == 2)
//                    continue;
//                HSSFRow row = sheet.getRow(rowIndex);
//                List<String> switchPaths = new ArrayList<>();
//                for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) { //cell  
//                    HSSFCell cell = row.getCell(cellIndex);
//                    String cellValue = getCellValue(cell);
//                    setProductNumber(cellValue);
//                    if (rowIndex == 1)
//                        switchParentName.add(cellIndex, getCellValue(cell));
//                    else if (rowIndex >= 3) // from third line to collect SPD path data
//                        switchPaths.add(cellIndex, cellValue);
//                }
//                formatBeanGeneratorSwitchPath(switchPaths, switchParentName);
//                formatBeanGeneratorSwitchBoxConfigurtionHead();
//                formatBeanGeneratorSwitchBoxConfigurtionRefs(switchPaths);
//            }
//        }
//        formatBeanGeneratorSwitch(switchParentName);
//        formatBeanGeneratorSwitchBoxConfigurtionEnd();
//        GenerateBean.generatNewBean("C:\\Users\\EKUIWAG\\Desktop\\bean-head.xml", beanDirectory);
//        GenerateBean.writeBeans(beanDirectory, beanGeneratorSwitchBoxConfigurtion.getBeanString());
//        GenerateBean.writeBeans(beanDirectory, beanGeneratorSwitchPath.getBeanString());
//        GenerateBean.writeBeans(beanDirectory, beanGeneratorSwitchChild.getBeanString());
//        GenerateBean.writeBeans(beanDirectory, beanGeneratorSwitch.getBeanString());
//        GenerateBean.writeEndBeans(beanDirectory);
//    }
//
//    private void setXmlName(String path, String cellValue) {
//        boxName = cellValue.toString().trim().substring(1, cellValue.length() - 1).split("]")[0].replace(":", "-");
//        String firstBoxName = boxName.split("-")[0];
//        String[] name = firstBoxName.split("\\.");
//        beanId = firstBoxName.replace(".", "_");
//        beanDirectory = path + beanId + ".xml";
//        variant = name[1];
//        revision = name[name.length - 1];
//    }
//
//    private void setProductNumber(String cellValue) {
//        if (!cellValue.isEmpty() && cellValue.substring(0, 1).equals("#") && productNumber.equals(UNSET)) {
//            productNumber = cellValue.substring(1).trim();
//            //            formatHead();
//        }
//    }
//
//    private void formatBeanGeneratorSwitch(List<String> switchPaths) {
//        if (switchPaths.isEmpty())
//            return;
//        for (int i = 1; i < switchPaths.size(); i++) {
//            String path = switchPaths.get(i);
//            beanGeneratorSwitch.addTabIncent(1);
//            beanGeneratorSwitch
//                    .addString(String
//                            .format("<bean id=\"%s\" class=\"com.ericsson.radio.test.ctr.helpers.arptoinstrumentpath.configuration.Switch\">\n",
//                                    "parent" + path));
//            beanGeneratorSwitch.addTabIncent(2);
//            beanGeneratorSwitch.addString(String.format("<property name=\"name\" value=\"%s\" />\n", path));
//            beanGeneratorSwitch.addTabIncent(2);
//            beanGeneratorSwitch.addString(String.format("<property name=\"order\" value=\"%s\" />\n", i));
//            beanGeneratorSwitch.addTabIncent(1);
//            beanGeneratorSwitch.addString(String.format("</bean>\n\n"));
//        }
//    }
//
//    private void formatBeanGeneratorSwitchPath(List<String> switchPaths, List<String> switchParentName) {
//        if (switchPaths.isEmpty())
//            return;
//        for (int i = 0; i < switchPaths.size(); i++) {
//            String path = switchPaths.get(i);
//            switch (i) {
//            case 0:
//                beanGeneratorSwitchPath.addTabIncent(1);
//                beanGeneratorSwitchPath
//                        .addString(String
//                                .format("<bean id=\"%s\" class=\"com.ericsson.radio.test.ctr.helpers.arptoinstrumentpath.configuration.SwitchPath\">\n",
//                                        path));
//                beanGeneratorSwitchPath.addTabIncent(2);
//                beanGeneratorSwitchPath.addString("<property name=\"switches\">\n");
//                beanGeneratorSwitchPath.addTabIncent(3);
//                beanGeneratorSwitchPath.addString("<list>\n");
//                break;
//            default:
//                beanGeneratorSwitchPath.addTabIncent(4);
//                if (Double.parseDouble(path) == 0) {
//                    String referenceIdForParent = "parent" + switchParentName.get(i);
//                    beanGeneratorSwitchPath.addString(String.format("<ref bean=\"%s\" />\n", referenceIdForParent));
//                } else if (Double.parseDouble(path) > 0) {
//                    String switchParent = switchParentName.get(i);
//                    int position = (int) Double.parseDouble(path);
//                    String referenceId = switchParentName.get(i) + "-" + position;
//                    beanGeneratorSwitchPath.addString(String.format("<ref bean=\"%s\" />\n", referenceId));
//                    formatBeanGeneratorSwitchPathChild(referenceId, switchParent, position);
//                } else {
//                    throw new RuntimeException("Unsupport value : " + path);
//                }
//                break;
//            }
//        }
//        beanGeneratorSwitchPath.addTabIncent(3);
//        beanGeneratorSwitchPath.addString("</list>\n");
//        beanGeneratorSwitchPath.addTabIncent(2);
//        beanGeneratorSwitchPath.addString("</property>\n");
//        beanGeneratorSwitchPath.addTabIncent(1);
//        beanGeneratorSwitchPath.addString("</bean>\n\n");
//        //        System.out.println(beanGeneratorSwitchPath.getBeanString());
//    }
//
//    private void formatBeanGeneratorSwitchPathChild(String referenceId, String switchParent, int position) {
//        if (!referenceIdStrings.contains(referenceId)) {
//            //                        beanGeneratorSwitchChild.generateSwitchChildReference(switchParent, referenceId, position);
//            beanGeneratorSwitchChild.addTabIncent(1);
//            beanGeneratorSwitchChild.addString(String.format("<bean id=\"%s\" parent=\"%s\">\n",
//                    referenceId, "parent" + switchParent));
//            beanGeneratorSwitchChild.addTabIncent(2);
//            beanGeneratorSwitchChild.addString(String.format(
//                    "<property name=\"position\" value = \"%d\" />\n", position));
//            beanGeneratorSwitchChild.addTabIncent(1);
//            beanGeneratorSwitchChild.addString(String.format("</bean>\n\n"));
//            referenceIdStrings.add(referenceId);
//        }
//    }
//
//    private void formatBeanGeneratorSwitchBoxConfigurtionRefs(List<String> switchPaths) {
//        if (switchPaths.isEmpty())
//            return;
//        //        beanGeneratorSwitchBoxConfigurtion.formatBeanSwitchBoxConfigurationClass(switchPaths.get(0));
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(4);
//        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<ref bean=\"%s\" />\n", switchPaths.get(0)));
//
//    }
//
//    //    private void formatBeanGeneratorSwitchChild(List<String> switchPaths, List<String> switchParentName) {
//    //        if (switchPaths.isEmpty())
//    //            return;
//    //        for (int i = 0; i < switchPaths.size(); i++) {
//    //            String path = switchPaths.get(i);
//    //            if (i >0) {
//    //                String switchParent = switchParentName.get(i);
//    //                int position = (int) Double.parseDouble(path);
//    //                String referenceId = switchParentName.get(i) + "-" + position;
//    //                if (!referenceIdStrings.contains(referenceId)) {
//    //                    beanGeneratorSwitchChild.generateSwitchChildReference(switchParent, referenceId, position);
//    //                    referenceIdStrings.add(referenceId);
//    //                }
//    //            }
//    //        }
//    //    }
//
//    private void formatBeanGeneratorSwitchBoxConfigurtionHead() {
//        if (!beanGeneratorSwitchBoxConfigurtion.getBeanString().isEmpty())
//            return;
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(1);
//        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<!-- %s -->\n", boxName));
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(1);
//        beanGeneratorSwitchBoxConfigurtion
//                .addString(String
//                        .format("<bean id=\"%s\" class=\"com.ericsson.radio.test.ctr.helpers.arptoinstrumentpath.configuration.SwitchBoxConfiguration\">\n",
//                                beanId));
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
//        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<property name=\"variant\" value=\"%s\" />\n",
//                variant));
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
//        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<property name=\"revision\" value=\"%s\" />\n",
//                revision));
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
//        beanGeneratorSwitchBoxConfigurtion.addString(String.format(
//                "<property name=\"productNumber\" value=\"%s\" />\n", productNumber));
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
//        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<property name=\"switchPaths\">\n"));
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(3);
//        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<list>\n"));
//    }
//
//    private void formatBeanGeneratorSwitchBoxConfigurtionEnd() {
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(3);
//        beanGeneratorSwitchBoxConfigurtion.addString("</list>\n");
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
//        beanGeneratorSwitchBoxConfigurtion.addString("</property>\n");
//        beanGeneratorSwitchBoxConfigurtion.addTabIncent(1);
//        beanGeneratorSwitchBoxConfigurtion.addString("</bean>\n\n");
//    }
//
//    private static String getCellValue(Cell cell) {
//        String value = "";
//        if (cell != null) {
//            switch (cell.getCellType()) {
//            case Cell.CELL_TYPE_NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
//                    value = sdf.format(date);
//                } else {
//                    Double data = cell.getNumericCellValue();
//                    value = data.toString();
//                }
//                break;
//            case Cell.CELL_TYPE_STRING:
//                value = cell.getStringCellValue();
//                break;
//            case Cell.CELL_TYPE_BOOLEAN:
//                Boolean data = cell.getBooleanCellValue();
//                value = data.toString();
//                break;
//            case Cell.CELL_TYPE_ERROR:
//                System.out.println("cell type error");
//                break;
//            case Cell.CELL_TYPE_FORMULA:
//                value = String.valueOf(cell.getNumericCellValue());
//                if (value.equals("NaN")) {
//                    value = cell.getStringCellValue().toString();
//                }
//                break;
//            case Cell.CELL_TYPE_BLANK:
//                System.out.println("null content");
//                break;
//            default:
//                value = cell.getStringCellValue().toString();
//                break;
//            }
//        }
//        return value;
//    }
//
//    @DataProvider(name = "data")
//    private static Object[][] data() {
//        return new Object[][] {
//                //                {"ECU"}
//                { "BCCU01" }, { "BCCU02" }, { "BCCU03" },
//                { "BFU01" }, { "BFU02" }, { "BFU03" }, { "BFU04" }, { "BFU05" }, { "BFU06" },
//                { "BPAU01" }, { "BPAU02" },
//                { "BTDF01" },
//                { "BTDM01" },
//                { "ECU01" }, { "ECU02" }, { "ECU03" },
//                { "FPMU01" },
//                { "LPMU01" }, { "LPMU02" }, { "LPMU03" }, { "LPMU04" },
//                { "RCU01" },
//                { "RMU01" },
//                { "TFU01" }
//        };
//    }
//
//    @Test
//    //    (dataProvider = "data", priority = 1)
//    @Parameters({ "fileName" })
//    public void test(String fileName) throws Exception {
//        //        String fileName = "NGTE";
//        String excelPath = "C:\\Users\\EKUIWAG\\Desktop\\NGTE\\";
//        String beanPath = "C:\\Users\\EKUIWAG\\Desktop\\NGTE\\xml\\";
//        String excelDirectory = excelPath + fileName + ".xls";
//        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelDirectory));
//        parseExcelCustom(workbook, beanPath, "");
//
//    }
//
//}
