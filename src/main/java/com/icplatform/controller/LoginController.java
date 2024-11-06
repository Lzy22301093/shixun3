package com.icplatform.controller;

import com.icplatform.entity.Student;
import com.icplatform.entity.Teacher;
import com.icplatform.service.StudentService;
import com.icplatform.service.TeacherService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    // 登录
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> Login(@RequestBody Map<String, String> loginData) {
        String number = loginData.get("username");
        String password = loginData.get("password");


        Map<String, Object> response = new HashMap<>();


        Student student = studentService.searchBySno(number);
        if (student != null && student.getPassword().equals(password)) {
                response.put("message", "登录成功!");
                response.put("status", "success");
                response.put("studentName", student.getSname());
                response.put("sno", student.getSno());
                response.put("type", "0");
                response.put("semail", student.getSemail());
                response.put("gender", student.getGender());
                response.put("major", student.getMajor());
                response.put("token", JWTUtil.generateToken(0, student.getSno()));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
                Teacher teacher = teacherService.searchByTno(number);
                if (teacher != null && teacher.getPassword().equals(password)) {
                    response.put("message", "登录成功!");
                    response.put("status", "success");
                    response.put("teacherName", teacher.getTname());
                    response.put("tno", teacher.getTno());
                    response.put("type", "1");
                    response.put("temail", teacher.getTemail());
                    response.put("gender", teacher.getGender());
                    response.put("major", teacher.getMajor());
                    response.put("title", teacher.getTitle());
                    response.put("token", JWTUtil.generateToken(1, teacher.getTno()));
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

            // 登录失败
            response.put("message", "登录失败!");
            response.put("status", "error");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
