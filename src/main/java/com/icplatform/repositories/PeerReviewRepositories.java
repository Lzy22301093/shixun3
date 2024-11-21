package com.icplatform.repositories;
import com.icplatform.entity.PeerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PeerReviewRepositories extends JpaRepository<PeerReview, Integer> {

    // 根据作业 ID 获取所有互评记录
    List<PeerReview> findByAssignmentId(int assignmentId);

    // 根据作业 ID 和被评估者 ID 获取互评记录
    List<PeerReview> findByAssignmentIdAndEvaluateeId(int assignmentId, String evaluateeId);

    // 根据作业 ID 统计互评数量
    @Query("SELECT COUNT(pr) FROM PeerReview pr WHERE pr.assignmentId = :assignmentId")
    int countByAssignmentId(@Param("assignmentId") int assignmentId);

    // 根据作业 ID 和被评估者 ID 统计互评数量
    @Query("SELECT COUNT(pr) FROM PeerReview pr WHERE pr.assignmentId = :assignmentId AND pr.evaluateeId = :evaluateeId")
    int countByAssignmentIdAndEvaluateeId(@Param("assignmentId") int assignmentId, @Param("evaluateeId") String evaluateeId);

    // 按 assignmentId 分组统计互评情况
    @Query("SELECT pr.assignmentId, COUNT(pr) FROM PeerReview pr GROUP BY pr.assignmentId")
    List<Object[]> countPeerReviewByAssignmentId();

    @Query("SELECT pr FROM PeerReview pr WHERE pr.evaluatorId = :evaluatorId AND pr.evaluateeId = :evaluateeId")
    PeerReview findByEvaluatorIdAndEvaluateeId(@Param("evaluatorId") String evaluatorId, @Param("evaluateeId") String evaluateeId);
}



