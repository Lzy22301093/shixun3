package com.icplatform.service;

import com.icplatform.entity.Assets;
import com.icplatform.entity.Course;
import com.icplatform.repositories.CourseRepositories;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class CourseService {
    private CourseRepositories courseRepositories;

    public CourseService(CourseRepositories courseRepositories) {
        this.courseRepositories = courseRepositories;
    }

    public Course findByCidAndCno(String cid, String cno) {
        return courseRepositories.findByCidAndCno(cid,cno);
    }
    public Course findByCid(String cid) {
        return courseRepositories.findByCid(cid);
    }

    // 更新课程大纲
    public void updateOutlineByCid(String outline, String cid) {
        Course course = courseRepositories.findByCid(cid);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        course.setOutline(outline);
        courseRepositories.save(course);  // 保存更新后的资产
    }

    //更新教学日历
    public void updateCalendarByCid(String calendar, String cid) {
        Course course = courseRepositories.findByCid(cid);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        course.setCalendar(calendar);
        courseRepositories.save(course);  // 保存更新后的资产
    }
}
