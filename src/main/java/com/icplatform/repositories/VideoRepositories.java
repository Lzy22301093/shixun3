package com.icplatform.repositories;

import com.icplatform.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepositories extends JpaRepository<Video, Integer> {

    Video findByCidAndVid(String cid, int vid);

    List<Video> findByCid(String cid);

}
