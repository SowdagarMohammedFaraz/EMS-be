package com.cts.ems.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.ems.dto.MilestoneDTO;
import com.cts.ems.entity.Goal;
import com.cts.ems.entity.Milestone;
import com.cts.ems.entity.User;
import com.cts.ems.repository.GoalRepository;
import com.cts.ems.repository.MilestoneRepository;

@ExtendWith(MockitoExtension.class)
public class MilestoneServiceTest {

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private MilestoneService milestoneService;

    private MilestoneDTO milestoneDTO;
    private Milestone milestone;
    private Goal goal;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmployeeID(1L);

        goal = new Goal();
        goal.setGoalID(1L);
        
        

        milestone = new Milestone();
        milestone.setMilestoneID(1L);
        milestone.setUser(user);
        milestone.setGoal(goal);
        milestone.setProgressStatus("In Progress");

        milestoneDTO = new MilestoneDTO();
        milestoneDTO.setMilestoneID(1L);
        milestoneDTO.setEmployeeID(1L);
        milestoneDTO.setGoalID(1L);
        milestoneDTO.setProgressStatus("Completed");
    }

    @Test
    public void testUpdateMilestone_MilestoneNotFound() {
        when(milestoneRepository.findById(milestoneDTO.getMilestoneID())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            milestoneService.updateMilestone(milestoneDTO);
        });

        assertEquals("Milestone not found" + milestoneDTO.getMilestoneID(), exception.getMessage());
    }

    @Test
    public void testUpdateMilestone_InvalidMilestoneUpdateRequest() {
        milestoneDTO.setEmployeeID(2L);

        when(milestoneRepository.findById(milestoneDTO.getMilestoneID())).thenReturn(Optional.of(milestone));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            milestoneService.updateMilestone(milestoneDTO);
        });

        assertEquals("Invalid milestone update request", exception.getMessage());
    }

    @Test
    public void testUpdateMilestone_AllMilestonesCompleted() {
        when(milestoneRepository.findById(milestoneDTO.getMilestoneID())).thenReturn(Optional.of(milestone));
        when(milestoneRepository.findByGoal_GoalIDAndUser_EmployeeID(milestoneDTO.getGoalID(), milestoneDTO.getEmployeeID()))
                .thenReturn(Arrays.asList(milestone));

        milestoneService.updateMilestone(milestoneDTO);

        verify(milestoneRepository).save(milestone);
        verify(goalRepository).save(goal);
        assertEquals("Completed", goal.getProgressStatus());
    }

    @Test
    public void testGetAllMilestones() {
        when(milestoneRepository.findByGoal_GoalID(goal.getGoalID())).thenReturn(Arrays.asList(milestone));

        List<Milestone> milestones = milestoneService.getAllMilestones(goal.getGoalID());

        assertEquals(1, milestones.size());
        assertEquals(milestone.getMilestoneID(), milestones.get(0).getMilestoneID());
    }
}