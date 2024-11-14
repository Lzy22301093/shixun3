package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.reflect.TypeToken;
import com.icplatform.dto.FileUploadResponse;
import com.icplatform.dto.PreviewLinkResponse;
import com.icplatform.entity.Course;
import com.icplatform.entity.Notification;
import com.icplatform.entity.SC;
import com.icplatform.entity.Teacher;
import com.icplatform.service.*;
import com.icplatform.utils.GsonUtil;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SCService scService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeachingService teachingService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("")
    public Map<String, Object> courseList(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> numberData) {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "token超时");
            response.put("status", "error");
            return response;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        String sno = numberData.get("username"); // 学生学号
        String tno = numberData.get("username"); // 教师工号（可能是同一字段）

        if (userType == 0) {
            List<SC> scList = scService.searchBySno(sno);
            if (!scList.isEmpty()) {
                List<Map<String, Object>> courseList = new ArrayList<>();

                for (SC sc : scList) {
                    String cid = sc.getCid();
                    String cno = sc.getCno();
                    Course course = courseService.findByCidAndCno(cid, cno);
                    if (course != null) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("cid", cid);
                        response.put("cno", cno);
                        response.put("cname", course.getCname());

                        courseList.add(response);
                    }
                }

                String newToken = JWTUtil.generateToken(userType, username);

                // 返回结果列表
                Map<String, Object> result = new HashMap<>();
                result.put("courseList", courseList);
                result.put("newToken", newToken);
                result.put("status", "success");
                return result;

            } else {
                // 没有找到任何 SC 记录
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "学生本学期无选课信息");
                errorResponse.put("status", "error");
                return errorResponse;
            }

        } else if (userType == 1) {
            // 处理教师的情况
            List<String> cidList = teachingService.searchCidByTno(tno); // 查询 teaching 表获取所有 cid
            List<Map<String, Object>> courseList = new ArrayList<>();

            for (String cid : cidList) {
                Course course = courseService.findByCid(cid); // 根据 cid 查询 course 表
                if (course != null) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("cid", cid);
                    response.put("cno", course.getCno());
                    response.put("cname", course.getCname());

                    courseList.add(response);
                }
            }

            String newToken = JWTUtil.generateToken(userType, username);

            // 返回结果列表
            Map<String, Object> result = new HashMap<>();
            result.put("courseList", courseList);
            result.put("newToken", newToken);
            result.put("status", "success");
            return result; // 添加了返回语句

        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "用户类型错误");
            errorResponse.put("status", "error");
            return errorResponse;
        }
    }

    //课程信息
    @PostMapping("/info")
    public Map<String,Object> CourseInformation(@RequestHeader Map<String,String> header,@RequestBody Map<String, String> infoData){
        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        }catch (Exception e){
            return null;
        }
        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 0 || userType == 1) {
            // 根据cid和cno查询课程信息
            String cid = infoData.get("cid");
            Course course = courseService.findByCid(cid);
            if (course != null) {
                // 获取课程的tno
                String tno = course.getTno();

                // 根据tno查询教师信息
                Teacher teacher = teacherService.searchByTno(tno);
                if (teacher != null) {
                    // 组装返回数据
                    Map<String, Object> courseInfo = new HashMap<>();
                    courseInfo.put("cid", course.getCid());
                    courseInfo.put("cno", course.getCno());
                    courseInfo.put("cname", course.getCname());
                    courseInfo.put("description",course.getDescription());

                    // 添加教师信息
                    Map<String, Object> teacherInfo = new HashMap<>();
                    teacherInfo.put("tname", teacher.getTname());
                    teacherInfo.put("temail", teacher.getTemail());
                    teacherInfo.put("gender", teacher.getGender());
                    teacherInfo.put("major", teacher.getMajor());
                    teacherInfo.put("title", teacher.getTitle());

                    courseInfo.put("teacherInfo", teacherInfo);

                    // 生成新token
                    String newToken = JWTUtil.generateToken(userType, username);

                    // 返回结果
                    Map<String, Object> result = new HashMap<>();
                    result.put("status","success");
                    result.put("courseInfo", courseInfo);
                    result.put("newToken", newToken);

                    return result;
                } else {
                    // 没有找到教师信息
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("status", "error");
                    errorResponse.put("error", "未找到对应教师信息");
                    return errorResponse;
                }
            } else {
                // 没有找到课程信息
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("error", "未找到课程信息");
                return errorResponse;
            }
        } else {
            // 用户类型错误
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("error", "用户类型错误");
            return errorResponse;
        }
    }

    //课程大纲
    @GetMapping("/outline")
    public ResponseEntity<PreviewLinkResponse> generateOutlineLink(@RequestHeader Map<String, String> header, @RequestParam String cid) {
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
            Course course = courseService.findByCid(cid);

            String filePath = course.getOutline();
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

    //课程大纲
    @GetMapping("/calendar")
    public ResponseEntity<PreviewLinkResponse> generateCalendarLink(@RequestHeader Map<String, String> header, @RequestParam String cid) {
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
            Course course = courseService.findByCid(cid);

            String filePath = course.getCalendar();
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

    //上传课程大纲并更新数据库
    @PostMapping("/outline/upload")
    public FileUploadResponse uploadOutline(@RequestHeader Map<String, String> header, @RequestParam("file") MultipartFile file, @RequestParam("cid") String cid) throws IOException {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return new FileUploadResponse("error", "token已被清除或已过期");
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {
            String originalFilename = file.getOriginalFilename();

            // 设置文件夹路径
            String courseResourcePath = "E:/ICPlatformStorage/CourseOutline/" + cid;  // 使用正斜杠
            File courseDir = new File(courseResourcePath);

            // 如果文件夹不存在则创建
            if (!courseDir.exists()) {
                courseDir.mkdirs();
            }
            // 设置上传文件路径
            File uploadFile = new File(courseDir, originalFilename);

            // 如果文件已存在，删除旧文件
            if (uploadFile.exists()) {
                uploadFile.delete();
            }

            // 保存新文件
            file.transferTo(uploadFile);

            try {
                courseService.updateOutlineByCid(uploadFile.getAbsolutePath().replace("\\", "/"), cid);
            } catch (IllegalArgumentException e) {
                return new FileUploadResponse("error", "课程不存在");
            }

            String newToken = JWTUtil.generateToken(userType, username);
            return new FileUploadResponse("success", "上传成功",newToken);

        } else {
            return new FileUploadResponse("error", "用户权限不足");
        }
    }

    //上传课程大纲并更新数据库
    @PostMapping("/calendar/upload")
    public FileUploadResponse uploadCalendar(@RequestHeader Map<String, String> header, @RequestParam("file") MultipartFile file, @RequestParam("cid") String cid) throws IOException {

        String token = header.get("token");
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            return new FileUploadResponse("error", "token已被清除或已过期");
        }

        String username = decodedJWT.getClaim("username").asString();
        int userType = decodedJWT.getClaim("usertype").asInt();

        if (userType == 1) {
            String originalFilename = file.getOriginalFilename();

            // 设置文件夹路径
            String courseResourcePath = "E:/ICPlatformStorage/CourseCalendar/" + cid;  // 使用正斜杠
            File courseDir = new File(courseResourcePath);

            // 如果文件夹不存在则创建
            if (!courseDir.exists()) {
                courseDir.mkdirs();
            }
            // 设置上传文件路径
            File uploadFile = new File(courseDir, originalFilename);

            // 如果文件已存在，删除旧文件
            if (uploadFile.exists()) {
                uploadFile.delete();
            }

            // 保存新文件
            file.transferTo(uploadFile);

            try {
                courseService.updateCalendarByCid(uploadFile.getAbsolutePath().replace("\\", "/"), cid);
            } catch (IllegalArgumentException e) {
                return new FileUploadResponse("error", "课程不存在");
            }

            String newToken = JWTUtil.generateToken(userType, username);
            return new FileUploadResponse("success", "上传成功",newToken);

        } else {
            return new FileUploadResponse("error", "用户权限不足");
        }
    }

    //上传课程通知
    @PostMapping("/notification/upload")
    public Map<String, Object> uploadNotification(@RequestHeader Map<String, String> header,@RequestBody Map<String, String> notificationData) throws IOException {
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
            String name = notificationData.get("name");
            String cid = notificationData.get("cid");
            int nid = Integer.valueOf(notificationData.get("nid"));
            String content = notificationData.get("content");

            System.out.println(content);

            try {
                notificationService.updateNotification(name,cid,nid,content);
            } catch (IllegalArgumentException e) {
                notificationService.insertNotification(name,cid,nid,content);
            }
            String newToken = JWTUtil.generateToken(userType, username);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "课程通知上传成功");
            response.put("newToken", newToken);
            return response;
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "上传失败,用户权限不足");
            return response;
        }
    }

    //删除课程通知
    @PostMapping("/notification/delete")
    public Map<String, Object> deleteNotification(@RequestHeader Map<String, String> header, @RequestBody Map<String, Object> deleteData) {
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

            // 使用 TypeToken 获取 List 类型
            Type cidListType = new TypeToken<List<String>>() {}.getType();
            Type nidListType = new TypeToken<List<Integer>>() {}.getType();

            // 使用 GsonUtil 从 JSON 转换为 List<String>
            List<String> cids = GsonUtil.fromJson(deleteData.get("cid").toString(), cidListType);
            List<Integer> nids = GsonUtil.fromJson(deleteData.get("nid").toString(), nidListType);

            System.out.println(nids.toString());
            System.out.println(cids);

            if (cids != null && nids != null && cids.size() == nids.size()) {
                // 配对 cid 和 nid
                for (int i = 0; i < cids.size(); i++) {
                    String cid = cids.get(i);
                    int nid = Integer.valueOf(nids.get(i));

                    Notification notification = notificationService.searchByCidAndNid(cid, nid);

                    if (notification != null) {
                        notificationService.deleteNotification(cid, nid);
                    }
                }
                String newToken = JWTUtil.generateToken(userType, username);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "课程通知删除成功");
                response.put("newToken", newToken);
                return response;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "删除失败用户权限不足");
        return response;
    }

   /* //上传视频
    @PostMapping("/video/upload")
    public Map<String, Object> uploadVideo(@RequestHeader Map<String, String> header,
                                           @RequestParam("videoFile") MultipartFile videoFile,
                                           @RequestParam("cid") String cid,
                                           @RequestParam("vid") int vid) throws IOException {
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

        }
    }*/

}


