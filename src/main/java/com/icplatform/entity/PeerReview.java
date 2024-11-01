package com.icplatform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class PeerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int AssignmentId; // 作业ID，关联Homework表
    private String evaluatorId; // 评分者学号
    private String evaluateeId; // 被评分者学号
    private int score; // 评分（十分制）
    private LocalDateTime timestamp; // 评分时间戳

    public PeerReview () {}

    public PeerReview(int id, int AssignmentId, String evaluatorId, String evaluateeId, int score, LocalDateTime timestamp) {
        this.id = id;
        this.AssignmentId = AssignmentId;
        this.evaluatorId = evaluatorId;
        this.evaluateeId = evaluateeId;
        this.score = score;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignmentId() {
        return AssignmentId;
    }

    public void setAssignmentId(int workId) {
        this.AssignmentId = workId;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

