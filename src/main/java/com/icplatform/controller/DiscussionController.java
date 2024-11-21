package com.icplatform.controller;

import com.icplatform.entity.Comment;
import com.icplatform.entity.Discussion;
import com.icplatform.service.CommentService;
import com.icplatform.service.DiscussionService;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discussion")
@CrossOrigin(origins = "*")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private CommentService commentService;

    // 发布讨论
    @PostMapping("/createDiscussion")
    public Map<String, Object> createDiscussion(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> discussionData)     {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String title = discussionData.get("title");
        String content = discussionData.get("content");
        String creatorSno = discussionData.get("creatorSno");

        Discussion discussion = new Discussion();
        discussion.setTitle(title);
        discussion.setContent(content);
        discussion.setCreatorSno(creatorSno);
        discussion.setLikeCount(0);
        discussion.setCommentCount(0);
        discussion.setCollectCount(0);

        discussionService.createDiscussion(discussion);
        response.put("status", "success");
        response.put("message", "讨论发布成功");
        return response;
    }

    // 展示所有评论
    @GetMapping("/getAllComments")
    public Map<String, Object> getAllComments(@RequestHeader Map<String, String> header, @RequestParam String discussionUuid) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        System.out.println(discussionUuid);
        List<Comment> comments = commentService.getCommentsByDiscussionUuid(discussionUuid);
        for (Comment comment : comments) {
            System.out.println(comment.getContent());
        }
        response.put("status", "success");
        response.put("comments", comments);
        return response;
    }

    // 创建评论
    @PostMapping("/createComment")
    public Map<String, Object> createComment(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> commentData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String discussionUuid = commentData.get("discussionUuid");
        String creatorSno = commentData.get("creatorSno");
        String content = commentData.get("content");

        Comment comment = new Comment();
        comment.setDiscussionUuid(discussionUuid);
        comment.setCreatorSno(creatorSno);
        comment.setContent(content);
        comment.setCreateTime(LocalDateTime.now());

        commentService.createComment(comment);
        response.put("status", "success");
        response.put("message", "评论创建成功");
        return response;
    }

    // 删除评论
    @PostMapping("/deleteComment")
    public Map<String, Object> deleteComment(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> deleteData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        int commentId = Integer.parseInt(deleteData.get("commentId"));
        commentService.deleteCommentById(commentId);
        response.put("status", "success");
        response.put("message", "评论删除成功");
        return response;
    }

    // 获取所有讨论
    @GetMapping("/getAllDiscussions")
    public Map<String, Object> getAllDiscussions(@RequestHeader Map<String, String> header) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        List<Discussion> discussions = discussionService.getAllDiscussions();
        response.put("status", "success");
        response.put("discussions", discussions);
        return response;
    }

    // 点赞讨论
    @PostMapping("/likeDiscussion")
    public Map<String, Object> likeDiscussion(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> likeData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String Sno = likeData.get("Sno");
        String discussionUuid = likeData.get("discussionUuid");
        Discussion discussion = discussionService.getDiscussionBySnoAndUuid(Sno,discussionUuid);
        if (discussion != null) {
            discussion.setLikeCount(discussion.getLikeCount() + 1);
            discussionService.createDiscussion(discussion);
            response.put("status", "success");
            response.put("message", "点赞成功");
        } else {
            response.put("status", "error");
            response.put("message", "讨论不存在");
        }

        return response;
    }

    // 评论讨论
    @PostMapping("/commentDiscussion")
    public Map<String, Object> commentDiscussion(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> commentData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String discussionUuid = commentData.get("discussionUuid");
        String creatorSno = commentData.get("creatorSno");
        String comment = commentData.get("comment");

        Discussion discussion = discussionService.getDiscussionBySnoAndUuid(creatorSno,discussionUuid);
        if (discussion != null) {
            discussion.setCommentCount(discussion.getCommentCount() + 1);
            // 这里可以进一步处理评论内容，可以将评论保存到数据库
            discussionService.createDiscussion(discussion);
            response.put("status", "success");
            response.put("message", "评论成功");
        } else {
            response.put("status", "error");
            response.put("message", "讨论不存在");
        }

        return response;
    }

    // 收藏讨论
    @PostMapping("/collectDiscussion")
    public Map<String, Object> collectDiscussion(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> collectData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String discussionUuid = collectData.get("discussionUuid");
        String Sno = collectData.get("Sno");
        Discussion discussion = discussionService.getDiscussionBySnoAndUuid(Sno,discussionUuid);
        if (discussion != null) {
            discussion.setCollectCount(discussion.getCollectCount() + 1);
            discussionService.createDiscussion(discussion);
            response.put("status", "success");
            response.put("message", "收藏成功");
        } else {
            response.put("status", "error");
            response.put("message", "讨论不存在");
        }

        return response;
    }

    // 删除讨论
    @PostMapping("/deleteDiscussion")
    public Map<String, Object> deleteDiscussion(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> deleteData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String discussionUuid = deleteData.get("discussionUuid");
        String Sno = deleteData.get("Sno");
        Discussion discussion = discussionService.getDiscussionBySnoAndUuid(Sno,discussionUuid);
        if (discussion != null) {
            discussionService.deleteDiscussion(discussion);
            response.put("status", "success");
            response.put("message", "讨论删除成功");
        } else {
            response.put("status", "error");
            response.put("message", "讨论不存在");
        }

        return response;
    }

    // 根据UUID寻找讨论
    @PostMapping("/findDiscussionByUuid")
    public Map<String, Object> findDiscussionByUuid(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> findData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清除或已过期");
            return response;
        }

        String discussionUuid = findData.get("discussionUuid");
        String Sno = findData.get("Sno");
        Discussion discussion = discussionService.getDiscussionBySnoAndUuid(Sno,discussionUuid);
        if (discussion != null) {
            response.put("status", "success");
            response.put("discussion", discussion);
        } else {
            response.put("status", "error");
            response.put("message", "讨论不存在");
        }

        return response;
    }
}
