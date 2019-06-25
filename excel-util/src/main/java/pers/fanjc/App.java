package pers.fanjc;

import org.apache.poi.ss.usermodel.Workbook;
import pers.fanjc.util.ExcelUtils;

public class App {
    public static void main(String[] args) {
        Workbook wb = ExcelUtils.importExcel(args[0]);
        String packageName = "pers.fanjc.domain";
        try {
            ExcelUtils.analyzeExcel(wb, packageName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
