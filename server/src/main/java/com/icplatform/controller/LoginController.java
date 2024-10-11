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

    // 学生登录
    @PostMapping("/student")
    public ResponseEntity<Map<String, Object>> studentLogin(@RequestBody Map<String, String> loginData) {
        String sno = loginData.get("sno");
        String password = loginData.get("password");

        Student student = studentService.searchBySno(sno);
        Map<String, Object> response = new HashMap<>();

        if (student != null && student.getPassword().equals(password)) {
            response.put("message", "登录成功!");
            response.put("status", "success");
            response.put("studentName", student.getSname());
            response.put("token", JWTUtil.generateToken(0, student.getSno()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "登录失败!");
            response.put("status", "error");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/teacher")
    public ResponseEntity<Map<String, Object>> teacherLogin(@RequestBody Map<String, String> loginData) {
        String tno = loginData.get("tno");
        String password = loginData.get("password");

        Teacher teacher = teacherService.searchByTno(tno);
        Map<String, Object> response = new HashMap<>();

        if (teacher != null && teacher.getPassword().equals(password)) {
            response.put("message", "登录成功!");
            response.put("status", "success");
            response.put("teacherName", teacher.getTname());
            response.put("token", JWTUtil.generateToken(1, teacher.getTno()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "登录失败!");
            response.put("status", "error");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
