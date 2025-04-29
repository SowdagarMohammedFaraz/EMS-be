package com.cts.ems.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.ems.entity.Milestone;
import com.cts.ems.entity.User;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long>{

	List<Milestone> findByGoal_GoalIDAndUser_EmployeeID(Long goalID,Long employeeID);
	
	List<Milestone> findByGoal_GoalID(Long goalId);
	
	void deleteByUser(User user);
}
