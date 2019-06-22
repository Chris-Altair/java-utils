package pers.fanjc.util;

import com.alibaba.fastjson.JSON;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.fanjc.annotation.ExcelHead;
import pers.fanjc.annotation.ExcelSheet;
import pers.fanjc.exception.ExcelException;
import pers.fanjc.exception.ExcelExceptionEnum;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static Workbook importExcel(String filePath){
        InputStream is = null;
        Workbook wb = null;
        if(!validateExcel(filePath)){
            throw new ExcelException(ExcelExceptionEnum.FILE_FORMAT_EXCEPTION);
        }
        try {
            is = new FileInputStream(filePath);
            //根据文件名判断文件是2003版本还是2007版本

            if (isExcel2007(filePath)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }

    /**
     * 获取excel全部的页脚
     *
     * @param wb
     * @return
     */
    public static List<Sheet> getSheets(Workbook wb) {
        List<Sheet> sheets = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            sheets.add(wb.getSheetAt(i));
        }
        return sheets;
    }

    /**
     * 生成 excelSheet:classPath 的map
     *
     * @param classPaths
     * @return
     * @throws ClassNotFoundException
     */
    public static Map<String, String> classExcelSheetMap(List<String> classPaths) throws ClassNotFoundException {
        Map<String, String> excelSheetMap = new HashMap<>();
        for (String classPath : classPaths) {
            Class c = Class.forName(classPath);
            if (!c.isAnnotationPresent(ExcelSheet.class))
                throw new ExcelException(ExcelExceptionEnum.NO_FIND_EXCEL_SHEET_EXCEPTION);
            ExcelSheet excelSheet = (ExcelSheet) c.getAnnotation(ExcelSheet.class);
            excelSheetMap.put(excelSheet.value(), classPath);
        }
        excelSheetMap.forEach((k, v) -> System.out.println("key:value = " + k + ":" + v));
        return excelSheetMap;
    }

    /**
     * 如果表头和entity元素的ExcelHead注解的值匹配，则给该注解对应的元素赋值，退出函数
     *
     * @param fields 实体entity对应的成员数组
     * @param name   excel列对应的列头
     * @param value  excel列对应的列值
     * @param entity 需要赋值的实体entity
     * @throws IllegalAccessException
     */
    public static void fieldsAssignment(Field[] fields, String name, String value, Object entity) throws IllegalAccessException {
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelHead.class))
                continue;
            ExcelHead excelHead = field.getAnnotation(ExcelHead.class);
            //如果表头=注解头，对应字段赋值
            if (name.equals(excelHead.value())) {
                boolean accessFlag = field.isAccessible();
                field.setAccessible(true);
                if(!"".equals(excelHead.dict())){
                    Map<String,String> map = (Map) JSON.parse(excelHead.dict());
                    if(map.containsKey(value)){
                        value = map.get(value);
                    }
                }
                //通过反射判断实体中的字段类型
                switch (field.getGenericType().toString()) {
                    case "class java.lang.String":
                        field.set(entity, value);
                        break;
                    case "class java.lang.Integer":
                        field.set(entity, Integer.parseInt(value));
                        break;
                    case "class java.lang.Double":
                        field.set(entity, Double.parseDouble(value));
                        break;
                    case "class java.lang.Long":
                        field.set(entity, Long.parseLong(value));
                        break;
                    case "class java.util.Date":
                        field.set(entity, DateUtil.getJavaDate(Double.parseDouble(value)));
                        break;
                    default:
                        field.set(entity, value);
                        break;
                }
                field.setAccessible(accessFlag);
                return;
            }
        }
    }

    /**
     * 循环sheet页并分析excel表，插入实体，入库
     * @param wb excel实体
     * @param classList excel对应的实体类路径列表
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void analyzeExcel(Workbook wb, List<String> classList) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<Sheet> sheets = getSheets(wb);//获取excel中所有的页脚
        Map<String, String> classPathMap = classExcelSheetMap(classList);//生成 excelSheet:classPath 的map
//        List<Long> times = new ArrayList<>();/////////////////////
        for (Sheet sheet : sheets) {
            String sheetName = sheet.getSheetName();
            int rowCount = sheet.getPhysicalNumberOfRows();//该sheet页对应的行数
            logger.info("开始读取{}页，总计{}条数据", new Object[]{sheetName,rowCount-1});
            //如果没有数据则跳到下一个sheet
            if (rowCount < 2){
                logger.info("{}页为空", sheetName);
                logger.info("{}页已读取完成", sheetName);
                continue;
            }
            //根据页脚查找对应类的全路径
            String className = classPathMap.get(sheetName);
            //根据类的全路径实例化相应的类
            Class c = Class.forName(className);
            logger.info("className = {}", c.getName());
            Row head = sheet.getRow(0);//获取表头
            for (int j = 1; j < rowCount; j++) {//获取每行
//                Long begintime = System.currentTimeMillis();///////////////
                logger.info("第{}行开始读取", j);
                Row row = sheet.getRow(j);
                Object entity = c.newInstance();
                Field[] fields = entity.getClass().getDeclaredFields();
                for (int k = 0; k < head.getPhysicalNumberOfCells(); k++) {//获取每个单元格,以head的列数为准
                    String name = head.getCell(k).getStringCellValue();
                    //+判断是否是ExcelHead中包含的字段，是则继续，否则continue
                    Cell cell = row.getCell(k);
                    String value;
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        value = row.getCell(k).getStringCellValue();//这里全转化成string
                    } else
                        value = "";
                    logger.info("(name,value)=({},{})", new Object[]{name, value});
                    fieldsAssignment(fields, name, value, entity);//赋值
                }
                logger.info("entity = {}", JSON.toJSONString(entity));//entity对应的类型是运行时类型
                //+这里可以执行对实体的操作
                logger.info("第{}行读取完成\n", j);
//                Long endtime = System.currentTimeMillis();////////////////////
//                times.add(endtime-begintime);
            }
//            System.out.println(times.toString());
            logger.info("{}页已读取完成\n", sheetName);
//            break;

        }
        logger.info("excel已读取完毕");
    }


    /**
     * 是否是2003的excel，返回true是2003
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是2007的excel，返回true是2007
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 验证EXCEL文件
     */
    public static boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Workbook wb = importExcel("/home/amadeus/文档/templete.xlsx");
        List<String> classList = new ArrayList<>();
        //需添加的实体路径
        classList.add("pers.fanjc.domain.BookDO");
        classList.add("pers.fanjc.domain.UserDO");
        try {
            analyzeExcel(wb, classList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }

}
