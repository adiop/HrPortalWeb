package com.warn.hr.persistence.utils;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface ExcelLineMapper<T> {

    public T map(Row row);

}
