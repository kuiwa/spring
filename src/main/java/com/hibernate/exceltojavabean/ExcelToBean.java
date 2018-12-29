package com.hibernate.exceltojavabean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExcelToBean {

    private static final String UNSET = "Unset";
    private List<String> referenceIdStrings = new ArrayList<>();
    private BeanGenerator beanGeneratorSwitchBoxConfigurtion = new BeanGenerator();
    private BeanGenerator beanGeneratorSwitchChild = new BeanGenerator();
    private BeanGenerator beanGeneratorSwitch = new BeanGenerator();
    private BeanGenerator beanGeneratorSwitchPath = new BeanGenerator();
    private String beanFileName = "";
    private String boxName = "";
    private String variant = "";
    private String productNumber = UNSET;
    private String revision = "";
    private String beanId;
    HSSFWorkbook workbook;
    List<String> refs = new ArrayList<>();

    @Test
    @Parameters({ "sheetName", "startRowIndex", "endRowIndex" })
    public void testNew(
            @Optional("BPAU") String sheetName,
            @Optional("14") int startRowIndex,
            @Optional("28") int endRowIndex) throws Exception {
        String beanPath = "C:\\Users\\EKUIWAG\\Desktop\\NGTE\\xml\\" + sheetName + "\\";
        parseExcelCustom(workbook, beanPath, sheetName, startRowIndex, endRowIndex);
    }

    @BeforeTest
    @Parameters({ "excelDirectory" })
    protected void beforeClass(@Optional("C:\\Users\\EKUIWAG\\Desktop\\NGTE\\NGTE.xls") String excelDirectory)
            throws FileNotFoundException, IOException {
        workbook = new HSSFWorkbook(new FileInputStream(excelDirectory));
    }

    private void parseExcelCustom(Workbook workbook, String path) throws IOException {
        parseExcelCustom(workbook, path, "", 0, 0);
    }

    private void parseExcelCustom(Workbook workbook, String path, String sheetName, int startRowIndex, int endRowIndex)
            throws IOException {
        List<String> switchParentName = new ArrayList<>();
        HSSFSheet sheet;
        if (sheetName.isEmpty()) {
            sheet = (HSSFSheet) workbook.getSheetAt(0);
        } else {
            sheet = (HSSFSheet) workbook.getSheet(sheetName);
        }
        int startRowNum = getStartRowNum(startRowIndex);
        String head = getCellValue(sheet.getRow(startRowNum).getCell(0));
        parseBoxName(head);
        int cellNum = sheet.getRow(startRowNum + 1).getPhysicalNumberOfCells();

        for (int rowIndex = startRowNum; rowIndex < getEndRowNum(endRowIndex, sheet); rowIndex++) { //row  
            if (rowIndex == startRowNum + 2)
                continue;
            HSSFRow row = sheet.getRow(rowIndex);
            List<String> switchPaths = new ArrayList<>();
            for (int cellIndex = 0; cellIndex < cellNum; cellIndex++) { //cell  
                String cellValue = getCellValue(row.getCell(cellIndex));
                setProductNumber(cellValue);
                if (rowIndex == startRowNum + 1)
                    switchParentName.add(cellIndex, cellValue);
                else if (rowIndex >= startRowNum + 3) // from third line to collect SPD path data
                    switchPaths.add(cellIndex, cellValue);
            }
            buildBeanGeneratorSwitchPath(switchPaths, switchParentName);
            if (!switchPaths.isEmpty())
                refs.add(beanId + "-" + switchPaths.get(0));
        }
        buildBeanGeneratorSwitch(switchParentName);
        buildBeanGeneratorSwitchBoxConfigurtion();
        beanGeneratorSwitchBoxConfigurtion.buildHead();
        //        GenerateBean.generatNewBean("C:\\Users\\EKUIWAG\\Desktop\\bean-head.xml", beanFileName);
        GenerateBean.writeBeans(path, beanFileName, beanGeneratorSwitchBoxConfigurtion.buildHead(), false);
        GenerateBean.writeBeans(path, beanFileName, beanGeneratorSwitchBoxConfigurtion.getBeanString(), true);
        GenerateBean.writeBeans(path, beanFileName, beanGeneratorSwitchPath.getBeanString(), true);
        GenerateBean.writeBeans(path, beanFileName, beanGeneratorSwitchChild.getBeanString(), true);
        GenerateBean.writeBeans(path, beanFileName, beanGeneratorSwitch.getBeanString(), true);
        GenerateBean.writeBeans(path, beanFileName, beanGeneratorSwitchBoxConfigurtion.buildEnd(), true);
    }

    private int getEndRowNum(int endRowIndex, HSSFSheet sheet) {
        if (endRowIndex > 0)
            return endRowIndex;
        return sheet.getPhysicalNumberOfRows();
    }

    private int getStartRowNum(int startRowIndex) {
        if (startRowIndex > 0)
            return startRowIndex - 1;
        return 0;
    }

    private void parseBoxName(String cellValue) {
        boxName = cellValue.toString().trim().substring(1, cellValue.length() - 1).split("]")[0].replace(":", "-");
        String firstBoxName = boxName.split("-")[0];
        String[] name = firstBoxName.split("\\.");
        beanId = firstBoxName.replace(".", "_");
        beanFileName = beanId + ".xml";
        variant = name[1];
        revision = name[name.length - 1];
    }

    private void setProductNumber(String cellValue) {
        if (!cellValue.isEmpty() && cellValue.substring(0, 1).equals("#") && productNumber.equals(UNSET)) {
            productNumber = cellValue.substring(1).trim();
        }
    }

    private void buildBeanGeneratorSwitch(List<String> switchPaths) {
        if (switchPaths.isEmpty())
            return;
        for (int i = 1; i < switchPaths.size(); i++) {
            String path = switchPaths.get(i);
            if (!path.isEmpty()) {
                beanGeneratorSwitch.setBeanId(beanId + "-" + "parent" + path);
                beanGeneratorSwitch.setClass(BeanGenerator.SWITCH);
                beanGeneratorSwitch.getProperties().put("name", path);
                beanGeneratorSwitch.getProperties().put("order", String.valueOf(i));
                beanGeneratorSwitch.build();                
            }
        }
    }

    private void buildBeanGeneratorSwitchPath(List<String> switchPaths, List<String> switchParentName) {
        if (switchPaths.isEmpty())
            return;
        for (int i = 0; i < switchPaths.size(); i++) {
            String path = switchPaths.get(i);
            if (i == 0) {
                beanGeneratorSwitchPath.setBeanId(beanId + "-" + path);
                continue;
            }
            String referenceId;
            if (path.isEmpty())
                continue;
            if (Double.parseDouble(path) == 0) {
                referenceId = "parent" + switchParentName.get(i);
            } else if (Double.parseDouble(path) > 0) {
                String switchParent = switchParentName.get(i);
                int position = (int) Double.parseDouble(path);
                referenceId = switchParentName.get(i) + "-" + position;
                formatBeanGeneratorSwitchPathChild(referenceId, switchParent, position);
            } else {
                throw new RuntimeException("Unsupport value : " + path);
            }
            beanGeneratorSwitchPath.getRefsOfSwitchPath().add(beanId + "-" + referenceId);
        }
        beanGeneratorSwitchPath.setClass(BeanGenerator.SWITCHPATH);
        beanGeneratorSwitchPath.setPropertyNameForList("switches");
        beanGeneratorSwitchPath.build();
        beanGeneratorSwitchPath.getRefsOfSwitchPath().clear();
    }

    private void formatBeanGeneratorSwitchPathChild(String referenceId, String switchParent, int position) {
        if (!referenceIdStrings.contains(referenceId)) {
            beanGeneratorSwitchChild.setBeanId(beanId + "-" + referenceId);
            beanGeneratorSwitchChild.getProperties().put("position", String.valueOf(position));
            beanGeneratorSwitchChild.buildParent(beanId + "-" + "parent" + switchParent);
            referenceIdStrings.add(referenceId);
        }
    }

    private void buildBeanGeneratorSwitchBoxConfigurtion() {
        if (!beanGeneratorSwitchBoxConfigurtion.getBeanString().isEmpty())
            return;
        beanGeneratorSwitchBoxConfigurtion.setBeanId(beanId);
        beanGeneratorSwitchBoxConfigurtion.setClass(BeanGenerator.SWITCHBOXCONFIGURATION);
        beanGeneratorSwitchBoxConfigurtion.setPropertyNameForList("switchPaths");
        beanGeneratorSwitchBoxConfigurtion.getProperties().put("variant", variant);
        beanGeneratorSwitchBoxConfigurtion.getProperties().put("revision", revision);
        beanGeneratorSwitchBoxConfigurtion.getProperties().put("productNumber", productNumber);
        beanGeneratorSwitchBoxConfigurtion.getRefs().addAll(refs);
        beanGeneratorSwitchBoxConfigurtion.build();

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

}
