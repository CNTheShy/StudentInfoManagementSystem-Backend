package com.xust.sims.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xust.sims.dto.*;
import com.xust.sims.entity.Academy;
import com.xust.sims.entity.Student;
import com.xust.sims.entity.Teacher;
import com.xust.sims.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/web")
@Slf4j
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/teacher/getByAcademyId")
    @Secured({"ROLE_admin"})
    public List<Teacher> getTeacherByAcademyId(@RequestParam("academyId") Integer academyId) {
        return teacherService.getTeacherInfoByAcademyId(academyId);
    }

    @GetMapping("/teacher/personalInfo")
    //@Secured({"ROLE_admin", "ROLE_teacher", "ROLE_user"})
    @Secured({"ROLE_admin", "ROLE_teacher"})
    public Teacher queryPersonalData(Principal principal) {
        return teacherService.getTeacherInfoDetailsById(principal.getName());
    }

    @GetMapping("/academy/teacherInfo")
    @Secured({"ROLE_admin"})
    public List<Academy> getAcademyWithTeacherInfo() {
        return teacherService.getAcademyWithTeacherInfo();
    }

    @PostMapping("/teacher/info")
    @Secured({"ROLE_admin"})
    public PageInfo<Teacher> queryTeacherInfo(@RequestBody TeacherInfoQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<Teacher> teachers = teacherService.getTeacherByQueryInfo(query);
        return new PageInfo<>(teachers);
    }

    @PostMapping("/teacher/upload")
    @Secured({"ROLE_admin"})
    public RespBean uploadTeacherInfo(@Valid @RequestBody TeacherInsert teacherInsert, BindingResult result) {
        log.info("The received teacher information is：{}", teacherInsert);
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                log.error("Mistake as；{}", error);
            }
            return new RespBean(ResponseCode.ERROR);
        }
        teacherService.addOneTeacherInsertInfo(teacherInsert);
        return new RespBean(ResponseCode.SUCCESS);
    }

    @PostMapping("/teacher/update")
    @Secured({"ROLE_admin"})
    public RespBean updateTeacherInfo(@RequestBody @Valid Teacher teacher, BindingResult bindingResult) {
        log.info("The received Teacher information is：{}", teacher);
        if (bindingResult.hasErrors() || !StringUtils.isEmpty(teacherInfoIsEmpty(teacher))) {
            StringBuilder sb = new StringBuilder();
            sb.append(teacherInfoIsEmpty(teacher));
            for (ObjectError error : bindingResult.getAllErrors()) {
                sb.append(error.getDefaultMessage()).append(";");
            }
            return new RespBean(ResponseCode.ERROR, sb.toString());
        }
        teacherService.updateTeacherInfo(teacher);
        return new RespBean(ResponseCode.SUCCESS);
    }

    private String teacherInfoIsEmpty(Teacher teacher) {
        if (teacher.getAcademy().getId() == null || teacher.getMajor().getId() == null) {
            return "College, major, and class information cannot be empty";
        }
        return "";
    }
}
