package com.icplatform.service;

import com.icplatform.entity.Homework;
import com.icplatform.entity.Video;
import com.icplatform.repositories.VideoRepositories;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    //更新作业
    public void updateVideoByCidAndVid(String vname, String path, String cid, int vid, LocalDateTime start, LocalDateTime end) {
        Video video = videoRepositories.findByCidAndVid(cid, vid);
        if (video == null) {
            throw new IllegalArgumentException("视频不存在");
        }
        video.setVname(vname);
        video.setPath(path);
        video.setStart(start);
        video.setEnd(end);
        videoRepositories.save(video);
    }

    //插入作业
    public void insertNewVideo(String vname, String path, String cid, int vid, LocalDateTime start, LocalDateTime end) {
        Video video = new Video();
        video.setVname(vname);
        video.setPath(path);
        video.setCid(cid);
        video.setVid(vid);
        video.setStart(start);
        video.setEnd(end);
        videoRepositories.save(video);
    }

}
