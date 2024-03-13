package com.xust.sims.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xust.sims.dao.CourseMapper;
import com.xust.sims.dao.ClassMapper;
import com.xust.sims.dto.ScheduleConfig;
import com.xust.sims.dto.SelectCourse;
import com.xust.sims.dto.StudentTimetable;
import com.xust.sims.dto.NewEnroll;
import com.xust.sims.entity.Course;
import com.xust.sims.entity.Class;
import com.xust.sims.threadconfig.CloseCourseThread;
import com.xust.sims.utils.UrlEncodeUtils;
import com.xust.sims.web.aspect.ScriptAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private ScheduledExecutorService scheduledExecutorService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addCourse(Course course) {
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());
        courseMapper.addCourse(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addClass(Class newClass) {
        classMapper.addClass(newClass);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void openNewClass(Integer courseId) {
        Course course = courseMapper.findCourseById(courseId);
        int count = classMapper.getCountClass(courseId);
        int i;
        for (i = 1001;i <= count+1001;i++) {
            if (classMapper.findClassBaseInfoById(i) == null) {
                break;
            }
        }
        Class newClass = new Class(""+i, course.getName(), courseId);
        classMapper.addClass(newClass);
    }

    @Override
    public List<Class> getClassesByCourseId(Integer courseId) {
        return classMapper.findClassByCourseId(courseId);
    }

    @Override
    public List<Course> findCourse(SelectCourse selectCourse) {
        return courseMapper.findCourse(selectCourse);
    }
    @Override
    public Course getCourseByName(String courseName){ return courseMapper.findCourseByName(courseName); }
    @Override
    public Integer getCourseIdByName(String courseName){ return courseMapper.findCourseIdByName(courseName); }

    @Override
    public JSON findCommonCourseInCache(int pageSize, int currPage) {
        JSONObject json = new JSONObject();
        List<Course> res = new ArrayList<>();
        Long total = redisTemplate.opsForList().size("select_course_ids");
        long start = pageSize * (currPage - 1);
        long offset = (pageSize * (currPage - 1)) + pageSize;
        List<Object> courseIds = redisTemplate.opsForList().range("select_course_ids", start, offset - 1);
        for (Object courseId : courseIds) {
            Course course = (Course) redisTemplate.opsForValue().get("course_" + courseId);
            Integer num = (Integer) redisTemplate.opsForValue().get("course_left_num_" + courseId);
            course.setLeftNum(num);
            res.add(course);
        }
        json.put("courseList", res);
        json.put("total", total);
        return json;
    }

    @Override
    public List<StudentTimetable> getCoursesByStudentId(String studentId) {
        return courseMapper.findCourseByStudentId(studentId);
    }

    @Override
    public JSON getSelectCourseUrlSign() {
        JSONObject json = new JSONObject();
        if (redisTemplate.opsForValue().get("disabled_select") != null) {
            json.put("status", 408);
            json.put("msg", "Can not select the course at present, the time for selecting the course is not yet");
            return json;
        }
        String sign = null;
        if (redisTemplate.hasKey("sign")) {
            log.info("Request sign after the first time, get it from the cache!");
            sign = ((String) redisTemplate.opsForValue().get("sign"));
        } else {
            log.info("The first request sign is generated and added to the cache");
            sign = UrlEncodeUtils.getUrlSign("/select/course");
            redisTemplate.opsForValue().set("sign", sign, Duration.ofMinutes(3L));
        }
        json.put("status", 210);
        json.put("sign", sign);
        return json;
    }

    @Override
    @Async
    public CompletableFuture<JSON> studentSelectCourseByCid(String studentId, int cid, String sign) {
        JSONObject json = new JSONObject();
        String cacheSign = (String) redisTemplate.opsForValue().get("sign");
        if (!sign.equals(cacheSign)) {
            json.put("status", 409);
            json.put("msg", "Description Failed to authenticate the signature value");
            return CompletableFuture.completedFuture(json);
        }
        Jedis jedis = null;
        try {
            jedis = (Jedis) redisConnectionFactory.getConnection().getNativeConnection();
            String sha = ScriptAspect.shaCourseValue;
            log.info("The sha value to get is：{}", sha);
            List<String> keyList = new ArrayList<>(2);
            keyList.add(Integer.toString(cid));
            List<String> valueList = new ArrayList<>(2);
            valueList.add(studentId);
            Long res = ((Long) jedis.evalsha(sha, keyList, valueList));
            if (res == -2) {
                json.put("status", 411);
                json.put("msg", "You have already chosen this course, you cannot choose it again!");
            } else if (res == -1) {
                json.put("status", 410);
                json.put("msg", "This course is currently full, please try again later!");
            } else if (res == 1) {
                json.put("status", 211);
                json.put("msg", "Enrollment success!!");
            }
            log.info("The result of enrollment is：{} {}", res, res.getClass().getName());
        } finally {
            assert jedis != null;
            jedis.close();
        }
        return CompletableFuture.completedFuture(json);
    }

    @Override
    public String[] findPredictionData(String studentId, int cid) {
        List<Integer> preRequests = courseMapper.findPreRequestById(cid);
        List<Integer> courseTaken = courseMapper.findCourseTakenById(studentId);
        List<Integer> pastScore = courseMapper.findPastScoreById(studentId);

        return processStudentData(studentId, cid, courseTaken, pastScore, preRequests);
    }

    @Override
    public List<Course> getPreRequest(int courseId) {
        List<Integer> pre = courseMapper.findPreRequestById(courseId);
        List<Course> preRequests = new ArrayList<>();
        for (int i : pre){
            Course ycourse = courseMapper.findCourseById(i);
            if (ycourse != null) {
                preRequests.add(ycourse);
            }
        }
        return preRequests;
    }

    public static String[] processStudentData(
            String studentID,
            int targetCourse,
            List<Integer> coursesTaken,
            List<Integer> pastScores,
            List<Integer> preRequests
    ) {
        final int NUM_COURSES = 100;

        int[] enrolledCourses = new int[NUM_COURSES];
        int[] pastGrades = new int[NUM_COURSES];
        int[] preRequestedCourses = new int[NUM_COURSES];

        for (int i = 0; i < coursesTaken.size(); i++) {
            int course = coursesTaken.get(i);
            if (course >= 0 && course < NUM_COURSES) {
                enrolledCourses[course] = 1;
                pastGrades[course] = pastScores.get(i);
            }
        }

        for (int course : preRequests) {
            if (course >= 0 && course < NUM_COURSES) {
                preRequestedCourses[course] = 1;
            }
        }

        String[] studentData = new String[2 + 3 * NUM_COURSES];
        studentData[0] = studentID;
        studentData[1] = String.valueOf(targetCourse);
        for (int i = 0; i < NUM_COURSES; i++) {
            studentData[2 + i] = String.valueOf(enrolledCourses[i]);
            studentData[2 + NUM_COURSES + i] = String.valueOf(pastGrades[i]);
            studentData[2 + 2 * NUM_COURSES + i] = String.valueOf(preRequestedCourses[i]);
        }

        return studentData;
    }

    @Override
    public List<Course> getSelectedCourse(String studentId) {
        List<Object> idList = redisTemplate.opsForList().range("student_selected_course_" + studentId, 0, -1);
        List<Course> courseList = new ArrayList<>();
        for (Object id : idList) {
            courseList.add(((Course) redisTemplate.opsForValue().get("course_" + id)));
        }
        return courseList;
    }

    @Override
    public void cancelSelectedCourse(String studentId, int cid) {
        try (Jedis jedis = (Jedis) redisConnectionFactory.getConnection().getNativeConnection()) {
            List<String> keyList = new ArrayList<>(2);
            keyList.add(Integer.toString(cid));
            List<String> valueList = new ArrayList<>(2);
            valueList.add(studentId);
            jedis.evalsha(ScriptAspect.shaCancelCourseValue, keyList, valueList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void courseConfig(ScheduleConfig config) {
        courseMapper.addCourseConfig(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteCourseById(Integer id) {
        courseMapper.deleteCourseInfoById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteClassById(String classId, Integer courseId) {
        classMapper.deleteClassById(classId, courseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean openCourseSystem(Date startTime, Date endTime, int[] academyIds) {
        /**
         * 1. 打开学院的选课权限
         * 2. 设置选择的开始时间 Redis 键 （TTL为 startTime - currTime）
         * 3. 课程信息预热：
         *      1. 添加所有课程性质为 公选的 id 到 list
         *      2. 存储课程信息 string - object
         *      3. 课程剩余数量 string - int
         * 4. 设定系统的关闭时间 时间为 endTime - currTime
         *      1. 删除课程 id List
         *      2. 更新学院的选择状态
         */
        List<Course> commonCourse = courseMapper.getCommonCourse();
        log.info("Has key select_course_ids：{}", redisTemplate.hasKey("select_course_ids"));
        if (redisTemplate.hasKey("select_course_ids")) {
            System.out.println(1);
            return false;
        }
        if (commonCourse.isEmpty()) {
            System.out.println(2);
            return false;
        }
        try {
            changeSelectFlagByAcademyIds(academyIds);
            Date currTime = new Date();
            redisTemplate.opsForValue().set("disabled_select", "", startTime.getTime() - currTime.getTime(), TimeUnit.MILLISECONDS);
            List<Integer> courseIds = commonCourseIds(commonCourse);
            for (Integer courseId : courseIds) {
                redisTemplate.opsForList().rightPush("select_course_ids", courseId);
            }
            //TODO 可以将这些Redis插入操作，放到一个pipeline中，减少网络请求数
            for (Course course : commonCourse) {
                redisTemplate.opsForValue().set("course_" + course.getId(), course, endTime.getTime() - currTime.getTime(), TimeUnit.MILLISECONDS);
                redisTemplate.opsForValue().set("course_left_num_" + course.getId(), course.getTotal(), endTime.getTime() - currTime.getTime(), TimeUnit.MILLISECONDS);
            }
            CloseCourseThread thread = new CloseCourseThread(commonCourse, this);
            log.info("Start the scheduled task and close the course selection system");
            scheduledExecutorService.schedule(thread,
                    endTime.getTime() - currTime.getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void closeCourseSystem(List<Course> courseList) {
        try {
            Set<String> keys = new HashSet<>();
            keys.add("select_course_ids");
            Map<Object, Set<Object>> courseToStudentId = new HashMap<>();
            Set<Integer> courseIds = new HashSet<>();
            List<Object> selectCourseIds = redisTemplate.opsForList().range("select_course_ids", 0, -1);
            log.info("The list of courses is:{}", selectCourseIds);
            for (Object courseId : selectCourseIds) {
                log.info("The course ID type is:{}", courseId.getClass().getName());
                Set<Object> members = redisTemplate.opsForSet().members("course_selected_student_" + courseId);
                log.info("The current course is: {} The person selected is:{}", courseId, members);
                for (Object studentId : members) {
                    log.info("The student ID type is:{}", studentId.getClass().getName());
                    keys.add("student_selected_course_" + studentId);
                }
                courseToStudentId.put(courseId, members);
                courseIds.add(((Integer) courseId));
                keys.add("course_selected_student_" + courseId);
            }

            log.info("The corresponding relationship between courses and students is as follows:{}", courseToStudentId);
            for (Map.Entry<Object, Set<Object>> entry : courseToStudentId.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    courseMapper.addStudentCommonCourse(entry.getKey(), entry.getValue());
                }
            }

            //delete keyList\ from redis
            log.info("To delete keyList: {}", keys);
            redisTemplate.delete(keys);
            redisTemplate.delete("sign");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            courseMapper.closeAllCourseSystem();
            insertClasses();
        }
    }

    private void insertClasses() {
        List<NewEnroll> newEnrolls = courseMapper.getNewEnrolls();
        if (newEnrolls == null || newEnrolls.isEmpty()) {
            // no new enrollment
            return;
        }
        for (NewEnroll newEnroll : newEnrolls) {
            if (newEnroll == null) {
                continue;
            }
            Integer courseId = newEnroll.getCourseId();
            String studentId = newEnroll.getStudentId();
            Integer classId = classMapper.findNotFullClassByCourseId(courseId);
            if (classId == null) {
                // No class found, so open a new class
                openNewClass(courseId);
                classId = classMapper.findNotFullClassByCourseId(courseId);
                if (classId == null) {
                    continue;
                }
            }
            classMapper.enrollToClassById(studentId, courseId, classId);
            classMapper.updateClassSizePlusOne(classId);
        }
    }

    private List<Integer> commonCourseIds(List<Course> courses) {
        List<Integer> ids = new ArrayList<>();
        for (Course course : courses) {
            ids.add(course.getId());
        }
        return ids;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void changeSelectFlagByAcademyIds(int[] academyIds) {
        if (academyIds == null || academyIds.length == 0) {
            courseMapper.openAllCourseSystem();
        } else {
            courseMapper.openCourseSystemByAcademyIds(academyIds);
        }
    }
}
