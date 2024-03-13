package com.xust.sims.web.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xust.sims.dto.*;
import com.xust.sims.entity.Score;
import com.xust.sims.entity.Student;
import com.xust.sims.entity.Teacher;
import com.xust.sims.service.ScoreInfoService;
import com.xust.sims.service.ScoreInfoServiceImpl;
import com.xust.sims.service.StudentInfoServiceImpl;
import com.xust.sims.utils.ScoreInfoInsertUtils;
import com.xust.sims.utils.StudentInfoInsertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequestMapping("/web")
@RestController
@Slf4j
public class ScoreInfoController extends BaseController{
    @Autowired
    private ScoreInfoService scoreInfoService;

    @GetMapping("/score/personalInfo")
    @Secured({"ROLE_admin"})
    public Score queryScoreData(Principal principal) {
        return scoreInfoService.getScoreInfoById(Long.valueOf(principal.getName()));
    }

    @PostMapping("/score/info")
    @Secured({"ROLE_admin"})
    public PageInfo<Score> queryScoreInfo(@RequestBody ScoreInfoQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<Score> scores = scoreInfoService.getScoreByQueryInfo(query);
        return new PageInfo<>(scores);
    }

    @PostMapping("/score/templateFile/download")
    @Secured({"ROLE_admin", "ROLE_teacher"})
    public void downloadTemplateFile(HttpServletResponse response) throws IOException {
        ScoreInfoInsertUtils.getTemplateFile(response);
    }

    @PostMapping("/scoreInfo/exportData")
    @Secured({"ROLE_admin"})
    public void exportScoreInfoData(HttpServletResponse response, @RequestBody ScoreInfoQuery query) throws IOException {
        List<Score> scores = scoreInfoService.getScoreByQueryInfo(query);
        ScoreInfoInsertUtils.getScoreInfoFile(response, scores, "Score information export file");
    }

    @PostMapping("/scoreInfo/exportData/accordingId")
    @Secured({"ROLE_admin"})
    public void exportScoreInfoDataByIds(@RequestParam("ids") int[] ids, HttpServletResponse response) throws IOException {
        log.info("The ids obtained are：{}", Arrays.toString(ids));
        List<Score> scoreList = scoreInfoService.getScoreByIds(ids);
        log.info("the size of studentList is：{}", scoreList.size());
        ScoreInfoInsertUtils.getScoreInfoFile(response, scoreList, "Partial grade information file");
    }

    @PostMapping("/scoreInfo/upload")
    @Secured({"ROLE_admin"})
    public RespBean uploadScoreInfo(@Valid @RequestBody ScoreInsert scoreInsert, BindingResult result) {
        log.info("The received score information is：{}", scoreInsert);
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                log.error("Mistake as；{}", error);
            }
            return new RespBean(ResponseCode.ERROR);
        }
        scoreInfoService.addOneScoreInsertInfo(scoreInsert);
        return new RespBean(ResponseCode.SUCCESS);
    }

    @PostMapping("/scoreInfo/fileUpload")
    @Secured({"ROLE_admin"})
    public RespBean uploadScoreInfoFile(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) throws Exception {
        List<ScoreExcelData> errorList = scoreInfoService.saveScoreExcelData(files);
        if (errorList.size() > 0) {
            HttpSession session = request.getSession();
            session.setAttribute("errorList", new ArrayList<>(errorList));
            ((ScoreInfoServiceImpl) scoreInfoService).getErrorList().clear();
            return new RespBean(ResponseCode.ERROR, errorList.size());
        }
        return new RespBean(ResponseCode.SUCCESS, errorList.size());
    }

    @PostMapping("/scoreInfo/errorFile")
    @Secured({"ROLE_admin"})
    public void downloadErrorFile(HttpServletRequest request, HttpServletResponse response) {
        @SuppressWarnings("unchecked")
        List<ScoreExcelData> errorList = (List<ScoreExcelData>) request.getSession().getAttribute("errorList");
        log.error("The error set is:{}", errorList);
        try {
            ScoreInfoInsertUtils.getErrorFile(response, errorList, "Error message file");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            request.getSession().removeAttribute("errorList");
        }
    }

    @PostMapping("/scoreInfo/update")
    @Secured({"ROLE_admin"})
    public RespBean updateScoreInfo(@RequestBody @Valid Score score, BindingResult bindingResult) {
        log.info("The Score information received is:{}", score);
        if (bindingResult.hasErrors() || !StringUtils.isEmpty(scoreInfoIsEmpty(score))) {
            StringBuilder sb = new StringBuilder();
            sb.append(scoreInfoIsEmpty(score));
            for (ObjectError error : bindingResult.getAllErrors()) {
                sb.append(error.getDefaultMessage()).append(";");
            }
            return new RespBean(ResponseCode.ERROR, sb.toString());
        }
        scoreInfoService.updateScoreInsertInfo(score);
        return new RespBean(ResponseCode.SUCCESS);
    }

    @PostMapping("/del/scoreInfo/{scoreInfoId}")
    public RespBean delScoreInfo(@PathVariable("scoreInfoId") Long scoreInfoId) {
        if(Objects.isNull(scoreInfoId)) {
            return new RespBean(ResponseCode.ERROR,"删除失败");
        }
        scoreInfoService.deleteScoreInfo(scoreInfoId);
        return new RespBean(ResponseCode.SUCCESS);
    }

    private String scoreInfoIsEmpty(Score score) {
        if (score.getStudent().getId() == null || score.getCourse().getId() == null) {
            return "Student and Course information can not be empty!";
        }
        return "";
    }
}