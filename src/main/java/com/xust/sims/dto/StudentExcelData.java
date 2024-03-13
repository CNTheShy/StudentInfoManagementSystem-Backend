package com.xust.sims.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.util.Date;


@Data
@NoArgsConstructor
public class StudentExcelData {
    @ExcelProperty(value = "user name")
    private String id;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "name")
    private String name;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "nationality")
    private String nation;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "gender")
    private String sex;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "age")
    private String age;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "school status")
    @ColumnWidth(15)
    private String politicsStatus;

    @NotEmpty(message = "can not be empty")
    @ExcelProperty(value = "id number")
    @ColumnWidth(15)
    private String idCard;

    @NotEmpty(message = "can not be empty")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "format error")
    @ExcelProperty(value = "contact number")
    @ColumnWidth(15)
    private String phoneNum;

    @ExcelProperty(value = "e-mail")
    @NotEmpty(message = "can not be empty")
    @Email(message = "format error")
    private String email;

    @ExcelProperty(value = "academy")
    @NotEmpty(message = "can not be empty")
    private String academyName;
    @ExcelIgnore
    private Integer academyId;

    @ExcelProperty(value = "major")
    @NotEmpty(message = "can not be empty")
    private String majorName;
    @ExcelIgnore
    private Integer majorId;

    @ExcelProperty(value = "group")
    @NotEmpty(message = "can not be empty")
    private String className;
    @ExcelIgnore
    private Integer classId;

    @ExcelProperty(value = "enrollment time")
    @ColumnWidth(15)
    @DateTimeFormat("yyyy-MM-dd")
    @NotNull(message = "can not be empty")
    private Date createTime;

    @ExcelProperty(value = "description")
    @ColumnWidth(25)
    private String description;

    public StudentExcelData(String id, String name, String nation, String sex, String age, String politicsStatus,
                            String idCard, String phoneNum, String email, String academyName, String majorName,
                            String className, Date createTime) {
        this.id = id;
        this.name = name;
        this.nation = nation;
        this.sex = sex;
        this.age = age;
        this.politicsStatus = politicsStatus;
        this.idCard = idCard;
        this.phoneNum = phoneNum;
        this.email = email;
        this.academyName = academyName;
        this.majorName = majorName;
        this.className = className;
        this.createTime = createTime;
    }
}
