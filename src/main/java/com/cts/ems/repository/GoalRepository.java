package com.cts.ems.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.ems.entity.Goal;
import com.cts.ems.entity.User;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>{ //long is data type of primary key
	//Goal is the entity class and Long is the data type of primary key
	
	
	
	
	List<Goal> findByEmployeeID(Long employeeID);
	
	List<Goal> findByAssignedByManager(Long managerId);
	
}
