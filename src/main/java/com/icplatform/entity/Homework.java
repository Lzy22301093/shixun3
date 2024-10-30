package com.icplatform.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Homework {

    @Id
    private int id;

    private String hname;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime stime;
    private Integer score;
    private String path;
    private String sno;
    private String cno;
    private String cid;
    private int workid;
    private String reviestatus;

    public Homework() {}


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getHname() { return hname; }
    public void setHname(String hname) { this.hname = hname; }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }

    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public LocalDateTime getStime() { return stime; }
    public void setStime(LocalDateTime stime) { this.stime = stime; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getSno() { return sno; }
    public void setSno(String sno) { this.sno = sno; }

    public String getCno() { return cno; }
    public void setCno(String cno) { this.cno = cno; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }

    public int getWorkid() { return workid; }
    public void setWorkid(int workid) { this.workid = workid; }

    public String getReviestatus() { return reviestatus; }
    public void setReviestatus(String reviestatus) { this.reviestatus = reviestatus; }
}
