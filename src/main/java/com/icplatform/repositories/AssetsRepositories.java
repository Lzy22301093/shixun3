package com.icplatform.repositories;

import com.icplatform.entity.Assets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetsRepositories extends JpaRepository<Assets, Integer> {

    // 根据文件名查询文件路径
    default String findTpathByFname(String fname) {
        Assets asset = findByFname(fname);
        return (asset != null) ? asset.getTpath() : null;
    }

    @Query("SELECT a.tpath FROM Assets a WHERE a.tpath LIKE CONCAT(:startWith, '%')")
    List<String> findByTpathStartingWith(@Param("startWith") String startWith);

    // 按文件名查找文件记录
    Assets findByFname(String fname);
}
