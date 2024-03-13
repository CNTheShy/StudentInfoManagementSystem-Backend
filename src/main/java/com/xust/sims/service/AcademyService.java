package com.xust.sims.service;

import com.alibaba.fastjson.JSON;
import com.xust.sims.dto.SelectCourse;
import com.xust.sims.entity.Academy;
import com.xust.sims.entity.Course;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface AcademyService {

    /**
     * 根据查询课程条件，来 search 课程
     * @param selectCourse
     * @return
     */
    List<Course> findCourse(SelectCourse selectCourse);

}
