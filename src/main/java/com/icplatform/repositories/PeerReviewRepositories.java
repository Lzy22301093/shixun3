package com.icplatform.repositories;
import com.icplatform.entity.PeerReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeerReviewRepositories extends JpaRepository<PeerReview, Integer> {
    List<PeerReview> findByAssignmentId(int assignmentId); // 根据作业 ID 获取互评记录
    List<PeerReview> findByAssignmentIdAndEvaluateeId(int assignmentId, String evaluateeId); // 其他查询方法
}

