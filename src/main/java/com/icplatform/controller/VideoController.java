package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.dto.PreviewLinkResponse;
import com.icplatform.entity.Notification;
import com.icplatform.entity.Video;
import com.icplatform.service.VideoService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/video")
@CrossOrigin(origins = "*")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public Map<String, Object> uploadVideo(@RequestHeader Map<String, String> header,
                                           @RequestParam("video") MultipartFile video,
                                           @RequestParam("cid") String cid,
                                           @RequestParam("vid") int vid,
                                           @RequestParam("start") LocalDateTime start,
                                           @RequestParam("end") LocalDateTime end) throws IOException {

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
            String originalFilename = video.getOriginalFilename();
            String contentType = video.getContentType(); // 获取文件的 MIME 类型

            // 检查文件是否为视频类型
            if (contentType == null || !contentType.startsWith("video/")) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "上传失败：文件不是视频类型");
                return response;
            }

            String courseResourcePath = "E:/ICPlatformStorage/Video/" + cid;  // 使用正斜杠
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
            // 保存新文件
            video.transferTo(uploadHomework);

            try {
                videoService.updateVideoByCidAndVid(originalFilename, uploadHomework.getAbsolutePath().replace("\\", "/"), cid, vid, start, end);
                System.out.println("update");
            } catch (IllegalArgumentException e) {
                videoService.insertNewVideo(originalFilename, uploadHomework.getAbsolutePath().replace("\\", "/"), cid, vid, start, end);
                System.out.println("insert");
            }

            String newToken = JWTUtil.generateToken(userType, username);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("newToken", newToken);
            response.put("message", "上传成功");
            return response;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "权限错误");
        return response;
    }

    @PostMapping("/display")
    public Map<String, Object> displayVideo(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> disData){
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

        if (userType == 1 || userType == 0) {
            String cid = disData.get("cid");
            List<Video> map = videoService.searchByCid(cid);
            List<Map<String, Object>> list = new ArrayList<>();
            if(map != null){
                for(Video video : map) {

                    Map<String, Object> response = new HashMap<>();
                    response.put("vname", video.getVname());
                    response.put("start", video.getStart());
                    response.put("end", video.getEnd());
                    response.put("vid", video.getVid());
                    response.put("path", video.getPath());

                    list.add(response);
                }
                String newToken = JWTUtil.generateToken(userType,username);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message","成功");
                response.put("newToken", newToken);
                response.put("notificationList",list);
                return response;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message","权限错误");
        return response;
    }

    //生成预览链接
    @GetMapping("/preview")
    public ResponseEntity<PreviewLinkResponse> generatePreviewLink(@RequestHeader Map<String, String> header, @RequestParam String cid, @RequestParam int vid) {
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

            String filePath = videoService.searchByCidAndVid(cid,vid).getPath();
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
            String previewUrl = "http://" + ipAddress + ":8080/api/video/preview/video?filePath=" + URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String newToken = JWTUtil.generateToken(userType, username);
            System.out.println(previewUrl);

            return ResponseEntity.ok(new PreviewLinkResponse(previewUrl, "success", newToken));
        }
        System.out.println("权限错误");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new PreviewLinkResponse("", "error"));
    }

    //视频预览
    @GetMapping("/preview/video")
    public ResponseEntity<Resource> previewVideo(@RequestParam String filePath) {
        try {
            // 解码文件路径
            Path path = Paths.get(URLDecoder.decode(filePath, StandardCharsets.UTF_8.name()));
            Resource fileResource = new UrlResource(path.toUri());

            // 检查文件是否存在
            if (!fileResource.exists() || !fileResource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 动态获取文件的 MIME 类型
            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // 默认 MIME 类型
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName().toString() + "\"")
                    .body(fileResource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
