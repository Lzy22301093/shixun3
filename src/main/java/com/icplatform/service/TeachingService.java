package com.icplatform.service;

import com.icplatform.entity.Teaching;
import com.icplatform.repositories.TeachingRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachingService {

    private TeachingRepositories teachingRepositories;

    public TeachingService(TeachingRepositories teachingRepositories) {
        this.teachingRepositories = teachingRepositories;
    }

    public Teaching searchByCid(String cid){
        return teachingRepositories.findById(cid).orElse(null);
    }

    public List<String> searchCidByTno(String tno){
        return teachingRepositories.findCidByTno(tno);
    }
}
