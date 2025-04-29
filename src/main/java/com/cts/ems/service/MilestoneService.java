package com.cts.ems.service;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.ems.controller.AuthController;
import com.cts.ems.dto.MilestoneDTO;
import com.cts.ems.entity.Goal;
import com.cts.ems.entity.Milestone;
import com.cts.ems.repository.GoalRepository;
import com.cts.ems.repository.MilestoneRepository;


@Service
public class MilestoneService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private MilestoneRepository milestoneRepository;
	
	@Autowired
	private GoalRepository goalRepository;
	
	/** update a milestone and check if all the milestones for the goal are completed **/
	 
	
	public void updateMilestone(MilestoneDTO milestoneDTO) {

		logger.info("Updating milestone ID:{} for employee ID:{}", milestoneDTO.getMilestoneID(), milestoneDTO.getEmployeeID());

        //Fetch the milestone to be updated

        Milestone milestone = milestoneRepository.findById(milestoneDTO.getMilestoneID())
                .orElseThrow(() -> new RuntimeException("Milestone not found with ID: " + milestoneDTO.getMilestoneID()));

        if (!milestone.getUser().getEmployeeID().equals(milestoneDTO.getEmployeeID()) ||
                !milestone.getGoal().getGoalID().equals(milestoneDTO.getGoalID())) {
            throw new RuntimeException("Invalid milestone update request for Milestone ID: " + milestoneDTO.getMilestoneID());
        }

        //Update milestone progress status

        milestone.setProgressStatus(milestoneDTO.getProgressStatus());
        milestoneRepository.save(milestone);

        //check if all the milestones for the given goal and employee are completed

        boolean allCompleted = milestoneRepository
                .findByGoal_GoalIDAndUser_EmployeeID(milestoneDTO.getGoalID(), milestoneDTO.getEmployeeID())
                .stream()
                .allMatch(m -> m.getProgressStatus().equalsIgnoreCase("Completed"));

        if (allCompleted) {
            Goal goal = milestone.getGoal();
            goal.setProgressStatus("Completed");
            goalRepository.save(goal);
            logger.info("All milestones completed for Goal ID: {}, marked as completed", milestoneDTO.getGoalID());
        }
	
	}
	
	public List<Milestone> getAllMilestones(Long goalID) {
        return milestoneRepository.findByGoal_GoalID(goalID);
    }
}
