package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.entity.Homework;
import com.icplatform.service.HomeworkService;
import com.icplatform.service.TeachingService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin(origins = "*")
public class CorrectHomeworkController {

    @Autowired
    private TeachingService teachingService;

    @Autowired
    private HomeworkService homeworkService;

    @PostMapping("/displayCorrect")
    public Map<String, Object> displayCorrect(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> displayData) {//展示作业，前端发送cid和workid
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 1){
            String cid = displayData.get("cid");
            int workid = Integer.valueOf(displayData.get("workid"));

            List<Homework> homeworkList = homeworkService.findByCidAndWorkid(cid,workid);

            String newToken = JWTUtil.generateToken(userType,username);

            Map<String,Object> response = new HashMap<>();
            response.put("homeworkList",homeworkList);
            response.put("status","success");
            response.put("newToken",newToken);
            return response;
        }else{
            Map<String,Object> response = new HashMap<>();
            response.put("status","error");
            return response;
        }
    }

    @GetMapping("/sendHomework")
    public ResponseEntity<Resource> sendHomework(@RequestHeader Map<String, String> header, @RequestParam String cid, @RequestParam int workid,@RequestParam String sno) {//前端发送cid,workid,sno
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 1){

            Resource file = homeworkService.getHomeworkFile(cid, workid, sno);

            if (file != null) {

                System.out.println(file.getFilename());

                String newToken = JWTUtil.generateToken(userType,username);

                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=" + file.getFilename())
                        .header("status","success")
                        .header("newToken",newToken)
                        .body(file);
            }
        }
        return ResponseEntity.badRequest().body(null);
    }

    //批改作业
    @PostMapping("/correctHomework")
    public Map<String, Object> correctHomework(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> correctData) {//批改作业,前端给的 分数 和 cid 和 workid  sno

        Map<String,Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            response.put("status","error");
            response.put("message","token已被清楚或已过期");
            return response;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 1){

            String cid = correctData.get("cid");
            String workid = correctData.get("workid");
            String sno = correctData.get("sno");
            String score = correctData.get("score");

            Integer scoreNum = Integer.valueOf(score);
            Integer workId = Integer.valueOf(workid);

            Homework homework = homeworkService.findByCidSnoAndWorkid(cid,sno,workId);

            if(homework != null){
                if(scoreNum != null) {
                    homework.setScore(scoreNum);
                }
                homeworkService.save(homework);
            }
        } else if (userType == 0) {
            response.put("status","error");
            response.put("message","用户权限不足");
            return response;
        }

        String newToken = JWTUtil.generateToken(userType,username);

        response.put("status","success");
        response.put("newToken",newToken);
        response.put("message","分数上传成功");
        return response;
    }
}
