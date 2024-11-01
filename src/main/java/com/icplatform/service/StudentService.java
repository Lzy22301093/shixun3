package com.icplatform.service;


import com.icplatform.entity.Student;
import com.icplatform.repositories.StudentRepositories;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private StudentRepositories studentRepositories;

    public StudentService(StudentRepositories studentRepositories) {
        this.studentRepositories = studentRepositories;
    }

    public Student searchBySno(String sno) {
        return studentRepositories.findById(sno).orElse(null);
    }
    public Student save(Student student) {
        return studentRepositories.save(student);
    }
}
