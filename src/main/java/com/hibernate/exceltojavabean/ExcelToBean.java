package com.hibernate.exceltojavabean;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExcelToBean
{

    private static final String UNSET = "Unset";
    private static ResourceBundle modelPropertiesBundle;
    private String beanString = "";
    private List<String> referenceIdStrings = new ArrayList<>();
    private List<String> referenceIdOfSwitchPaths = new ArrayList<>();
    private int index = 0;
    private String boxNameString = UNSET;
    private String beanSwitchBoxConfigurtion = "";
    private String beanChildString = "";
    private String beanSwitchString = "";

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

    private List<Map<String, Object>> parseExcelCustom(Workbook workbook, String beanDirectory) {
        List<String> switchParentName = new ArrayList<>();
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
            for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) { //row  
                if (rowIndex == 2)
                    continue;
                HSSFRow row = sheet.getRow(rowIndex);
                Map<String, Object> map = new HashMap<String, Object>();
                List<String> switchPaths = new ArrayList<>();
                for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) { //cell  
                    HSSFCell cell = row.getCell(cellIndex);
                    String cellValue = getCellValue(cell);
                    if (boxNameString.equals(UNSET)) {
                        boxNameString = cellValue;
                        formatHead();
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
        GenerateBean.writeBeans(beanDirectory, beanSwitchBoxConfigurtion);
        GenerateBean.writeBeans(beanDirectory, beanString);
        GenerateBean.writeBeans(beanDirectory, beanChildString);
        GenerateBean.writeBeans(beanDirectory, beanSwitchString);
        System.out.println(JSONObject.toJSONString("List<>=" + result));
        return result;
    }

    private void formatSwitchPathReference(List<String> switchPaths, List<String> switchParentName) {
        //        <bean id="ATT10" class="com.ericsson.radio.test.ctr.helpers.instrument.configuration.SwitchPath">
        //        <property name="switches">
        //            <list>
        //                <ref bean="parentS21" />
        //                <ref bean="parentS45" />
        //                <ref bean="parentS02" />
        //                <ref bean="parentS46" />
        //                <ref bean="parentS23" />
        //                <ref bean="S47-1" />
        //                <ref bean="S48-1" />
        //                <ref bean="parentS01" />
        //                <ref bean="parentS22" />
        //                <ref bean="parentS41" />
        //                <ref bean="parentS42" />
        //                <ref bean="parentS03" />
        //                <ref bean="parentS04" />
        //            </list>
        //        </property>
        //    </bean>

        for (int i = 0; i < switchPaths.size(); i++) {
            String path = switchPaths.get(i);
            switch (i) {
            case 0:
                formatBeanSwitchBoxConfigurationClass(path);
                firstFormatSwitchPathClass(path);
                formatStringOneTabIncent(2);
                formatString("<property name=\"switches\">");
                formatStringOneTabIncent(3);
                formatString("<list>");
                break;
            default:
                //                if (isFirst)
                //                    isFirst = false;
                //                else
                formatStringOneTabIncent(4);
                if (Double.parseDouble(path) == 0) {
                    String referenceIdForParent = "parent" + switchParentName.get(i);
                    beanString += String.format("<ref bean=\"%s\" />", referenceIdForParent);
                    formatStringNewLine();
                } else if (Double.parseDouble(path) > 0) {
                    String switchParent = switchParentName.get(i);
                    int position = (int) Double.parseDouble(path);
                    String referenceId = switchParentName.get(i) + "-" + position;
                    beanString += String.format("<ref bean=\"%s\" />", referenceId);
                    formatStringNewLine();
                    if (!referenceIdStrings.contains(referenceId)) {
                        beanChildString += generateSwitchChildReference(switchParent, referenceId, position);

                        referenceIdStrings.add(referenceId);
                    }
                } else {
                    throw new RuntimeException("Unsupport value : " + path);
                }
                break;
            }
        }
        formatStringOneTabIncent(3);
        formatString("</list>");
        formatStringOneTabIncent(2);
        formatString("</property>");
        formatStringOneTabIncent();
        formatString("</bean>");
        formatStringNewLine();
        System.out.println(beanString);
    }

    private void formatBeanSwitchBoxConfigurationClass(String path) {
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 4);
        beanSwitchBoxConfigurtion += formatSwitchPaths(path);
    }

    private void formatHead() {
        beanSwitchBoxConfigurtion = formatStringOneTabIncent("", 1);
        beanSwitchBoxConfigurtion += String
                .format("<bean id=\"%s\" class=\"com.ericsson.radio.test.ctr.helpers.instrument.configuration.SwitchBoxConfiguration\">",
                        boxNameString);
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 2);
        beanSwitchBoxConfigurtion += String.format("<property name=\"variant\" value = \"\" />");
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 2);
        beanSwitchBoxConfigurtion += String.format("<property name=\"revision\" value = \"\" />");
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 2);
        beanSwitchBoxConfigurtion += String.format("<property name=\"productNumber\" value = \"\" />");
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 2);
        beanSwitchBoxConfigurtion += String.format("<property name=\"switchPaths\">");
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 3);
        beanSwitchBoxConfigurtion += String.format("<list>");
        //        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        //        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 4);
    }

    private String formatSwitchPaths(String path) {
        return String.format("<ref bean=\"%s\">", path);

    }

    private void formatEnd() {
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 3);
        beanSwitchBoxConfigurtion += "</list>";
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 2);
        beanSwitchBoxConfigurtion += "</property>";
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringOneTabIncent(beanSwitchBoxConfigurtion, 1);
        beanSwitchBoxConfigurtion += "</bean>";
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
        beanSwitchBoxConfigurtion = formatStringNewLine(beanSwitchBoxConfigurtion);
    }

    private String generateSwitchChildReference(String switchParent, String referenceId, int position) {
        String referenceBeanString = formatStringOneTabIncent("", 1);
        String parentId = "parent" + switchParent;
        referenceBeanString += String.format("<bean id=\"%s\" parent=\"parentS21\">", referenceId, parentId);
        referenceBeanString = formatStringNewLine(referenceBeanString);
        referenceBeanString = formatStringOneTabIncent(referenceBeanString, 2);
        referenceBeanString += String.format("<property name=\"position\" value = \"%d\" />", position);
        referenceBeanString = formatStringNewLine(referenceBeanString);
        referenceBeanString = formatStringOneTabIncent(referenceBeanString, 1);
        referenceBeanString += String.format("</bean>");
        referenceBeanString = formatStringNewLine(referenceBeanString);
        referenceBeanString = formatStringNewLine(referenceBeanString);
        return referenceBeanString;
    }

    private void firstFormatSwitchPathClass(String path) {
        beanString += String
                .format("    <bean id=\"%s\" class=\"com.ericsson.radio.test.ctr.helpers.instrument.configuration.SwitchPath\">",
                        path);
        formatStringNewLine();
    }

    private void formatString(String formatString) {
        beanString = formatString(beanString, formatString);
    }

    private String formatString(String beanString, String formatString) {
        String newString = beanString;
        newString += formatString;
        return formatStringNewLine(newString);
    }

    private void formatStringOneTabIncent() {
        formatStringOneTabIncent(1);
    }

    private void formatStringOneTabIncent(int incentTimes) {
        beanString = formatStringOneTabIncent(beanString, incentTimes);
    }

    private String formatStringOneTabIncent(String beanString, int incentTimes) {
        String newString = beanString;
        for (int i = 1; i <= incentTimes; i++)
            newString += "    ";
        return newString;
    }

    private void formatStringNewLine() {
        beanString += "\n";
    }

    private String formatStringNewLine(String beanString) {
        return beanString += "\n";
    }

    private void formatSwitchPathParent(List<String> switchPaths) {
        //        <bean id="parentS21" class="com.ericsson.radio.test.ctr.helpers.instrument.configuration.Switch">
        //        <property name="name" value="S21" />
        //        <property name="order" value="1" />
        //    </bean>
        for (int i = 0; i < switchPaths.size(); i++) {
            if (i == 0)
                continue;
            String path = switchPaths.get(i);
            formatSwitchClass(path, i);
            System.out.println(beanString);
        }
    }

    private void formatSwitchClass(String path, int i) {
        beanSwitchString += String
                .format("    <bean id=\"parent%s\" class=\"com.ericsson.radio.test.ctr.helpers.instrument.configuration.Switch\">",
                        path);
        beanSwitchString = formatStringNewLine(beanSwitchString);
        beanSwitchString += String.format("        <property name=\"name\" value=\"%s\" />", path);
        beanSwitchString = formatStringNewLine(beanSwitchString);
        beanSwitchString += String.format("        <property name=\"order\" value=\"%s\" />", i);
        beanSwitchString = formatStringNewLine(beanSwitchString);
        beanSwitchString += String.format("    </bean>");
        beanSwitchString = formatStringNewLine(beanSwitchString);
        beanSwitchString = formatStringNewLine(beanSwitchString);
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
                { "BCCU01" }, { "BCCU02" }, { "BCCU03" }, { "BCCU02" }, { "BFU01" }, { "BFU02" }, { "BFU03" },
                { "BFU04" }, { "BFU05" }, { "BFU06" }, { "BPAU01" }, { "BPAU02" }, { "TFU01" } };
    }

    @Test(dataProvider = "data")
    public void test(String fileName) throws Exception {
        String path = "C:\\Users\\EKUIWAG\\Desktop\\NGTE\\";
        String excelDirectory = path + fileName + ".xls";
        String beanDirectory = path + fileName + ".xml";
        FileInputStream input = new FileInputStream(excelDirectory);
        HSSFWorkbook workbook = new HSSFWorkbook(input);
        GenerateBean.generatNewBean("C:\\Users\\EKUIWAG\\Desktop\\bean-head.xml", beanDirectory);
        parseExcelCustom(workbook, beanDirectory);
        GenerateBean.writeEndBeans(beanDirectory);
        referenceIdOfSwitchPaths.forEach(System.out::println);
    }

}
