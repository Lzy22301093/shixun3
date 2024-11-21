package com.icplatform.repositories;

import com.icplatform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositories extends JpaRepository<Comment, Integer> {
    // 根据讨论 UUID 查找所有评论
    @Query("SELECT c FROM Comment c WHERE c.discussionUuid = :discussionUuid")
    List<Comment> findByDiscussionUuid(String discussionUuid);
}
