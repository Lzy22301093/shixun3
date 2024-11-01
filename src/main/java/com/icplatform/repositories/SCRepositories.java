package com.icplatform.repositories;

import com.icplatform.CompositePrimaryKey.SCId;
import com.icplatform.entity.SC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SCRepositories extends JpaRepository<SC, SCId> {

    List<SC> findBySno(String sno);

    // 使用 cid 来统计选课的学生数量
    @Query("SELECT COUNT(s) FROM SC s WHERE s.cid = :cid")
    int countByCid(String cid);
}
