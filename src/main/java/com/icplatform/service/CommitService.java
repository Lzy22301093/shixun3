package com.icplatform.service;


import com.icplatform.entity.Assets;
import com.icplatform.entity.Commit;
import com.icplatform.entity.Homework;
import com.icplatform.repositories.CommitRepositories;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommitService {

    private CommitRepositories commitRepositories;

    public CommitService(CommitRepositories commitRepositories) {
        this.commitRepositories = commitRepositories;
    }

    //更新布置作业信息
    public void updateAssignHomework(LocalDateTime start, LocalDateTime end, int workid, String cid, String path, String cname, String content, int fullmark, int publish, int publishScore){
        Commit commit = commitRepositories.findByCidAndWorkId(cid, workid);

        if(commit == null){
            throw new IllegalArgumentException("已布置的作业记录不存在");
        }
        commit.setStart(start);
        commit.setEnd(end);
        commit.setCname(cname);
        commit.setPath(path);
        commit.setContent(content);
        commit.setFullmark(fullmark);
        commit.setPublish(publish);
        commit.setPublishscore(publishScore);
        commitRepositories.save(commit);
    }

    public void insertAssignHomework(LocalDateTime start, LocalDateTime end, int workid, String cid, String path, String cname, String content, int fullmark) {
        Commit commit = new Commit();
        commit.setStart(start);
        commit.setEnd(end);
        commit.setWorkid(workid);
        commit.setCid(cid);
        commit.setPath(path);
        commit.setCname(cname);
        commit.setContent(content);
        commit.setFullmark(fullmark);
        commitRepositories.save(commit);
        commitRepositories.save(commit);
    }

    public Commit findByCidAndWorkId(String cid, int workid) {
        return commitRepositories.findByCidAndWorkId(cid, workid);
    }

    public List<Commit> findByCid(String cid) {
        return commitRepositories.findByCid(cid);
    }

    public Commit save(Commit commit) {
        return commitRepositories.save(commit);
    }

    public boolean checkWorkIdExist(int workid, String cid) {
        return commitRepositories.existsByWorkidAndCid(workid, cid);
    }

    public void updatePublish(int publish, String cid, int workid) {
        Commit commit = commitRepositories.findByCidAndWorkId(cid, workid);

        if(commit != null){
            commit.setPublish(publish);
            commitRepositories.save(commit);
        }
    }
    public void updatePublishScore(int publishscore, String cid, int workid) {
        Commit commit = commitRepositories.findByCidAndWorkId(cid, workid);

        if(commit != null){
            commit.setPublishscore(publishscore);
            commitRepositories.save(commit);
        }
    }

    public String searchPathByCidAndWorkid(String cid,int workid){
        return commitRepositories.findByCidAndWorkId(cid,workid).getPath();
    }

    public String deleteCommitByCidAndWorkid(String cid, int workid) {
        Commit commit  = commitRepositories.findByCidAndWorkId(cid ,workid);
        if(commit != null) {
            commitRepositories.delete(commit);
            String result = "数据库记录删除成功";
            return result;
        }else{
            String result = "数据库记录不存在";
            return result;
        }
    }
}
