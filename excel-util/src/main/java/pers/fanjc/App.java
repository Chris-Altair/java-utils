package pers.fanjc;

import org.apache.poi.ss.usermodel.Workbook;
import pers.fanjc.util.ExcelUtils;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Workbook wb = ExcelUtils.importExcel(args[0]);
        List<String> classList = new ArrayList<>();
        classList.add("pers.fanjc.domain.BookDO");
        classList.add("pers.fanjc.domain.UserDO");
        try {
            ExcelUtils.analyzeExcel(wb, classList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
