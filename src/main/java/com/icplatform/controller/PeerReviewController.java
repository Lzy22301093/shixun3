package com.icplatform.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.icplatform.entity.PeerReview;
import com.icplatform.service.PeerReviewService;
import com.icplatform.service.HomeworkService;
import com.icplatform.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class PeerReviewController {

    @Autowired
    private PeerReviewService peerReviewService;

    @Autowired
    private HomeworkService homeworkService;

    // 提交评分
    @PostMapping("/submitReview")
    public Map<String, Object> submitReview(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> reviewData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清楚或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        String evaluateeId = reviewData.get("evaluateeId"); // 被评分学生的ID
        int assignmentId = Integer.parseInt(reviewData.get("assignmentId")); // 作业ID
        int score = Integer.parseInt(reviewData.get("score")); // 评分

        // 创建并保存评分记录
        PeerReview review = new PeerReview();
        review.setAssignmentId(assignmentId);
        review.setEvaluatorId(username); // 当前评分者ID
        review.setEvaluateeId(evaluateeId);
        review.setScore(score);
        peerReviewService.submitReview(review);

        response.put("status", "success");
        response.put("message", "评分提交成功");
        return response;
    }

    // 修改评分
    @PostMapping("/modifyReview")
    public Map<String, Object> modifyReview(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> reviewData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清楚或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int assignmentId = Integer.parseInt(reviewData.get("assignmentId"));
        String evaluateeId = reviewData.get("evaluateeId");
        int newScore = Integer.parseInt(reviewData.get("score"));

        // 查找并更新评分记录
        List<PeerReview> reviews = peerReviewService.getReviewsForStudent(assignmentId, evaluateeId);
        for (PeerReview review : reviews) {
            if (review.getEvaluatorId().equals(username)) {
                review.setScore(newScore);
                peerReviewService.modifyReview(review);
                response.put("status", "success");
                response.put("message", "评分修改成功");
                return response;
            }
        }

        response.put("status", "error");
        response.put("message", "未找到对应的评分记录");
        return response;
    }

    // 展示作业评分
    @PostMapping("/viewPeerReviews")
    public Map<String, Object> viewPeerReviews(@RequestHeader Map<String, String> header, @RequestBody Map<String, String> reviewData) {
        Map<String, Object> response = new HashMap<>();
        String token = header.get("token");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWTUtil.verifyToken(token);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "token已被清楚或已过期");
            return response;
        }

        String username = decodedJWT.getClaim("username").asString();
        int assignmentId = Integer.parseInt(reviewData.get("assignmentId"));
        String evaluateeId = reviewData.get("evaluateeId");

        // 获取互评记录
        List<PeerReview> reviews = peerReviewService.getReviewsForStudent(assignmentId, evaluateeId);

        response.put("reviews", reviews);
        response.put("status", "success");
        return response;
    }
}
