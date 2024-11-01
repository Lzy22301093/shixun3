package com.icplatform.service;

import com.icplatform.entity.PeerReview;
import com.icplatform.repositories.PeerReviewRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeerReviewService {

    @Autowired
    private PeerReviewRepositories peerReviewRepository;

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
}


