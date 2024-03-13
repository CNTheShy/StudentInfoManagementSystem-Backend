package com.xust.sims.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import java.util.Date;


@Data
public class StudentExcelTemplateData {
    @ExcelProperty(value = "name")
    private String name;
    @ExcelProperty(value = "nationality")
    private String nation;
    @ExcelProperty(value = "gender")
    private String sex;
    @ExcelProperty(value = "age")
    private String age;
    @ExcelProperty(value = "school status")
    @ColumnWidth(15)
    private String politicsStatus;
    @ExcelProperty(value = "id number")
    @ColumnWidth(15)
    private String idCard;
    @ExcelProperty(value = "contact number")
    @ColumnWidth(15)
    private String phoneNum;
    @ExcelProperty(value = "e-mail")
    private String email;
    @ExcelProperty(value = "academy")
    private String academyName;
    @ExcelProperty(value = "major")
    private String majorName;
    @ExcelProperty(value = "group")
    private String className;
    @ExcelProperty(value = "enrollment time")
    @ColumnWidth(15)
    @DateTimeFormat("yyyy-MM-dd")
    private Date createTime;
}
