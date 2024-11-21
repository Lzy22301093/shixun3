package com.icplatform.repositories;

import com.icplatform.entity.AssignedHomework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignedHomeworkRepositories extends JpaRepository<AssignedHomework, Long> {
    //查询单个
    @Query("SELECT a FROM AssignedHomework a WHERE a.reviewerSno = :reviewerSno AND a.revieweeSno = :revieweeSno " +
            "AND a.cid = :cid AND a.workid = :workid")
    AssignedHomework findByTwoSnoAndCidAndWorkid(@Param("reviewerSno") String reviewerSno,
                                                 @Param("revieweeSno") String revieweeSno,
                                                 @Param("cid") String cid,
                                                 @Param("workid") int workid);

    @Query("SELECT a FROM AssignedHomework a WHERE a.reviewerSno = :reviewerSno " +
            "AND a.cid = :cid AND a.workid = :workid")
    List<AssignedHomework> findByReviewerSnoAndCidAndWorkid(@Param("reviewerSno") String reviewerSno,
                                                 @Param("cid") String cid,
                                                 @Param("workid") int workid);

    @Query("SELECT COUNT(a) FROM AssignedHomework a WHERE a.reviewerSno = :reviewerSno AND a.cid = :cid AND a.workid = :workid")
    int countSubmittedBySnoAndCidAndWorkId(@Param("reviewerSno") String reviewerSno, @Param("cid") String cid, @Param("workid") int workid);

    // 根据评价人 Sno 查询其要评价的作业
    @Query("SELECT a FROM AssignedHomework a WHERE a.reviewerSno = :reviewerSno")
    List<AssignedHomework> findByReviewerSno(String reviewerSno);

    // 根据评价人 Sno 和课程信息查询
    @Query("SELECT a FROM AssignedHomework a WHERE a.reviewerSno = :reviewerSno AND a.cid = :cid")
    List<AssignedHomework> findByReviewerSnoAndCid(String reviewerSno, String cid);
}
