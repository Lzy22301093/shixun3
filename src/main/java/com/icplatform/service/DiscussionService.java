package com.icplatform.service;

import com.icplatform.entity.Discussion;
import com.icplatform.repositories.DiscussionRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionRepositories discussionRepositories;

    // 创建新的讨论
    public Discussion createDiscussion(Discussion discussion) {
        return discussionRepositories.save(discussion);  // 保存新的讨论
    }

    // 查找讨论通过UUID
    public Discussion getDiscussionBySnoAndUuid(String Sno,String discussionUuid) {
        return discussionRepositories.findBySnoAndDiscussionUuid(Sno,discussionUuid);
    }

    // 查找某个用户发起的所有讨论
    public List<Discussion> getDiscussionsByCreatorSno(String creatorSno) {
        return discussionRepositories.findByCreatorSno(creatorSno);
    }

    // 根据标题关键词查找讨论
    public List<Discussion> searchDiscussionsByTitle(String title) {
        return discussionRepositories.findByTitleContaining(title);
    }

    // 展示全部讨论
    public List<Discussion> getAllDiscussions() {
        return discussionRepositories.findAll();
    }

    // 删除讨论
    public void deleteDiscussion(Discussion discussion) {
        discussionRepositories.delete(discussion);
    }
}
