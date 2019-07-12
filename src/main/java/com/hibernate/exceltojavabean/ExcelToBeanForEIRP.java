package com.hibernate.exceltojavabean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExcelToBeanForEIRP {

    private static final String UNSET = "Unset";
    private static final String ANGEL_3GPP = "angel_3GPP";
    private static final String ANGEL_F = "angle_f";
    private static final String EIRP_LOWER_VALUE = "eirpLowerValue";
    private static final String EIRP_UPPER_VALUE = "eirpUpperValue";
    private static final String OFFSET = "OFFSET";
    private static final String RAT = "rat";
    private static final String RADIO_TYPE = "radioType";
    private static final String EIRP_CLASS = "EirpConfigurationClass";
    private static final String SEPARATOR_SPACE = " ";
    private static final String SEPARATOR_COMMA = ",";
    private static final String SEPARATOR_UNDERLINE = "_";
    private static final String SYMBOL_ADD_AND_SUBSTRACT = "±";
    private static final String SYMBOL_ADD = "+";
    private static final String SYMBOL_SUBSTRACT = "-";
    private BeanGenerator beanGen = new BeanGenerator();
    HSSFWorkbook workbook;
    List<String> refs = new ArrayList<>();

    @Test
    @Parameters({ "sheetName", "startRowIndex", "endRowIndex", "beanFileName" })
    public void testGenrateBean (
            @Optional("9.2.1.2 Target EIRP, AIR 6468") String sheetName,
            @Optional("2") int startRowIndex,
            @Optional("19") int endRowIndex,
            @Optional("LimitsForEirpRange.xml") String beanFileName) throws Exception {
        String beanPath = "C:\\Users\\EKUIWAG\\Desktop\\NGTE\\limits\\xml\\" + sheetName + "\\";
        HSSFSheet sheet = getSheet(workbook, sheetName);
        int startRowNum = getStartRowNum(startRowIndex);
        int cellNum = sheet.getRow(startRowNum + 1).getPhysicalNumberOfCells();
        List<List<String>> datas = generateList(sheet, startRowNum, endRowIndex, cellNum);
        List<BeanGenerator> beanGens = transCellValueToBeanProperty(datas, "EIRP_");
        GenerateBean.writeBeans(beanPath, beanFileName, beanGen.buildHead(), false);
        beanGens.forEach(bean -> {
            bean.build();
            try {
                GenerateBean.writeBeans(beanPath, beanFileName, bean.getBeanString(), true);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        GenerateBean.writeBeans(beanPath, beanFileName, beanGen.buildEnd(), true);            
    }
    
    @BeforeTest
    @Parameters({ "excelDirectory" })
    protected void beforeClass(@Optional("C:\\Users\\EKUIWAG\\Desktop\\NGTE\\NGTE.xls") String excelDirectory)
            throws FileNotFoundException, IOException {
        workbook = new HSSFWorkbook(new FileInputStream(excelDirectory));
    }

    private List<BeanGenerator> transCellValueToBeanProperty(List<List<String>> datas, String beanId) throws Exception {
        List<BeanGenerator> beanGens = new ArrayList<BeanGenerator>();
        for (int line = 3; line < datas.size(); line++) {
            List<String> row = datas.get(line);
            for (int i = 2; i < row.size() - 1; i++) {
                String angel1 = datas.get(1).get(i).replace("°", "");
                String angel2 = datas.get(2).get(i).replace("°", "");
                if (!angel1.isEmpty() || !angel2.isEmpty()) {
                    String rat = getRat(row.get(1));
                    String comment = row.get(row.size() - 1);
                    String eirp = row.get(i);
                    if (eirp.isEmpty()) {
                        eirp = datas.get(line - 1).get(i);
                    }
                    for (String radioType : getRadioType(comment)) {
                        BeanGenerator beanGen = new BeanGenerator();
                        beanGen.setBeanId(beanId + radioType);
                        beanGen.setClass(EIRP_CLASS);
                        beanGen.setProperty(RAT, rat);
                        List<Double> eirpRange = getEirpRange(eirp);
                        beanGen.setProperty(ANGEL_F, angel2);
                        beanGen.setProperty(ANGEL_3GPP, angel1);
                        beanGen.setProperty(EIRP_LOWER_VALUE, eirpRange.get(0).toString());
                        beanGen.setProperty(EIRP_UPPER_VALUE, eirpRange.get(1).toString());
                        beanGen.setProperty(RADIO_TYPE, radioType);
                        beanGens.add(beanGen);
                    }
                }
            }
            
        }
        return beanGens;
    }

    private List<Double> getEirpRange(String eirp) throws Exception {
        String eirpFormula = eirp.split(SEPARATOR_SPACE)[0].trim();
        Double lowerValue = 0d;
        Double upperValue = 0d;
        if (eirpFormula.contains(SYMBOL_ADD_AND_SUBSTRACT)) {
            Double level = Double.parseDouble(eirpFormula.split(SYMBOL_ADD_AND_SUBSTRACT)[0]);
            Double offset = Double.parseDouble(eirpFormula.split(SYMBOL_ADD_AND_SUBSTRACT)[1]);
            lowerValue = level - offset;
            upperValue = level + offset;
        } else if (eirpFormula.contains(SYMBOL_ADD)) {
            Double level = Double.parseDouble(eirpFormula.split(SYMBOL_ADD)[0]);
            Double offset = Double.parseDouble(eirpFormula.split(SYMBOL_ADD)[1]);
            lowerValue = level;
            upperValue = level + offset;
        } else if (eirpFormula.contains(SYMBOL_SUBSTRACT)) {
            Double level = Double.parseDouble(eirpFormula.split(SYMBOL_SUBSTRACT)[0]);
            Double offset = Double.parseDouble(eirpFormula.split(SYMBOL_SUBSTRACT)[1]);
            lowerValue = level - offset;
            upperValue = level;
        } else {
            throw new Exception("Wrong eirp range. [" + eirpFormula + "]");
        }
        List<Double> range = new ArrayList<Double>();
        range.add(lowerValue);
        range.add(upperValue);
        return range;
    }

    private String getRat(String eirpInfo) {
        String[] infoStrings = eirpInfo.split(SEPARATOR_COMMA);
        if (infoStrings.length == 1) {
            return "";
        }
        return infoStrings[1].trim();
    }

    private List<String> getRadioType(String comment) {
        List<String> radioBands = new ArrayList<String>(Arrays.asList(comment.split(SEPARATOR_COMMA)));
        String[] firstRadioTypeDatas = radioBands.get(0).split(SEPARATOR_SPACE);
        String radioHead = firstRadioTypeDatas[0].trim();
        String radioNumber = firstRadioTypeDatas[1].trim();
        radioBands.set(0, firstRadioTypeDatas[2]); /* remove the non-band content from the first element. */
        List<String> radioTypes = new ArrayList<String>();
        for (String band: radioBands) {
            radioTypes.add(radioHead + SEPARATOR_UNDERLINE + radioNumber + SEPARATOR_UNDERLINE + band.trim());
        }
        return radioTypes;
    }

    private List<List<String>> generateList(HSSFSheet sheet, int startRowNum, int endRowIndex, int cellNum) {
        List<List<String>> datas = new ArrayList<List<String>>();
        for (int rowIndex = startRowNum; rowIndex < getEndRowNum(endRowIndex, sheet); rowIndex++) { //row  
            List<String> rowDatas = new ArrayList<String>();
            for (int cellIndex = 0; cellIndex < cellNum; cellIndex++) { //cell  
                String cellValue = getCellValue(sheet.getRow(rowIndex).getCell(cellIndex));
                rowDatas.add(cellValue);
            }
            datas.add(rowDatas);
        }
        return datas;
    }

    private HSSFSheet getSheet(Workbook workbook, String sheetName) {
        HSSFSheet sheet;
        if (sheetName.isEmpty()) {
            sheet = (HSSFSheet) workbook.getSheetAt(0);
        } else {
            sheet = (HSSFSheet) workbook.getSheet(sheetName);
        }
        return sheet;
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
