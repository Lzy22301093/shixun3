package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.dto.DownloadLinkResponse;
import com.icplatform.dto.FileUploadResponse;
import com.icplatform.entity.Commit;
import com.icplatform.entity.Homework;
import com.icplatform.service.CourseService;
import com.icplatform.service.HomeworkService;
import com.icplatform.service.CommitService;
import com.icplatform.service.SCService;
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
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
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

    //上传作业
    @PostMapping("upload")
    public FileUploadResponse uploadHomework(@RequestHeader Map<String, String> header, @RequestParam("homeworkFile") MultipartFile homeworkFile, @RequestParam("cid") String cid, @RequestParam("sno") String sno, @RequestParam("workid") int workid, @RequestParam("reviestatus") String reviestatus) throws IOException {

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
    public Map<String, String> assignHomework(@RequestHeader Map<String, String> header, @RequestParam("homework") MultipartFile homework, @RequestParam("start") String start, @RequestParam("end") String end, @RequestParam("workid") int workid, @RequestParam("cid") String cid, @RequestParam("content") String content, @RequestParam("fullmark") int fullmark) throws IOException {

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
            try {
                commitService.updateAssignHomework(startTime, endTime, workid, cid, filePath, originalFilename, content, fullmark);
            } catch (IllegalArgumentException e) {
                commitService.insertAssignHomework(startTime, endTime, workid, cid, filePath, originalFilename, content, fullmark);
            }

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


    //下载作业链接
    @GetMapping("/generateDownloadLink")
    public ResponseEntity<DownloadLinkResponse> generateDownloadLink(@RequestHeader Map<String, String> header, @RequestParam String homeworkName) {
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new DownloadLinkResponse("error", "无效的Token"));
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            String correctedFileName = null;

            // 解码文件名
            String decodedFname = null;
            try {
                decodedFname = URLDecoder.decode(homeworkName, StandardCharsets.UTF_8.name());
                // 将空格替换回加号
                correctedFileName = decodedFname.replace(" ", "+");
                System.out.println(correctedFileName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DownloadLinkResponse("error", "文件名解码失败"));
            }

            System.out.println("解码后的文件名: " + correctedFileName);
            String filePath = homeworkService.searchPathByHname(homeworkName);
            System.out.println("文件路径: " + filePath);

            if (filePath == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DownloadLinkResponse("error", "文件未找到"));
            }

            filePath = filePath.replace("\\", "/");

            String ipAddress;
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DownloadLinkResponse("error", "无法获取IP地址"));
            }

            String downloadUrl = "http://" + ipAddress + ":8080/api/homework/download?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String newToken = JWTUtil.generateToken(userType,username);

            return ResponseEntity.ok(new DownloadLinkResponse("success", downloadUrl,newToken));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DownloadLinkResponse("error", "用户权限不足"));
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
                    commitinfo.put("workid", "第" + workid + "次作业");
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


}
