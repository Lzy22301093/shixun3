package com.icplatform.service;

import com.icplatform.entity.Homework;
import com.icplatform.entity.PeerReview;
import com.icplatform.repositories.AssignedHomeworkRepositories;
import com.icplatform.repositories.HomeworkRepositories;
import com.icplatform.repositories.PeerReviewRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.icplatform.entity.AssignedHomework;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class PeerReviewService {

    @Autowired
    private PeerReviewRepositories peerReviewRepository;

    @Autowired
    private AssignedHomeworkRepositories assignedHomeworkRepositories;

    @Autowired
    private HomeworkRepositories homeworkRepositories;


    public void submitReview(PeerReview review) {
        peerReviewRepository.save(review);
    }

    public void modifyReview(PeerReview review) {
        peerReviewRepository.save(review);
    }

    public List<PeerReview> getReviewsForStudent(int assignmentId, String evaluateeId) {
        return peerReviewRepository.findByAssignmentIdAndEvaluateeId(assignmentId, evaluateeId);
    }

    // 根据作业 ID 获取所有互评记录
    public List<PeerReview> getReviewsForAssignment(int assignmentId) {
        return peerReviewRepository.findByAssignmentId(assignmentId);
    }

    public double calculateFinalScore(int assignmentId, String evaluateeId, int teacherScore) {
        List<PeerReview> reviews = peerReviewRepository.findByAssignmentIdAndEvaluateeId(assignmentId, evaluateeId);

        if (reviews.size() < 2) {
            return teacherScore; // 不足两条评分直接返回老师评分
        }

        // 去掉最高和最低分
        int totalScore = 0;
        int maxScore = Integer.MIN_VALUE;
        int minScore = Integer.MAX_VALUE;

        for (PeerReview review : reviews) {
            totalScore += review.getScore();
            if (review.getScore() > maxScore) {
                maxScore = review.getScore();
            }
            if (review.getScore() < minScore) {
                minScore = review.getScore();
            }
        }

        // 计算平均分
        double averageScore = (totalScore - maxScore - minScore) / (reviews.size() - 2);

        // 加权计算
        return (averageScore * 0.3) + (teacherScore * 0.7);
    }

    public void assignRandomHomeworks(String reviewerSno, String cid, int workid) {
        // 1. 查询所有相关的作业
        List<Homework> allHomeworks = homeworkRepositories.findByCidAndWorkid(cid, workid);
        // 2. 随机选择作业并分配给 reviewerSno
        Random random = new Random();
        int assignCount = Math.min(allHomeworks.size(), 3);  // 分配最多3个作业
        if (assignedHomeworkRepositories.countSubmittedBySnoAndCidAndWorkId(reviewerSno,cid,workid) >= assignCount) return;
        for (int i = 0; i < assignCount; i++) {
            int randomIndex = random.nextInt(allHomeworks.size());
            Homework homework = allHomeworks.get(randomIndex);
            // 3. 创建并保存 AssignedHomework 实例
            AssignedHomework assignedHomework = new AssignedHomework();
            assignedHomework.setReviewerSno(reviewerSno);
            assignedHomework.setRevieweeSno(homework.getSno());  // 获取作业的学生学号
            assignedHomework.setCid(cid);
            assignedHomework.setWorkid(homework.getWorkid());
            AssignedHomework temp = assignedHomeworkRepositories.findByTwoSnoAndCidAndWorkid(reviewerSno, homework.getSno(), cid, homework.getWorkid());

            if (temp == null) {
                assignedHomeworkRepositories.save(assignedHomework);
            }
            // 4. 避免重复分配
            allHomeworks.remove(randomIndex);
        }
    }

    public List<AssignedHomework> findAssignedHomeworks(String reviewerSno,String cid,int workid) {
        return assignedHomeworkRepositories.findByReviewerSnoAndCidAndWorkid(reviewerSno,cid,workid);
    }
}


