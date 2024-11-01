package com.icplatform.repositories;


import com.icplatform.entity.Homework;
import com.icplatform.entity.Teaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeachingRepositories extends JpaRepository<Teaching, String> {

    @Query("SELECT t.cid FROM Teaching t WHERE t.tno = :tno")
    List<String> findCidByTno(@Param("tno") String tno);

}
