package com.icplatform.service;

import com.icplatform.entity.Homework;
import com.icplatform.repositories.AssignedHomeworkRepositories;
import com.icplatform.repositories.HomeworkRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.icplatform.entity.AssignedHomework;

import java.util.List;
import java.util.Random;

@Service
public class AssignedHomeworkService {

    @Autowired
    private AssignedHomeworkRepositories assignedHomeworkRepositories;

    @Autowired
    private HomeworkRepositories homeworkRepositories;

    public double calculateFinalScore(String reviewerSno, String revieweeSno, String cid, int workid) {
        List<AssignedHomework> the_AssignedHomework = assignedHomeworkRepositories.findByReviewerSnoAndCidAndWorkid(reviewerSno, cid, workid);
        if(homeworkRepositories.findByCidAndSnoAndWorkid(cid,revieweeSno,workid).getTeacherscore() != null){
            int teacherScore = homeworkRepositories.findByCidAndSnoAndWorkid(cid,revieweeSno,workid).getTeacherscore();
            if (the_AssignedHomework.size() < 2) {
                return teacherScore; // 不足两条评分直接返回老师评分
            }

            // 去掉最高和最低分
            int totalScore = 0;
            int maxScore = Integer.MIN_VALUE;
            int minScore = Integer.MAX_VALUE;

            for (AssignedHomework assignedHomework : the_AssignedHomework) {
                totalScore += assignedHomework.getStudentscore();
                if (assignedHomework.getStudentscore() > maxScore) {
                    maxScore = assignedHomework.getStudentscore();
                }
                if (assignedHomework.getStudentscore() < minScore) {
                    minScore = assignedHomework.getStudentscore();
                }
            }

            // 计算平均分
            double averageScore = (totalScore - maxScore - minScore) / (the_AssignedHomework.size() - 2);

            // 加权计算
            return (averageScore * 0.3) + (teacherScore * 0.7);
        }
        else{
            return 0;
        }
    }

    public void assignRandomHomeworks(String reviewerSno, String cid, int workid) {
        // 1. 查询所有相关的作业
        List<Homework> allHomeworks = homeworkRepositories.findByCidAndWorkid(cid, workid);
        // 2. 随机选择作业并分配给 reviewerSno
        Random random = new Random();
        int assignCount = Math.min(allHomeworks.size(), 3);  // 分配最多3个作业
        if (assignedHomeworkRepositories.countSubmittedBySnoAndCidAndWorkId(reviewerSno,cid,workid) >= assignCount) return;
        for (int i = 0; i < assignCount; i++) {
            int randomIndex = random.nextInt(allHomeworks.size());
            Homework homework = allHomeworks.get(randomIndex);
            // 3. 创建并保存 AssignedHomework 实例
            AssignedHomework assignedHomework = new AssignedHomework();
            assignedHomework.setReviewerSno(reviewerSno);
            assignedHomework.setRevieweeSno(homework.getSno());  // 获取作业的学生学号
            assignedHomework.setCid(cid);
            assignedHomework.setStudentscore(0);
            assignedHomework.setWorkid(homework.getWorkid());
            AssignedHomework temp = assignedHomeworkRepositories.findByTwoSnoAndCidAndWorkid(reviewerSno, homework.getSno(), cid, homework.getWorkid());

            if (temp == null) {
                assignedHomeworkRepositories.save(assignedHomework);
            }
            // 4. 避免重复分配
            allHomeworks.remove(randomIndex);
        }
    }

    public List<AssignedHomework> findAssignedHomeworks(String reviewerSno,String cid,int workid) {
        return assignedHomeworkRepositories.findByReviewerSnoAndCidAndWorkid(reviewerSno,cid,workid);
    }
}


