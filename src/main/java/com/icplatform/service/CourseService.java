package com.icplatform.service;

import com.icplatform.entity.Course;
import com.icplatform.repositories.CourseRepositories;
import org.springframework.stereotype.Service;

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
}
