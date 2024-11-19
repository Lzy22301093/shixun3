package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.reflect.TypeToken;
import com.icplatform.dto.DownloadLinkResponse;
import com.icplatform.dto.FileUploadResponse;
import com.icplatform.dto.PreviewLinkResponse;
import com.icplatform.entity.Commit;
import com.icplatform.entity.Homework;
import com.icplatform.service.CourseService;
import com.icplatform.service.HomeworkService;
import com.icplatform.service.CommitService;
import com.icplatform.service.SCService;
import com.icplatform.utils.GsonUtil;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

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
            String sno = homeworkDate.get("pno");

            if(cid != null && sno != null && sno.charAt(0) == '2') {

                int totalStudents = scService.countStudentsByCid(cid);

                List<Commit> commitList = commitService.findByCid(cid);

                if(commitList != null && commitList.size() > 0) {
                    List<Map<String, Object>> commitInfoList = new ArrayList<>();
                    for (Commit commit : commitList) {


                        int publish = commit.getPublish();
                        if (publish == 1) {

                            int workId = commit.getWorkid();

                            Homework homework = homeworkService.findByCidSnoAndWorkid(cid, sno, workId);

                                int submitStudents = homeworkService.countSubmittedByCidAndWorkId(cid, workId);

                                String submitRatio = submitStudents + "/" + totalStudents;
                                Map<String, Object> commitInfo = new HashMap<>();

                                commitInfo.put("cname", commit.getCname());
                                commitInfo.put("start", commit.getStart());
                                commitInfo.put("end", commit.getEnd());
                                commitInfo.put("submitRatio", submitRatio);
                                commitInfo.put("workid", workId);
                                if (homework != null) {
                                    commitInfo.put("submitTime", homework.getStime());
                                }else{
                                    commitInfo.put("submitTime", "未提交");
                                }
                                if (commit.getPublishscore() == 1) {
                                    commitInfo.put("score", homework.getScore());
                                } else {
                                    commitInfo.put("score", "未公布成绩");
                                }
                                if(homework != null){
                                    commitInfo.put("reviestatus", homework.getReviestatus());
                                }else{
                                    commitInfo.put("reviestatus", "未提交");
                                }
                                LocalDateTime now = LocalDateTime.now();
                                if (now.isBefore(commit.getStart()) || now.isAfter(commit.getEnd())) {
                                    commitInfo.put("submitStatus", "0");
                                } else {
                                    commitInfo.put("submitStatus", "1");
                                }
                                commitInfoList.add(commitInfo);
                            }
                        }
                    String newToken = JWTUtil.generateToken(userType, username);

                    Map<String, Object> response = new HashMap<>();
                    response.put("homeworkList",commitInfoList);
                    response.put("status","success");
                    response.put("message","作业列表返回成功");
                    response.put("newToken",newToken);
                    return response;
                    }

                }else{
                    Map<String, Object> response = new HashMap<>();
                    response.put("status","error");
                    response.put("message","没作业");
                    return response;
                }
        }else if (userType == 1) {

            String cid = homeworkDate.get("cid");
            String tno = homeworkDate.get("pno");

            if(cid != null && tno != null && tno.charAt(0) == '5'){
                int totalStudents = scService.countStudentsByCid(cid);
                List<Commit> commitList = commitService.findByCid(cid);
                if(commitList != null && commitList.size() > 0) {
                    List<Map<String, Object>> commitInfoList = new ArrayList<>();
                    for(Commit commit : commitList){

                        int workId = commit.getWorkid();

                        int submitStudents = homeworkService.countSubmittedByCidAndWorkId(cid, workId);

                        String submitRatio = submitStudents + "/" + totalStudents;

                        Map<String ,Object> commitInfo = new HashMap<>();

                        commitInfo.put("cname",commit.getCname());
                        commitInfo.put("start",commit.getStart());
                        commitInfo.put("end",commit.getEnd());
                        commitInfo.put("submitRatio",submitRatio);
                        commitInfo.put("workid",workId);
                        commitInfo.put("publish",commit.getPublish());
                        commitInfo.put("publishScore",commit.getPublishscore());
                        commitInfo.put("fullMark",commit.getFullmark());

                        System.out.println("publish:" + commit.getPublish());
                        System.out.println("publishScore:" + commit.getPublishscore());

                        commitInfoList.add(commitInfo);
                    }
                    String newToken = JWTUtil.generateToken(userType, username);

                    Map<String, Object> response = new HashMap<>();
                    response.put("homeworkList",commitInfoList);
                    response.put("status","success");
                    response.put("message","作业列表返回成功");
                    response.put("newToken",newToken);
                    return response;
                }else{
                    Map<String, Object> response = new HashMap<>();
                    response.put("status","error");
                    response.put("message","没布置作业");
                    return response;
                }
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status","error");
            response.put("message","用户权限错误,请重新登录");
            return response;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status","error");
        response.put("message","用户权限错误");
        return response;
    }



    //提交作业
    @PostMapping("/upload")
    public FileUploadResponse uploadHomework(@RequestHeader Map<String, String> header, @RequestParam("homeworkFile") MultipartFile homeworkFile, @RequestParam("cid") String cid, @RequestParam("sno") String sno, @RequestParam("workid") int workid) throws IOException {

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
            String originalFilename = homeworkFile.getOriginalFilename();
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
            homeworkFile.transferTo(uploadHomework);

            String reviestatus = "未批改";
            // 检查数据库中是否存在相同的 hname
            try {
                // 存在相同的 hname 则更新记录
                homeworkService.updateHomeworkByCidAndSnoAndWorkid(originalFilename, uploadHomework.getAbsolutePath().replace("\\", "/") , cid, sno, workid, cno, currentTime,reviestatus); // 将路径中的反斜杠替换为正斜杠
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
    public Map<String, String> assignHomework(@RequestHeader Map<String, String> header,
                                              @RequestParam("homework") MultipartFile homework,
                                              @RequestParam("start") String start,
                                              @RequestParam("end") String end,
                                              @RequestParam("workid") int workid,
                                              @RequestParam("cid") String cid,
                                              @RequestParam("content") String content,
                                              @RequestParam("fullmark") int fullmark) throws IOException {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {

            boolean workIdExists = commitService.checkWorkIdExist(workid,cid);
            if (workIdExists) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "workid 已存在");
                return response;
            }

            System.out.println(start);
            System.out.println(end);

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(start, df);
            LocalDateTime endTime = LocalDateTime.parse(end, df);

            // 保存文件到指定路径
            String homeworkPath = "E:/ICPlatformStorage/AssignHomework/" + cid + "/" + workid;
            File homeworkDir = new File(homeworkPath);

            if (!homeworkDir.exists()) {
                homeworkDir.mkdirs();
            }

            String originalFilename = homework.getOriginalFilename();
            File savedHomework = new File(homeworkDir, originalFilename);

            if (savedHomework.exists()) {
                savedHomework.delete();
            }

            homework.transferTo(savedHomework);

            // 将路径和文件名保存在数据库的commit表中
            String filePath = savedHomework.getAbsolutePath().replace("\\", "/");
            commitService.insertAssignHomework(startTime, endTime, workid, cid, filePath, originalFilename, content, fullmark);

            String newToken = JWTUtil.generateToken(userType, username);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "布置作业成功");
            response.put("newToken", newToken);
            return response;
        }

        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "布置作业失败，权限不足");
        return response;
    }

    //更新布置的作业
    @PostMapping("/assign/update")
    public Map<String, String> updateAssignHomework(@RequestHeader Map<String, String> header,
                                                    @RequestParam("homework") MultipartFile homework,
                                                    @RequestParam("start") String start,
                                                    @RequestParam("end") String end,
                                                    @RequestParam("workid") int workid,
                                                    @RequestParam("cid") String cid,
                                                    @RequestParam("content") String content,
                                                    @RequestParam("fullmark") int fullmark,
                                                    @RequestParam("publish") int publish,
                                                    @RequestParam("publishScore") int publishScore) throws IOException {
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {
            if(!commitService.checkWorkIdExist(workid,cid)){
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message","workid不存在，无法更新");
                return response;
            }

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(start, df);
            LocalDateTime endTime = LocalDateTime.parse(end, df);

            // 保存文件到指定路径
            String homeworkPath = "E:/ICPlatformStorage/AssignHomework/" + cid + "/" + workid;
            File homeworkDir = new File(homeworkPath);

            if (!homeworkDir.exists()) {
                homeworkDir.mkdirs();
            }

            String originalFilename = homework.getOriginalFilename();
            File savedHomework = new File(homeworkDir, originalFilename);

            if (savedHomework.exists()) {
                savedHomework.delete();
            }

            homework.transferTo(savedHomework);
            // 将路径和文件名保存在数据库的commit表中
            String filePath = savedHomework.getAbsolutePath().replace("\\", "/");
            commitService.updateAssignHomework(startTime, endTime, workid, cid, filePath, originalFilename, content, fullmark, publish, publishScore);

            String newToken = JWTUtil.generateToken(userType, username);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "作业更新成功");
            response.put("newToken", newToken);
            return response;
        }
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "更新布置作业失败，权限不足");
        return response;
    }

    //更新布置的作业
    @PostMapping("/assign/update/publish")
    public Map<String, String> updatePublish(@RequestHeader Map<String, String> header,@RequestBody Map<String, String> publishData) throws IOException {
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {

            String cid = publishData.get("cid");
            int workid = Integer.valueOf(publishData.get("workid"));
            int publish = Integer.valueOf(publishData.get("publish"));

            if(!commitService.checkWorkIdExist(workid,cid)){
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message","workid不存在，无法更新");
                return response;
            }

            commitService.updatePublish(publish, cid, workid);

            String newToken = JWTUtil.generateToken(userType, username);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "作业更新成功");
            response.put("newToken", newToken);
            return response;
        }
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "更新布置作业失败，权限不足");
        return response;
    }

    //更新布置的作业分数
    @PostMapping("/assign/update/publishScore")
    public Map<String, String> updatePublishScore(@RequestHeader Map<String, String> header,@RequestBody Map<String, String> publishScoreData) throws IOException {
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {

            String cid = publishScoreData.get("cid");
            int workid = Integer.valueOf(publishScoreData.get("workid"));
            System.out.println("publish:"+publishScoreData.get("publishScore"));
            int publishScore = Integer.valueOf(publishScoreData.get("publishScore"));

            if(!commitService.checkWorkIdExist(workid,cid)){
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message","workid不存在，无法更新");
                return response;
            }

            commitService.updatePublishScore(publishScore, cid, workid);

            String newToken = JWTUtil.generateToken(userType, username);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "作业更新成功");
            response.put("newToken", newToken);
            return response;
        }
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "更新布置作业失败，权限不足");
        return response;
    }

    //课程作业下载
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadHomework(@RequestParam String filePath){
        File file = new File(filePath);
        Resource resource = new FileSystemResource(file);

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("status","error")
                    .build();
        }

        // 设置 Content-Disposition 响应头
        String filename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        // 返回文件资源及新的 token
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("status","success")
                .body(resource);
    }

    //生成学生作业下载链接
    @GetMapping("/generateDownloadLink")
    public ResponseEntity<DownloadLinkResponse> generateDownloadLink(@RequestHeader Map<String, String> header,
                                                                     @RequestParam String cid,
                                                                     @RequestParam int workid,
                                                                     @RequestParam String sno) {
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DownloadLinkResponse("", "error"));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            // 根据 cid, workid 和 sno 查找文件路径
            String filePath = homeworkService.findByCidSnoAndWorkid(cid, sno, workid).getPath();
            System.out.println("文件路径: " + filePath);

            if (filePath == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DownloadLinkResponse("", "error"));
            }

            // 替换路径中的反斜杠为正斜杠
            filePath = filePath.replace("\\", "/");

            // 获取当前 IP 地址
            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DownloadLinkResponse("", "error"));
            }

            // 构建下载链接
            String downloadUrl = "http://" + ipAddress + ":8080/api/homework/download?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            System.out.println(downloadUrl);

            // 生成新的 token
            String newToken = JWTUtil.generateToken(userType, username);

            return ResponseEntity.ok(new DownloadLinkResponse(downloadUrl, "success", newToken));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DownloadLinkResponse("", "error"));
    }

    //生成布置作业下载链接
    @GetMapping("/assign/generateDownloadLink")
    public ResponseEntity<DownloadLinkResponse> generateAssignDownloadLink(@RequestHeader Map<String, String> header,
                                                                     @RequestParam String cid,
                                                                     @RequestParam int workid) {
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DownloadLinkResponse("", "error"));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            // 根据 cid, workid 和 sno 查找文件路径
            String filePath = commitService.findByCidAndWorkId(cid, workid).getPath();
            System.out.println("文件路径: " + filePath);

            if (filePath == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DownloadLinkResponse("", "error"));
            }

            // 替换路径中的反斜杠为正斜杠
            filePath = filePath.replace("\\", "/");

            // 获取当前 IP 地址
            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DownloadLinkResponse("", "error"));
            }

            // 构建下载链接
            String downloadUrl = "http://" + ipAddress + ":8080/api/homework/download?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            System.out.println(downloadUrl);

            // 生成新的 token
            String newToken = JWTUtil.generateToken(userType, username);

            return ResponseEntity.ok(new DownloadLinkResponse(downloadUrl, "success", newToken));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DownloadLinkResponse("", "error"));
    }



    //老师查看布置作业的列表
    @PostMapping("/display/homework")
    public Map<String, Object> displayHomework(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> disData) {//cid
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message","token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {

            String cid = disData.get("cid");
            if(cid != null){

                List<Commit> HomeworkList = commitService.findByCid(cid);
                List<Map<String, Object>> homeworkList = new ArrayList<>();
                for (Commit commit : HomeworkList) {

                    String cname = commit.getCname();
                    LocalDateTime start = commit.getStart();
                    LocalDateTime end = commit.getEnd();
                    int workid = commit.getWorkid();


                    Map<String, Object> commitinfo = new HashMap<>();
                    commitinfo.put("cname", cname);
                    commitinfo.put("cid", cid);
                    commitinfo.put("start", start);
                    commitinfo.put("end", end);
                    commitinfo.put("workid", workid);
                    homeworkList.add(commitinfo);
                }

                String newToken = JWTUtil.generateToken(userType, username);
                Map<String, Object> response = new HashMap<>();
                response.put("homeworkList", homeworkList);
                response.put("newToken",newToken);
                response.put("status","success");
                response.put("message","已成功返回课程作业列表");
                return response;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status","error");
        response.put("message","用户类型错误");
        return response;
    }

    //获取作业信息
    @GetMapping("/info")
    public Map<String, Object> homeworkInfo(@RequestHeader Map<String, String> header, @RequestParam String cid, @RequestParam int workid) {

        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message","token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0) {

            Commit commit = commitService.findByCidAndWorkId(cid, workid);

            if (commit != null) {

                LocalDateTime start = commit.getStart();
                LocalDateTime end = commit.getEnd();
                String cname = commit.getCname();
                String content = commit.getContent();
                int fullmark = commit.getFullmark();

                String newToken = JWTUtil.generateToken(userType, username);

                Map<String, Object> commitinfo = new HashMap<>();
                commitinfo.put("cname", cname);
                commitinfo.put("start", start);
                commitinfo.put("end", end);
                commitinfo.put("fullmark", fullmark);
                commitinfo.put("status", "success");
                commitinfo.put("message", "成功");
                commitinfo.put("newToken", newToken);
                return commitinfo;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "失败，数据不存在");
            return response;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "用户类型错误");
        return response;
    }

    //删除布置作业
    @PostMapping("/assign/delete")
    public Map<String, Object> AssignHomeworkDelete(@RequestHeader Map<String, String> header, @RequestBody Map<String, Object> commitData) {//cid,
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {
            // 使用 TypeToken 指定泛型类型
            Type listOfStringType = new TypeToken<List<String>>() {}.getType();
            Type listOfIntegerType = new TypeToken<List<Integer>>() {}.getType();

            // 转换 JSON 数据
            List<String> cids = GsonUtil.fromJson(GsonUtil.toJson(commitData.get("cid")), listOfStringType);
            List<Double> workidDoubles = GsonUtil.fromJson(GsonUtil.toJson(commitData.get("workid")), new TypeToken<List<Double>>() {}.getType());

            // 转换 List<Double> 为 List<Integer>
            List<Integer> workids = workidDoubles.stream()
                    .map(Double::intValue)  // 将 Double 转为 Integer
                    .collect(Collectors.toList());

            if (cids == null || workids == null || cids.size() != workids.size()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "传入的cid和aid数量不匹配");
                return response;
            }

            boolean allDeleted = true;
            for (int i = 0; i < cids.size(); i++) {
                String cid = cids.get(i);
                int workid = workids.get(i);

                // 查询路径
                String tpath = commitService.searchPathByCidAndWorkid(cid, workid);
                if (tpath != null) {
                    // 删除数据库记录和文件
                    commitService.deleteCommitByCidAndWorkid(cid, workid);
                    File file = new File(tpath);

                    if (file.exists() && !file.delete()) {
                        allDeleted = false;
                    }
                } else {
                    allDeleted = false;
                }
            }

            String newToken = JWTUtil.generateToken(userType, username);
            Map<String, Object> response = new HashMap<>();
            response.put("status", allDeleted ? "success" : "partial_success");
            response.put("message", allDeleted ? "文件删除成功" : "文件删除失败");
            response.put("newToken", newToken);
            return response;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "权限不足,删除失败");
        return response;
    }

    // 生成预览链接(学生作业)
    @GetMapping("/preview")
    public ResponseEntity<PreviewLinkResponse> generatePreviewLink(@RequestHeader Map<String, String> header, @RequestParam String cid, @RequestParam String sno, @RequestParam int workid) {
        System.out.println("预览");
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            System.out.println("预览");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PreviewLinkResponse("", "error"));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            System.out.println("预览1");
            // 根据 cid 和 workid 查找文件路径
            String filePath = homeworkService.findByCidSnoAndWorkid(cid, sno, workid).getPath();
            if (filePath == null) {
                System.out.println("路径是空的");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreviewLinkResponse("", "error"));
            }

            // 替换路径中的反斜杠为正斜杠
            filePath = filePath.replace("\\", "/");
            System.out.println("预览2");
            // 获取当前 IP 地址
            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
                System.out.println("预览3");
            } catch (UnknownHostException e) {
                System.out.println("无法获取ip");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PreviewLinkResponse("", "error"));
            }

            // 生成预览链接，编码路径确保格式正确
            String previewUrl = "http://" + ipAddress + ":8080/api/assets/homework/pdf?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String newToken = JWTUtil.generateToken(userType, username);
            System.out.println(previewUrl);

            return ResponseEntity.ok(new PreviewLinkResponse(previewUrl, "success", newToken));
        }
        System.out.println("权限错误");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PreviewLinkResponse("", "error"));
    }

    // 生成预览链接(老师布置的作业)
    @GetMapping("/assign/preview")
    public ResponseEntity<PreviewLinkResponse> generateAssignPreviewLink(@RequestHeader Map<String, String> header,
                                                                         @RequestParam String cid,
                                                                         @RequestParam int workid) {
        System.out.println("预览");
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            System.out.println("预览");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PreviewLinkResponse("", "error"));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            System.out.println("预览1");
            // 根据 cid 和 workid 查找文件路径
            String filePath = commitService.searchPathByCidAndWorkid(cid, workid);
            if (filePath == null) {
                System.out.println("路径是空的");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PreviewLinkResponse("", "error"));
            }

            // 替换路径中的反斜杠为正斜杠
            filePath = filePath.replace("\\", "/");
            System.out.println("预览2");
            // 获取当前 IP 地址
            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
                System.out.println("预览3");
            } catch (UnknownHostException e) {
                System.out.println("无法获取ip");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PreviewLinkResponse("", "error"));
            }

            // 生成预览链接，编码路径确保格式正确
            String previewUrl = "http://" + ipAddress + ":8080/api/assets/homework/pdf?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String newToken = JWTUtil.generateToken(userType, username);
            System.out.println(previewUrl);

            return ResponseEntity.ok(new PreviewLinkResponse(previewUrl, "success", newToken));
        }
        System.out.println("权限错误");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PreviewLinkResponse("", "error"));
    }

}
