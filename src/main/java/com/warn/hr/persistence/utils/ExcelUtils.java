package com.warn.hr.persistence.utils;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExcelUtils {

    public static List<Row> readFile(byte [] fileBytes) {

        List<Row> rows = new LinkedList<>();

        try(ByteArrayInputStream file = new ByteArrayInputStream(fileBytes)) {

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                rows.add(rowIterator.next());
            }

        } catch (Exception e) {
            throw new RuntimeException("An error has occurred while trying to read the excel file " + fileBytes, e);
        }

        return rows;

    }
}
