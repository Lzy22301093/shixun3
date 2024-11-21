package com.icplatform.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 主键，自增长

    @Column(name = "discussion_uuid", nullable = false)
    private String discussionUuid;  // 所属讨论的 UUID

    @Column(nullable = false)
    private String content;  // 评论内容

    @Column(name = "creator_sno", nullable = false)
    private String creatorSno;  // 评论者的学号

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;  // 点赞数，默认值为 0

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();  // 评论时间

    // 无参构造函数
    public Comment() {}

    public Comment(int id, String discussionUuid, String content, String creatorSno, int likeCount, LocalDateTime createTime) {
        this.id = id;
        this.discussionUuid = discussionUuid;
        this.content = content;
        this.creatorSno = creatorSno;
        this.likeCount = likeCount;
        this.createTime = createTime;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatorSno() {
        return creatorSno;
    }

    public void setCreatorSno(String creatorSno) {
        this.creatorSno = creatorSno;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
