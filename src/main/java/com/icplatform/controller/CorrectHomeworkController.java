package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.dto.PreviewLinkResponse;
import com.icplatform.entity.Homework;
import com.icplatform.service.HomeworkService;
import com.icplatform.service.TeachingService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity<PreviewLinkResponse> generateHomeworkPreviewLink(@RequestHeader Map<String, String> header, @RequestParam String cid, @RequestParam int workid, @RequestParam String sno) {
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PreviewLinkResponse("", "error", ""));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {
            // 获取文件资源路径
            Homework homework = homeworkService.findByCidSnoAndWorkid(cid, sno, workid);

            String filePath = homework.getPath();
            if (filePath == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreviewLinkResponse("", "error", ""));
            }

            // 替换路径中的反斜杠
            filePath = filePath.replace("\\", "/");

            // 获取当前IP地址
            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PreviewLinkResponse("", "error", ""));
            }

            // 生成预览链接，编码文件路径
            String previewUrl = "http://" + ipAddress + ":8080/api/assets/preview/pdf?filePath=" +
                    URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String newToken = JWTUtil.generateToken(userType, username);

            return ResponseEntity.ok(new PreviewLinkResponse(previewUrl, "success", newToken));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PreviewLinkResponse("", "error", ""));
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
