package com.icplatform.repositories;

import com.icplatform.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepositories extends JpaRepository<Teacher, String> {
}
