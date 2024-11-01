package com.icplatform.repositories;


import com.icplatform.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepositories extends JpaRepository<Student, String> {  //<类，主键>
}
