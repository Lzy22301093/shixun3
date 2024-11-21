package com.icplatform.repositories;

import com.icplatform.entity.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionRepositories extends JpaRepository<Discussion, Integer> {
    // 根据讨论的 Sno 和 UUID 查找讨论
    @Query("SELECT d FROM Discussion d WHERE d.creatorSno = :sno AND d.discussionUuid = :discussionUuid")
    Discussion findBySnoAndDiscussionUuid(String sno,String discussionUuid);

    // 根据发起人学号（creatorSno）查找该发起人的所有讨论
    @Query("SELECT d FROM Discussion d WHERE d.creatorSno = :creatorSno")
    List<Discussion> findByCreatorSno(String creatorSno);

    @Query("SELECT d FROM Discussion d WHERE d.title LIKE %:title%")
    List<Discussion> findByTitleContaining(String title);  // 根据标题关键字查找讨论
}
