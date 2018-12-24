package com.hibernate.exceltojavabean;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.hpsf.Variant;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExcelToBean
{

    private static final String UNSET = "Unset";
    private static ResourceBundle modelPropertiesBundle;
    private List<String> referenceIdStrings = new ArrayList<>();
    private String boxNameString = UNSET;
    private BeanGenerator beanGeneratorSwitchBoxConfigurtion = new BeanGenerator();
    private BeanGenerator beanGeneratorSwitchChild = new BeanGenerator();
    private BeanGenerator beanGeneratorSwitch = new BeanGenerator();
    private BeanGenerator beanGeneratorSwitchPath = new BeanGenerator();
    private String[][] dataStrings;
    private String xmlNameString = "";
    private String beanId = "";
    private String variant = "";
    private String productNumber = UNSET;
    private String revision = "";

    static {
        //  
        modelPropertiesBundle = ResourceBundle.getBundle("model");
    }

    /** 
     * 
     * */
    public static List<Map<String, Object>> parseExcel(Workbook workbook) {
        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        int excleRowLength = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();
        String[] columnName = new String[excleRowLength]; //  
        for (int i = 0; i < columnName.length; i++) { //
            if (modelPropertiesBundle.containsKey((String.valueOf(i)))) {
                columnName[i] = modelPropertiesBundle.getString(String.valueOf(i));
            }
        }
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) { //sheet  
            HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(sheetIndex);
            for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) { //row  
                HSSFRow row = sheet.getRow(rowIndex);
                Map<String, Object> map = new HashMap<String, Object>();
                for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) { //cell  
                    HSSFCell cell = row.getCell(cellIndex);
                    if (columnName[cellIndex] != null && columnName[cellIndex].trim().length() > 0) { //  
                        //  
                        map.put(columnName[cellIndex].trim(), getCellValue(cell));
                    }
                }
                result.add(map);
            }
        }
        System.out.println(JSON.toJSONString("List<>=" + result));
        result.forEach((e) -> System.out.print(e.toString()));
        return result;
    }

    private List<Map<String, Object>> parseExcelCustom(Workbook workbook, String path, String sheetName)
            throws IOException {
        List<String> switchParentName = new ArrayList<>();
        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        int excleRowLength = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();
        System.out.println(workbook.getSheetName(1));
        String head = getCellValue(workbook.getSheetAt(0).getRow(0).getCell(0));
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) { //sheet  
            //            if (!workbook.getSheetName(sheetIndex).equals(sheetName))
            //                continue;
            HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(sheetIndex);
            for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) { //row  
                if (rowIndex == 2)
                    continue;
                HSSFRow row = sheet.getRow(rowIndex);
                Map<String, Object> map = new HashMap<String, Object>();
                List<String> switchPaths = new ArrayList<>();
                for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) { //cell  
                    HSSFCell cell = row.getCell(cellIndex);
                    String cellValue = getCellValue(cell);
                    //                    if (!matchBoxName(cellValue))
                    //                        continue;
                    if (boxNameString.equals(UNSET)) {
                        boxNameString = cellValue;
                        setXmlName(cellValue);
                        
                    }
                    if (rowIndex == 0) {
                        setProductNumber(cellValue);
                    }
                    switchPaths.add(cellIndex, cellValue);
                    if (rowIndex == 1) {
                        switchParentName.add(cellIndex, getCellValue(cell));
                        continue;
                    }
                }
                if (rowIndex == 1) {
                    formatSwitchPathParent(switchParentName);
                    continue;
                }
                if (rowIndex >= 3) // from third line to collect SPD path data
                    formatSwitchPathReference(switchPaths, switchParentName);
                result.add(map);
            }
        }
        formatEnd();
        String beanDirectory = path + beanId + ".xml";
        GenerateBean.generatNewBean("C:\\Users\\EKUIWAG\\Desktop\\bean-head.xml", beanDirectory);
        GenerateBean.writeBeans(beanDirectory, beanGeneratorSwitchBoxConfigurtion.getBeanString());
        GenerateBean.writeBeans(beanDirectory, beanGeneratorSwitchPath.getBeanString());
        GenerateBean.writeBeans(beanDirectory, beanGeneratorSwitchChild.getBeanString());
        GenerateBean.writeBeans(beanDirectory, beanGeneratorSwitch.getBeanString());
        GenerateBean.writeEndBeans(beanDirectory);
        //        System.out.println(JSONObject.toJSONString("List<>=" + result));
        return result;
    }

    private void setXmlName(String cellValue) {
        String boxNameString = cellValue.toString().trim().substring(1, cellValue.length() - 1).split("]")[0];        
        if (cellValue.contains(":")){
            beanId = boxNameString.replace(":", "-");
        } else {
            String[] name = boxNameString.split("\\.");
            beanId = boxNameString.replace(".", "_");
//            String string =beanId.substring(beanId.length()-1);
            variant = name[1];
            revision = name[name.length - 1];
            
        }
//        beanId = beanId.split("]")[0];
//        if (beanId.substring(beanId.length()-1).equals("]")){
//            beanId = beanId.substring(0, beanId.length()-1);
//        }
//        String firstNameString = cellValue.trim().substring(1, cellValue.length() - 1);
//        
//            xmlNameString = boxNameString.split(":")[0];            
//        } else 
//            xmlNameString = boxNameString;
//        else {
//            firstNameString = cellValue.trim().substring(1, cellValue.length() - 2);
//        }
    }

    private void setProductNumber(String cellValue) {
        if (!cellValue.isEmpty() && cellValue.substring(0, 1).equals("#") && productNumber.equals(UNSET)) {
            productNumber = cellValue.substring(1).trim();
            formatHead();
        }
    }

    private boolean matchBoxName(String cellValue) {
        // [ACU.2125.R1] \\D+. R\\d 
        String expReg = "\\[\\D+.[\\s\\S]+R\\d+[\\s\\S]?\\]";
        boolean m = cellValue.matches(expReg);
        return m;
    }

    private void formatSwitchPathReference(List<String> switchPaths, List<String> switchParentName) {
        for (int i = 0; i < switchPaths.size(); i++) {
            String path = switchPaths.get(i);
            switch (i) {
            case 0:
                beanGeneratorSwitchBoxConfigurtion.formatBeanSwitchBoxConfigurationClass(path);
                beanGeneratorSwitchPath.addStringSwitchPathClass(path);
                beanGeneratorSwitchPath.addTabIncent(2);
                beanGeneratorSwitchPath.addString("<property name=\"switches\">");
                beanGeneratorSwitchPath.addNewLine();
                beanGeneratorSwitchPath.addTabIncent(3);
                beanGeneratorSwitchPath.addString("<list>");
                beanGeneratorSwitchPath.addNewLine();
                break;
            default:
                beanGeneratorSwitchPath.addTabIncent(4);
                if (Double.parseDouble(path) == 0) {
                    String referenceIdForParent = "parent" + switchParentName.get(i);
                    beanGeneratorSwitchPath.addString(String.format("<ref bean=\"%s\" />", referenceIdForParent));
                    beanGeneratorSwitchPath.addNewLine();
                } else if (Double.parseDouble(path) > 0) {
                    String switchParent = switchParentName.get(i);
                    int position = (int) Double.parseDouble(path);
                    String referenceId = switchParentName.get(i) + "-" + position;
                    beanGeneratorSwitchPath.addString(String.format("<ref bean=\"%s\" />", referenceId));
                    beanGeneratorSwitchPath.addNewLine();
                    if (!referenceIdStrings.contains(referenceId)) {
                        beanGeneratorSwitchChild.generateSwitchChildReference(switchParent, referenceId, position);
                        referenceIdStrings.add(referenceId);
                    }
                } else {
                    throw new RuntimeException("Unsupport value : " + path);
                }
                break;
            }
        }
        beanGeneratorSwitchPath.addTabIncent(3);
        beanGeneratorSwitchPath.addListEnd();
        beanGeneratorSwitchPath.addNewLine();
        beanGeneratorSwitchPath.addTabIncent(2);
        beanGeneratorSwitchPath.addPropertyEnd();
        beanGeneratorSwitchPath.addNewLine();
        beanGeneratorSwitchPath.addTabIncent(1);
        beanGeneratorSwitchPath.addBeanEnd();
        beanGeneratorSwitchPath.addNewLine();
        beanGeneratorSwitchPath.addNewLine();
        System.out.println(beanGeneratorSwitchPath.getBeanString());
    }

    private void formatHead() {
        beanGeneratorSwitchBoxConfigurtion.addTabIncent(1);
        beanGeneratorSwitchBoxConfigurtion
                .addString(String
                        .format("<bean id=\"%s\" class=\"com.ericsson.radio.test.ctr.helpers.arptoinstrumentpath.configuration.SwitchBoxConfiguration\">",
                                beanId));
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<property name=\"variant\" value=\"%s\" />",
                variant));
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<property name=\"revision\" value=\"%s\" />",
                revision));
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<property name=\"productNumber\" value=\"%s\" />",
                productNumber));
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<property name=\"switchPaths\">"));
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
        beanGeneratorSwitchBoxConfigurtion.addTabIncent(3);
        beanGeneratorSwitchBoxConfigurtion.addString(String.format("<list>"));
    }

    private void formatEnd() {

        beanGeneratorSwitchBoxConfigurtion.addNewLine();

        beanGeneratorSwitchBoxConfigurtion.addTabIncent(3);
        beanGeneratorSwitchBoxConfigurtion.addListEnd();
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
        beanGeneratorSwitchBoxConfigurtion.addTabIncent(2);
        beanGeneratorSwitchBoxConfigurtion.addPropertyEnd();
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
        beanGeneratorSwitchBoxConfigurtion.addTabIncent(1);
        beanGeneratorSwitchBoxConfigurtion.addBeanEnd();
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
        beanGeneratorSwitchBoxConfigurtion.addNewLine();
    }

    private void formatSwitchPathParent(List<String> switchPaths) {
        for (int i = 0; i < switchPaths.size(); i++) {
            if (i == 0)
                continue;
            String path = switchPaths.get(i);
            beanGeneratorSwitch.addSwitchClass(path, i);
        }
    }

    private static String getCellValue(Cell cell) {
        String value = "";
        if (cell != null) {
            switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                    value = sdf.format(date);
                } else {
                    Double data = cell.getNumericCellValue();
                    value = data.toString();
                }
                break;
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                Boolean data = cell.getBooleanCellValue();
                value = data.toString();
                break;
            case Cell.CELL_TYPE_ERROR:
                System.out.println("cell type error");
                break;
            case Cell.CELL_TYPE_FORMULA:
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {
                    value = cell.getStringCellValue().toString();
                }
                break;
            case Cell.CELL_TYPE_BLANK:
                System.out.println("null content");
                break;
            default:
                value = cell.getStringCellValue().toString();
                break;
            }
        }
        return value;
    }

    @DataProvider(name = "data")
    private static Object[][] data() {
        return new Object[][] {
                //                {"ECU"}
                { "BCCU01" }, { "BCCU02" }, { "BCCU03" },
                { "BFU01" }, { "BFU02" }, { "BFU03" }, { "BFU04" }, { "BFU05" }, { "BFU06" },
                { "BPAU01" }, { "BPAU02" },
                { "BTDF01" },
                { "BTDM01" },
                { "ECU01" }, { "ECU02" }, { "ECU03" },
                { "FPMU01" },
                { "LPMU01" }, { "LPMU02" }, { "LPMU03" }, { "LPMU04" },
                { "RCU01" },
                { "RMU01" },
                { "TFU01" }
        };
    }

    @Test
    //    (dataProvider = "data", priority = 1)
    @Parameters({ "fileName" })
    public void test(String fileName) throws Exception {
        //        String fileName = "NGTE";
        String excelPath = "C:\\Users\\EKUIWAG\\Desktop\\NGTE\\";
        String beanPath = "C:\\Users\\EKUIWAG\\Desktop\\NGTE\\xml\\";
        String excelDirectory = excelPath + fileName + ".xls";
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelDirectory));
        parseExcelCustom(workbook, beanPath, "");

    }

}
