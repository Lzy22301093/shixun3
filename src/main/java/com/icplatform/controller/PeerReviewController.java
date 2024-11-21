package com.icplatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.icplatform.dto.DownloadLinkResponse;
import com.icplatform.entity.AssignedHomework;
import com.icplatform.entity.Homework;
import com.icplatform.repositories.AssignedHomeworkRepositories;
import com.icplatform.repositories.HomeworkRepositories;
import com.icplatform.service.AssignedHomeworkService;
import com.icplatform.service.HomeworkService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class PeerReviewController {

    @Autowired
    private AssignedHomeworkService assignedHomeworkService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private HomeworkRepositories homeworkRepositories;

    @Autowired
    private AssignedHomeworkRepositories assignedHomeworkRepositories;

    // 分配并展示作业
    @PostMapping("/peerDisplay")
    public ResponseEntity<Map<String, Object>> assignAndDisplay(@RequestBody Map<String, String> request) {
        String reviewerSno = request.get("reviewerSno");
        String cid = request.get("cid");
        int workid = Integer.parseInt(request.get("workid")); // 获取作业ID

        // 随机分配作业给 reviewerSno
        assignedHomeworkService.assignRandomHomeworks(reviewerSno, cid, workid);

        // 查找分配的作业
        List<AssignedHomework> assignedHomeworks = assignedHomeworkService.findAssignedHomeworks(reviewerSno, cid, workid);
        List<Map<String, Object>> returnHomeworks = new ArrayList<>();

        for (AssignedHomework assignedHomework : assignedHomeworks) {
            String acid = assignedHomework.getCid();
            String arevieweeSno = assignedHomework.getRevieweeSno();
            int aworkid = assignedHomework.getWorkid();

            Homework h = homeworkService.findByCidSnoAndWorkid(acid, arevieweeSno, aworkid);

            if (h != null) {
                Map<String, Object> homeworkInfo = new HashMap<>();
                homeworkInfo.put("homeworkName", h.getHname());
                homeworkInfo.put("start", h.getStart());
                homeworkInfo.put("end", h.getEnd());
                homeworkInfo.put("submitTime", h.getStime());
                homeworkInfo.put("studentscore", h.getStudentscore());
                homeworkInfo.put("reviestatus", h.getReviestatus());
                homeworkInfo.put("revieweeSno",h.getSno());

                // 检查是否在提交时间范围内
                LocalDateTime now = LocalDateTime.now();
                if (now.isBefore(h.getStart()) || now.isAfter(h.getEnd())) {
                    homeworkInfo.put("submitStatus", "not submitted");
                } else {
                    homeworkInfo.put("submitStatus", "can submission");
                }

                returnHomeworks.add(homeworkInfo);
            }
        }

        // 创建新的token
        String newToken = JWTUtil.generateToken(1, reviewerSno); // 假设 userType 为1表示老师身份
        Map<String, Object> response = new HashMap<>();
        response.put("homeworkList", returnHomeworks);
        response.put("newToken", newToken);
        response.put("status", "success");

        return ResponseEntity.ok()
                .header("status", "success")
                .body(response);
    }

    /*// 下载已分配的作业
    @GetMapping("/peerDownload")
    public ResponseEntity<Resource> downloadAssignedHomework(@RequestParam String reviewerSno, @RequestParam String cid, @RequestParam int workid) {

        // 查询分配的作业列表
        List<AssignedHomework> assignedHomeworks = assignedHomeworkService.findAssignedHomeworks(reviewerSno, cid, workid);

        // 检查是否有分配的作业
        if (assignedHomeworks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("status", "error")
                    .build();
        }

        // 创建一个临时 ZIP 文件，用于存储多个作业文件
        File zipFile;
        try {
            zipFile = File.createTempFile("homework_files", ".zip");
            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
                // 遍历所有分配的作业并添加到 ZIP 文件中
                for (AssignedHomework assignedHomework : assignedHomeworks) {
                    String ano_cid = assignedHomework.getCid();
                    String ano_revieweeSno = assignedHomework.getRevieweeSno();
                    int ano_workid = assignedHomework.getWorkid();

                    // 获取文件资源
                    Resource fileResource = homeworkService.getHomeworkFile(ano_cid, ano_workid, ano_revieweeSno);
                    if (fileResource != null && fileResource.exists()) {
                        // 将文件添加到 ZIP 输出流中
                        try (InputStream fileIn = fileResource.getInputStream()) {
                            ZipEntry zipEntry = new ZipEntry(fileResource.getFilename());
                            zipOut.putNextEntry(zipEntry);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fileIn.read(buffer)) >= 0) {
                                zipOut.write(buffer, 0, length);
                            }
                            zipOut.closeEntry();
                        }
                    }
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("status", "error")
                    .body(null);
        }

        // 将 ZIP 文件作为资源返回
        Resource resource = new FileSystemResource(zipFile);
        HttpHeaders headers = new HttpHeaders();

        // 设置 Content-Disposition 响应头
        String filename = URLEncoder.encode(zipFile.getName(), StandardCharsets.UTF_8);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        headers.add("status", "success");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }*/

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
    @GetMapping("/generatePeerDownloadLink")
    public ResponseEntity<DownloadLinkResponse> generateDownloadLink(@RequestHeader Map<String, String> header,
                                                                     @RequestParam String revieweeSno,
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
            Homework homework = homeworkService.findByCidSnoAndWorkid(cid, revieweeSno, workid);
            String filePath = homework.getPath();
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
            String downloadUrl = "http://" + ipAddress + ":8080/api/student/download?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            System.out.println(downloadUrl);

            // 生成新的 token
            String newToken = JWTUtil.generateToken(userType, username);

            return ResponseEntity.ok(new DownloadLinkResponse(downloadUrl, "success", newToken));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new DownloadLinkResponse("", "error"));
    }

    //提交作业评分
    @PostMapping("/submitReview")
    public Map<String, Object> submitReview(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> reviewData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String reviewerSno = reviewData.get("reviewerSno");
        String revieweeSno = reviewData.get("revieweeSno"); // 被评分学生的ID
        String cid = reviewData.get("cid");
        int score = Integer.parseInt(reviewData.get("score")); // 评分
        int workid = Integer.parseInt(reviewData.get("workid")); // 作业ID

        // 查找作业是否存在对应的记录
        Homework existingHomework = homeworkRepositories.findByCidAndSnoAndWorkid(cid,revieweeSno,workid);
        AssignedHomework assignedHomework = assignedHomeworkRepositories.findByTwoSnoAndCidAndWorkid(reviewerSno,revieweeSno,cid,workid);

        if (existingHomework != null) {
            // 如果记录已存在，更新评分
            existingHomework.setStudentscore(score);
            assignedHomework.setStudentscore(score);
            if(homeworkService.searchTeacherScoreByCidSnoAndWorkid(cid, revieweeSno, workid) != 0){
                existingHomework.setTotalscore(assignedHomeworkService.calculateFinalScore(reviewerSno,revieweeSno,cid,workid));
            }
            homeworkRepositories.save(existingHomework);
            response.put("status", "success");
            response.put("message", "评分更新成功");
        } else {
            response.put("status", "error");
            response.put("message", "没有找到对应的作业记录");
        }

        return response;
    }

}
