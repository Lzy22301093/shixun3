package com.icplatform.repositories;

import com.icplatform.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommitRepositories extends JpaRepository<Commit, Integer> {

    List<Commit> findByCid(String Cid);

    @Query("SELECT l FROM Commit l WHERE l.cid = :cid AND l.workid = :workid")
    Commit findByCidAndWorkId(@Param("cid") String cid, @Param("workid") int workid);

    boolean existsByWorkidAndCid(int workid, String Cid);
}
