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
    private Integer teacherscore;
    private Integer studentscore;
    private Double totalscore;
    private String path;
    private String sno;
    private String cno;
    private String cid;
    private int workid;
    private String reviestatus;
    private String comment;

    public Homework() {}

    public Homework(int id, String hname, LocalDateTime start, LocalDateTime end, LocalDateTime stime, Integer teacherscore, Integer studentscore, Double totalscore, String path, String sno, String cno, String cid, int workid, String reviestatus, String comment) {
        this.id = id;
        this.hname = hname;
        this.start = start;
        this.end = end;
        this.stime = stime;
        this.teacherscore = teacherscore;
        this.studentscore = studentscore;
        this.totalscore = totalscore;
        this.path = path;
        this.sno = sno;
        this.cno = cno;
        this.cid = cid;
        this.workid = workid;
        this.reviestatus = reviestatus;
        this.comment = comment;
    }

    public Integer getStudentscore() {
        return studentscore;
    }

    public void setStudentscore(Integer student_score) {
        this.studentscore = student_score;
    }

    public Double getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(Double total_score) {
        this.totalscore = total_score;
    }

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

    public Integer getTeacherscore() { return teacherscore; }
    public void setTeacherscore(Integer teacher_score) { this.teacherscore = teacher_score; }

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

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
