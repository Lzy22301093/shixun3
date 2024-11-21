package com.icplatform.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "discussions")
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 主键，自增长

    @Column(nullable = false, unique = true) // 确保每个讨论有唯一的 UUID
    private String discussionUuid;  // 讨论的唯一标识符（UUID）

    private String title;  // 讨论标题
    private String content;  // 讨论内容

    private String creatorSno;  // 发起人学号

    private int likeCount;  // 点赞数
    private int commentCount;  // 评论数
    private int collectCount;  // 收藏数

    // 默认构造函数
    public Discussion() {
        this.discussionUuid = UUID.randomUUID().toString(); // 在创建时自动生成UUID
    }

    // 带参构造函数
    public Discussion(int id, String title, String content, String creatorSno, int likeCount, int commentCount, int collectCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creatorSno = creatorSno;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.collectCount = collectCount;
        this.discussionUuid = UUID.randomUUID().toString(); // 创建时生成UUID
    }

    // getter 和 setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscussionUuid() {
        return discussionUuid;
    }

    public void setDiscussionUuid(String discussionUuid) {
        this.discussionUuid = discussionUuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorSno() {
        return creatorSno;
    }

    public void setCreatorSno(String creatorSno) {
        this.creatorSno = creatorSno;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }
}
