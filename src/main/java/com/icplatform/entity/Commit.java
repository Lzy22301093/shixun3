package com.icplatform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Commit {

    @Id
    private int id;

    private String cid;
    private LocalDateTime start;
    private LocalDateTime end;
    private int workid;
    private String path;
    private String cname;
    private String content;
    private int fullmark;
    private int publish;
    private int publishscore;

    public Commit() {}

    public void setWorkid(int workid) {
        this.workid = workid;
    }
    public int getWorkid() {
        return workid;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCid() {
        return cid;
    }
    public void setCid(String cid) {
        this.cid = cid;
    }
    public LocalDateTime getStart() {
        return start;
    }
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    public LocalDateTime getEnd() {
        return end;
    }
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getFullmark() {
        return fullmark;
    }
    public void setFullmark(int fullmark) {
        this.fullmark = fullmark;
    }
    public int getPublish() {
        return publish;
    }
    public void setPublish(int publish) {
        this.publish = publish;
    }
    public int getPublishscore() {
        return publishscore;
    }
    public void setPublishscore(int publishscore) {
        this.publishscore = publishscore;
    }
}
