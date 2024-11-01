package com.icplatform.repositories;

import com.icplatform.CompositePrimaryKey.CourseId;
import com.icplatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepositories extends JpaRepository<Course, CourseId> {

    Course findByCidAndCno(String cid, String cno);

    Course findByCid(String cid);

}
