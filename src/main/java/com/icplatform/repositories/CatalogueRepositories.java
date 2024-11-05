package com.icplatform.repositories;

import com.icplatform.entity.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CatalogueRepositories extends JpaRepository<Catalogue, Integer> {

    @Query("SELECT c.path FROM Catalogue c WHERE c.path LIKE CONCAT(:rootPath, '%')")
    List<String> findEmptyFoldersByRootPath(@Param("rootPath") String rootPath);
}
