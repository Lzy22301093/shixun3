package com.icplatform.service;

import com.icplatform.entity.Homework;
import com.icplatform.repositories.HomeworkRepositories;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HomeworkService {
    private HomeworkRepositories homeworkRepositories;

    public HomeworkService(HomeworkRepositories homeworkRepositories) {
        this.homeworkRepositories = homeworkRepositories;
    }

    public List<Homework> searchByCidAndSno(String cid, String sno) {
        return homeworkRepositories.findByCidAndSno(cid, sno);
    }

    public int countHomeworkByCid(String cid) {
        return homeworkRepositories.countByCid(cid);
    }

    // 根据 cid 和 workid 统计已提交的作业数量
    public int countSubmittedByCidAndWorkId(String cid, int workid) {
        return homeworkRepositories.countSubmittedByCidAndWorkId(cid, workid);
    }

    public List<Object[]> countHomeworkByWorkId(String cid) {
        return homeworkRepositories.countHomeworkByWorkId(cid);
    }

    public Homework findByCidSnoAndWorkid(String cid, String sno, int workid) {
        return homeworkRepositories.findByCidAndSnoAndWorkid(cid, sno, workid);
    }

    public List<Homework> findByCidAndWorkid(String cid, int workid) {
        return homeworkRepositories.findByCidAndWorkid(cid, workid);
    }

    public Resource getHomeworkFile(String cid, int workid, String sno){
        Homework homework = homeworkRepositories.findByCidAndSnoAndWorkid(cid,sno,workid);

        if (homework != null) {
            try {
                Path filePath = Paths.get(homework.getPath());
                return new UrlResource(filePath.toUri());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Homework save(Homework homework) {
        return homeworkRepositories.save(homework);
    }

    //更新作业
    public void updateHomeworkByCidAndSnoAndWorkid(String hname, String path, String cid, String sno, int workid, String cno, LocalDateTime submit_time, String reviestatus) {
        Homework homework = homeworkRepositories.findByCidAndSnoAndWorkid(cid,sno,workid);
        if (homework == null) {
            throw new IllegalArgumentException("作业不存在");
        }

        homework.setHname(hname);
        homework.setPath(path);
        homework.setCno(cno);
        homework.setStime(submit_time);
        homework.setReviestatus(reviestatus);
        homeworkRepositories.save(homework);

    }

    //插入作业
    public void insertNewHomework(String hname, String path, String cid, String sno, int workid, String cno, LocalDateTime submit_time,LocalDateTime start, LocalDateTime end, String reviestatus) {
        Homework homework = new Homework();
        homework.setHname(hname);
        homework.setCid(cid);
        homework.setSno(sno);
        homework.setPath(path);
        homework.setWorkid(workid);
        homework.setCno(cno);
        homework.setStime(submit_time);
        homework.setStart(start);
        homework.setEnd(end);
        homework.setReviestatus(reviestatus);
        homeworkRepositories.save(homework);
    }

    public String searchPathByHname(String hname) {
        return homeworkRepositories.findByHname(hname).getPath();
    }

    public String deleteHomeworkByFname(String hname) {

        Homework homework = homeworkRepositories.findByHname(hname);
        if(homework != null) {
            homeworkRepositories.delete(homework);
            String result = "数据库记录删除成功";
            return result;
        }else{
            String result = "数据库记录不存在";
            return result;
        }
    }
}
