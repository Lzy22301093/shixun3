package com.icplatform.service;


import com.icplatform.entity.Commit;
import com.icplatform.repositories.CommitRepositories;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommitService {

    private CommitRepositories commitRepositories;

    public CommitService(CommitRepositories commitRepositories) {
        this.commitRepositories = commitRepositories;
    }

    //更新布置作业信息
    public void updateAssignHomework(LocalDateTime start, LocalDateTime end, int workid, String cid, String path, String cname){
        Commit commit = commitRepositories.findByCidAndWorkId(cid, workid);

        if(commit == null){
            throw new IllegalArgumentException("已布置的作业记录不存在");
        }
        commit.setStart(start);
        commit.setEnd(end);
        commit.setCname(cname);
        commit.setPath(path);
        commitRepositories.save(commit);
    }

    public void insertAssignHomework(LocalDateTime start, LocalDateTime end, int workid, String cid, String path, String cname) {
        Commit commit = new Commit();
        commit.setStart(start);
        commit.setEnd(end);
        commit.setWorkid(workid);
        commit.setCid(cid);
        commit.setPath(path);
        commit.setCname(cname);
        commitRepositories.save(commit);
    }

    public Commit findByCidAndWorkId(String cid, int workid) {
        return commitRepositories.findByCidAndWorkId(cid, workid);
    }

    public Commit save(Commit commit) {
        return commitRepositories.save(commit);
    }
}
