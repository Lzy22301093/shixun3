package com.icplatform.repositories;

import com.icplatform.entity.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepositories extends CrudRepository<Notification, Integer> {

    Notification findById(int id);

    List<Notification> findByCid(String cid);

    Notification findByCidAndNid(String cid, int nid);

}
