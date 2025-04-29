package com.cts.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.ems.entity.PerformanceReview;

@Repository
public interface ReviewRepository extends JpaRepository<PerformanceReview, Long>{

	PerformanceReview findByEmployeeID(Long employeeID);
}
