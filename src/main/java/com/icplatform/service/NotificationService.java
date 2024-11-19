package com.icplatform.service;

import com.icplatform.entity.Notification;
import com.icplatform.repositories.NotificationRepositories;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private NotificationRepositories notificationRepositories;

    public NotificationService(NotificationRepositories notificationRepositories) {
        this.notificationRepositories = notificationRepositories;
    }

    public List<Notification> searchByCid(String cid) {
        return notificationRepositories.findByCid(cid);
    }

    public void updateNotification(String name, String cid, String tno, String content, LocalDateTime time, int nid) {
        Notification notification = notificationRepositories.findByCidAndNid(cid, nid);
        if(notification != null) {
            notification.setName(name);
            notification.setContent(content);
            notification.setTno(tno);
            notification.setTime(time);
            notificationRepositories.save(notification);
        }else{
            throw new IllegalArgumentException("课程通知不存在");
        }
    }

    public void insertNotification(String name, String cid, String tno, String content, LocalDateTime time,int nid) {
        Notification notification = new Notification();
        notification.setCid(cid);
        notification.setName(name);
        notification.setContent(content);
        notification.setTime(time);
        notification.setNid(nid);
        notification.setTno(tno);
        notificationRepositories.save(notification);
    }

    public String deleteNotification(int nid, String cid) {
        Notification notification = notificationRepositories.findByCidAndNid(cid,nid);
        if(notification != null) {
            notificationRepositories.delete(notification);
            String result = "课程通知删除成功";
            return result;
        }else{
            String errorResult = "课程通知不存在";
            return errorResult;
        }
    }

    public void save(Notification notification) {
        notificationRepositories.save(notification);
    }

}
