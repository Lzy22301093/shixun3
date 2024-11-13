package com.icplatform.service;

import com.icplatform.entity.Notification;
import com.icplatform.repositories.NotificationRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private NotificationRepositories notificationRepositories;

    public NotificationService(NotificationRepositories notificationRepositories) {
        this.notificationRepositories = notificationRepositories;
    }

    public Notification searchByCidAndNid(String cid, int nid) {
        return notificationRepositories.findByCidAndNid(cid,nid);
    }

    public List<Notification> searchByCid(String cid) {
        return notificationRepositories.findByCid(cid);
    }

    public void updateNotification(String name, String cid, int nid, String content) {
        Notification notification = notificationRepositories.findByCidAndNid(cid,nid);
        if(notification != null) {
            notification.setName(name);
            notification.setContent(content);
            notificationRepositories.save(notification);
        }else{
            throw new IllegalArgumentException("课程通知不存在");
        }
    }

    public void insertNotification(String name, String cid, int nid, String content) {
        Notification notification = new Notification();
        notification.setCid(cid);
        notification.setNid(nid);
        notification.setName(name);
        notification.setContent(content);
        notificationRepositories.save(notification);
    }

    public String deleteNotification(String cid, int nid) {
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
