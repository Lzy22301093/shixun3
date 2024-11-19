package com.icplatform.controller;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.reflect.TypeToken;
import com.icplatform.entity.Notification;
import com.icplatform.service.NotificationService;
import com.icplatform.utils.GsonUtil;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    //课程通知列表
    @PostMapping("/display")
    public Map<String, Object> displayNotification(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> disData) {//cid
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
            List<Notification> map = notificationService.searchByCid(cid);
            List<Map<String, Object>> list = new ArrayList<>();
            if(map != null){
                for(Notification notification : map) {

                    Map<String, Object> response = new HashMap<>();
                    response.put("cid", cid);
                    response.put("name", notification.getName());
                    response.put("content", notification.getContent());
                    response.put("time", notification.getTime());
                    response.put("tno", notification.getTno());
                    response.put("nid", notification.getNid());

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

    // 上传课程通知
    @PostMapping("/upload")
    public Map<String, Object> uploadNotification(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> notificationData) throws IOException {
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
            String tno = notificationData.get("tno");
            int nid = Integer.valueOf(notificationData.get("nid"));
            String content = notificationData.get("content");

            // 获取当前时间
            LocalDateTime time = LocalDateTime.now();

            System.out.println(content);

            try {
                notificationService.updateNotification(name, cid, tno, content, time, nid);
            } catch (IllegalArgumentException e) {
                notificationService.insertNotification(name, cid, tno, content, time, nid);
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
    @PostMapping("/delete")
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

            Type listOfStringType = new TypeToken<List<String>>() {}.getType();
            Type listOfIntegerType = new TypeToken<List<Integer>>() {}.getType();

            List<String> cids = GsonUtil.fromJson(GsonUtil.toJson(deleteData.get("cid")), listOfStringType);
            List<Double> nidDoubles = GsonUtil.fromJson(GsonUtil.toJson(deleteData.get("nid")), new TypeToken<List<Double>>() {}.getType());

            // 转换 List<Double> 为 List<Integer>
            List<Integer> nids = nidDoubles.stream()
                    .map(Double::intValue)  // 将 Double 转为 Integer
                    .collect(Collectors.toList());

            if (cids == null || nids == null || cids.size() != nids.size()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "传入的cid和nid数量不匹配");
                return response;
            }

            for (int i = 0; i < nids.size(); i++) {
                int nid = nids.get(i);
                String cid = cids.get(i);
                notificationService.deleteNotification(nid,cid);
            }
            String newToken = JWTUtil.generateToken(userType, username);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "课程通知删除成功");
            response.put("newToken", newToken);
            return response;

        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "删除失败用户权限不足");
        return response;
    }

}
