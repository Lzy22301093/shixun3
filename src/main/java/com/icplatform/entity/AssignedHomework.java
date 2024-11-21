package com.icplatform.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignedhomework")
public class AssignedHomework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 让数据库自动生成主键
    private Integer assignedId;

    private String reviewerSno;
    private String revieweeSno;
    private String cid;
    private Integer workid;
    private Integer studentscore;

    public AssignedHomework() {
    }

    public AssignedHomework(Integer assignedId, String reviewerSno, String revieweeSno, String cid, Integer workid, Integer studentscore) {
        this.assignedId = assignedId;
        this.reviewerSno = reviewerSno;
        this.revieweeSno = revieweeSno;
        this.cid = cid;
        this.workid = workid;
        this.studentscore = studentscore;
    }

    // Getters and Setters
    public Integer getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(Integer assignedId) {
        this.assignedId = assignedId;
    }

    public String getReviewerSno() {
        return reviewerSno;
    }

    public void setReviewerSno(String reviewerSno) {
        this.reviewerSno = reviewerSno;
    }

    public String getRevieweeSno() {
        return revieweeSno;
    }

    public void setRevieweeSno(String revieweeSno) {
        this.revieweeSno = revieweeSno;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getWorkid() {
        return workid;
    }

    public void setWorkid(Integer workid) {
        this.workid = workid;
    }

    public Integer getStudentscore() {
        return studentscore;
    }

    public void setStudentscore(Integer studentscore) {
        this.studentscore = studentscore;
    }
}