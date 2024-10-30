package com.icplatform.repositories;


import com.icplatform.entity.Homework;
import com.icplatform.entity.Teaching;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachingRepositories extends JpaRepository<Teaching, String> {
}
