package com.icplatform.repositories;

import com.icplatform.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeworkRepositories extends JpaRepository<Homework, Integer> {

    List<Homework> findByCidAndSno(String cid, String sno);

    Homework findByHname(String hname);

    // 根据 cid 统计已提交的作业数量
    @Query("SELECT COUNT(h) FROM Homework h WHERE h.cid = :cid")
    int countByCid(@Param("cid") String cid);

    // 根据 cid 和 workid 统计已提交的作业数量
    @Query("SELECT COUNT(h) FROM Homework h WHERE h.cid = :cid AND h.workid = :workid")
    int countSubmittedByCidAndWorkId(@Param("cid") String cid, @Param("workid") int workid);

    // 按 workid 分组统计作业提交情况
    @Query("SELECT h.workid, COUNT(h) FROM Homework h WHERE h.cid = :cid GROUP BY h.workid")
    List<Object[]> countHomeworkByWorkId(@Param("cid") String cid);

    @Query("SELECT h FROM Homework h WHERE h.cid = :cid AND h.sno = :sno AND h.workid = :workid")
    Homework findByCidAndSnoAndWorkid(@Param("cid") String cid, @Param("sno") String sno, @Param("workid") int workid);

    @Query("SELECT h FROM Homework h WHERE h.cid = :cid AND h.workid = :workid")
    List<Homework> findByCidAndWorkid(@Param("cid") String cid, @Param("workid") int workid);

}
