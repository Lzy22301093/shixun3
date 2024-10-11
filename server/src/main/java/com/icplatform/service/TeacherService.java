package com.icplatform.service;

import com.icplatform.entity.Teacher;
import com.icplatform.repositories.TeacherRepositories;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    private TeacherRepositories teacherRepositories;

    public TeacherService(TeacherRepositories teacherRepositories) {
        this.teacherRepositories = teacherRepositories;
    }

    public Teacher searchByTno(String tno) { return teacherRepositories.getReferenceById(tno);}
}
