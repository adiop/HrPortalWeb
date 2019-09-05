package com.warn.hr.persistence.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ExcelUtils {

    public static List<Row> readFile(byte [] fileBytes) {

        List<Row> rows = readFileXLS(fileBytes);

        if(rows.isEmpty()) {
            rows = readFileXLSX(fileBytes);
        }

        if(rows.isEmpty()) {
            throw new RuntimeException("Couldn't convert the bytes to a list of rows");
        }

        return rows;
    }

    public static <T> List<T> readFile(byte [] fileBytes, ExcelLineMapper<T> mapper) {
        return readFileStream(fileBytes, mapper).collect(Collectors.toList());
    }

    public static Stream<Row> readFileStream(byte [] fileBytes) {
        return readFile(fileBytes).parallelStream();
    }

    public static <T> Stream<T> readFileStream(byte [] fileBytes, ExcelLineMapper<T> mapper) {
        return readFileStream(fileBytes).map(row -> mapper.map(row));
    }

    public static List<Row> readFileXLS(byte [] fileBytes) {

        List<Row> rows = new ArrayList<>(300);

        try(ByteArrayInputStream file = new ByteArrayInputStream(fileBytes)) {

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                rows.add(rowIterator.next());
            }

        } catch (Exception e) {
            log.error("readFileXLS - An error has occurred while trying to read the excel file " + fileBytes, e);
        }

        return rows;

    }

    public static List<Row> readFileXLSX(byte [] fileBytes) {

        List<Row> rows = new ArrayList<>(300);

        try(ByteArrayInputStream file = new ByteArrayInputStream(fileBytes)) {

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet  sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                rows.add(rowIterator.next());
            }

        } catch (Exception e) {
            log.error("readFileXLSX - An error has occurred while trying to read the excel file " + fileBytes, e);
        }

        return rows;

    }

}
