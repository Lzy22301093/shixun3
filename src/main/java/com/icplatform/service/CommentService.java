package com.icplatform.service;

import com.icplatform.entity.Comment;
import com.icplatform.repositories.CommentRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepositories commentRepository;

    // 获取所有与指定讨论关联的评论
    public List<Comment> getCommentsByDiscussionUuid(String discussionUuid) {
        return commentRepository.findByDiscussionUuid(discussionUuid);
    }

    // 创建新评论或回复
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // 根据评论ID删除评论
    public void deleteCommentById(int commentId) {
        commentRepository.deleteById(commentId);
    }
}
