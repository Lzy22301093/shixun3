package com.icplatform.repositories;

import com.icplatform.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommitRepositories extends JpaRepository<Commit, Integer> {

    @Query("SELECT l FROM Commit l WHERE l.cid = :cid AND l.workid = :workid")
    Commit findByCidAndWorkId(@Param("cid") String cid, @Param("workid") int workid);
}
