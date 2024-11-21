package com.icplatform.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "peer_review")
public class PeerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 让数据库自动生成主键
    private int assignmentId;
    private String evaluatorId; // 评分者学号
    private String evaluateeId; // 被评分者学号
    private int score; // 评分（百分制）

    public PeerReview () {}

    public PeerReview(int AssignmentId, String evaluatorId, String evaluateeId, int score) {
        this.assignmentId = AssignmentId;
        this.evaluatorId = evaluatorId;
        this.evaluateeId = evaluateeId;
        this.score = score;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int workId) {
        this.assignmentId = workId;
    }

    public String getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(String evaluatorSno) {
        this.evaluatorId = evaluatorSno;
    }

    public String getEvaluateeId() {
        return evaluateeId;
    }

    public void setEvaluateeId(String evaluateeId) {
        this.evaluateeId = evaluateeId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}

