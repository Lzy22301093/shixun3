package com.icplatform.service;

import com.icplatform.entity.SC;
import com.icplatform.repositories.SCRepositories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SCService {

    private SCRepositories scRepositories;

    public SCService(SCRepositories scRepositories) {
        this.scRepositories = scRepositories;
    }

    public List<SC> searchBySno(String sno) {
        return scRepositories.findBySno(sno);
    }

    public SC save(SC sc) {
        return scRepositories.save(sc);
    }

    public int countStudentsByCid(String cid){
        return scRepositories.countByCid(cid);
    }
}
