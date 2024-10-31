package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.dto.FileUploadResponse;
import com.icplatform.entity.Homework;
import com.icplatform.service.CourseService;
import com.icplatform.service.HomeworkService;
import com.icplatform.service.CommitService;
import com.icplatform.service.SCService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CommitService commitService;

    @Autowired
    private SCService scService;

    //展示课程作业信息
    @PostMapping("/display")
    public Map<String,Object> CourseHomework(@RequestHeader Map<String,String> header,@RequestBody Map<String,String> homeworkDate){//cid,sno

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        System.out.println("Received homeworkDate: " + homeworkDate);

        if (userType == 0) {
            String cid = homeworkDate.get("cid");
            String sno = homeworkDate.get("sno");

            if(cid != null && sno != null){

                int totalStudents = scService.countStudentsByCid(cid);

                List<Homework> homeworkList = homeworkService.searchByCidAndSno(cid,sno);
                if(homeworkList != null && homeworkList.size() > 0){
                    List<Map<String, Object>> homeworkInfoList = new ArrayList<>();

                    for(Homework homework : homeworkList){

                        int submitStudents = homeworkService.countSubmittedByCidAndWorkId(cid, homework.getWorkid());

                        String submitRatio = submitStudents + "/" + totalStudents;

                        Map<String, Object> homeworkInfo = new HashMap<>();
                        homeworkInfo.put("homeworkName",homework.getHname());
                        homeworkInfo.put("start",homework.getStart());
                        homeworkInfo.put("end",homework.getEnd());
                        homeworkInfo.put("submitTime",homework.getStime());
                        homeworkInfo.put("submitRatio",submitRatio);
                        homeworkInfo.put("score",homework.getScore());
                        homeworkInfo.put("reviestatus",homework.getReviestatus());

                        // 获取当前时间并判断是否在可提交范围内

                        LocalDateTime now = LocalDateTime.now();
                        if (now.isBefore(homework.getStart()) || now.isAfter(homework.getEnd())) {
                            homeworkInfo.put("submitStatus", "not submitted");
                        } else {
                            homeworkInfo.put("submitStatus", "can submission");
                        }

                        homeworkInfoList.add(homeworkInfo);
                    }

                    // 返回作业信息和新token
                    String newToken = JWTUtil.generateToken(userType, username);
                    Map<String, Object> result = new HashMap<>();
                    result.put("homeworkInfoList", homeworkInfoList);
                    result.put("newToken", newToken);
                    result.put("status", "success");
                    return result;
                }else{
                    // 没有找到作业记录
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("message", "未找到作业记录");
                    errorResponse.put("status", "error");
                    return errorResponse;
                }
            }
        }
        return null;
    }

    //课程作业下载
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadHomework(@RequestHeader Map<String,String> header, @RequestParam String cid, @RequestParam String sno, @RequestParam int workid) {//cid,sno,workid

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 0){

            System.out.println("收到下载请求，CID: " + cid + ", 学号: " + sno + ", 作业ID: " + workid); // 输出接收到的参数

            if(cid != null && sno != null){
                Homework homework = homeworkService.findByCidSnoAndWorkid(cid,sno,workid);

                if(homework != null){
                    String homeworkPath = homework.getPath();
                    Resource resource = resourceLoader.getResource(homeworkPath);

                    String newToken = JWTUtil.generateToken(userType, username);

                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + homework.getHname());
                    headers.add("newToken", newToken);
                    headers.add("status", "success");

                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(resource);
                }
            }
        }
        return null;
    }

    //上传作业
    @PostMapping("upload")
    public FileUploadResponse uploadHomework(@RequestHeader Map<String, String> header, @RequestParam MultipartFile homework, @RequestParam String cid, @RequestParam String sno, @RequestParam int workid, @RequestParam String reviestatus) throws IOException {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return new FileUploadResponse("error", "token已被清除或已过期");
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0) {
            String originalFilename = homework.getOriginalFilename();
            LocalDateTime currentTime = LocalDateTime.now();

            String cno = courseService.findByCid(cid).getCno();

            System.out.println(cno);

            String courseResourcePath = "E:/ICPlatformStorage/Homework/" + cid;  // 使用正斜杠
            File courseDir = new File(courseResourcePath);

            // 如果文件夹不存在则创建
            if (!courseDir.exists()) {
                courseDir.mkdirs();
            }

            // 设置上传文件路径
            File uploadHomework = new File(courseDir, originalFilename);

            // 如果文件已存在，删除旧文件
            if (uploadHomework.exists()) {
                uploadHomework.delete();
            }

            LocalDateTime start = commitService.findByCidAndWorkId(cid, workid).getStart();
            LocalDateTime end = commitService.findByCidAndWorkId(cid, workid).getEnd();

            // 保存新文件
            homework.transferTo(uploadHomework);
            // 检查数据库中是否存在相同的 hname
            try {
                // 存在相同的 hname 则更新记录
                homeworkService.updateHomeworkByHname(originalFilename, uploadHomework.getAbsolutePath().replace("\\", "/") , cid, sno, workid, cno, currentTime,reviestatus); // 将路径中的反斜杠替换为正斜杠
                System.out.println("1");
            } catch (IllegalArgumentException e) {
                // 不存在相同的 hname 则插入新记录
                homeworkService.insertNewHomework(originalFilename, uploadHomework.getAbsolutePath().replace("\\", "/") , cid, sno, workid, cno, currentTime,start,end,reviestatus); // 将路径中的反斜杠替换为正斜杠
                System.out.println("2");
            }

            String newToken = JWTUtil.generateToken(userType, username);

            return new FileUploadResponse("success", "上传成功",newToken);

        } else {
            return new FileUploadResponse("error", "用户权限不足");
        }
    }

    //布置作业
    @PostMapping("/assign")
    public Map<String, String> assignHomework(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> assignData){

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message","token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if(userType == 1){

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(assignData.get("start"),df);
            LocalDateTime end = LocalDateTime.parse(assignData.get("end"),df);
            int workid = Integer.valueOf(assignData.get("workid"));
            String cid = assignData.get("cid");

            try{
                //如果数据库中存在记录则更新
                commitService.updateAssignHomework(start, end, workid,cid);
                System.out.println("3");
            }catch (IllegalArgumentException e){
                //如果不存在记录则插入新的记录
                commitService.InsertAssignHomework(start, end, workid,cid);
                System.out.println("4");
            }

            String newToken = JWTUtil.generateToken(userType,username);

            Map<String, String> response = new HashMap<>();
            response.put("status","success");
            response.put("message","布置作业成功");
            response.put("newToken",newToken);
            return response;
        }

        Map<String, String> response = new HashMap<>();
        response.put("status","error");
        response.put("message","布置作业失败权限不足");
        return response;
    }

}
