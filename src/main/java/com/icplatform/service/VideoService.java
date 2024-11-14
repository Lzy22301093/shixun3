package com.icplatform.service;

import com.icplatform.entity.Video;
import com.icplatform.repositories.VideoRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    private VideoRepositories videoRepositories;

    public VideoService(VideoRepositories videoRepositories) {
        this.videoRepositories = videoRepositories;
    }

    public Video searchByCidAndVid(String cid, int vid) {
        return videoRepositories.findByCidAndVid(cid, vid);
    }

    public List<Video> searchByCid(String cid) {
        return videoRepositories.findByCid(cid);
    }
}
