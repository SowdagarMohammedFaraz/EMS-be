package com.cts.ems.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.ems.entity.Goal;
import com.cts.ems.entity.Milestone;
import com.cts.ems.entity.User;
import com.cts.ems.enums.Role;
import com.cts.ems.exception.InvalidEmployeeRoleException;
import com.cts.ems.repository.GoalRepository;
import com.cts.ems.repository.UserRepository;

public class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoalService goalService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddGoalWithValidEmployeeRole() {
        User user = new User();
        user.setEmployeeID(1L);
        user.setRoles(Collections.singleton(Role.EMPLOYEE));

        Goal goal = new Goal();
        goal.setEmployeeID(1L);
        Milestone milestone = new Milestone();
        goal.setMilestones(Collections.singletonList(milestone));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        goalService.addGoal(goal);

        verify(goalRepository, times(1)).save(goal);
    }

    @Test
    public void testAddGoalWithInvalidEmployeeRole() {
        User user = new User();
        user.setEmployeeID(1L);
        user.setRoles(Collections.singleton(Role.MANAGER));

        Goal goal = new Goal();
        goal.setEmployeeID(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(InvalidEmployeeRoleException.class, () -> goalService.addGoal(goal));
    }

    @Test
    public void testAddGoalWithNonExistentUser() {
        Goal goal = new Goal();
        goal.setEmployeeID(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> goalService.addGoal(goal));
    }

    @Test
    public void testGetAllGoal() {
        Long employeeID = 1L;
        goalService.getAllGoal(employeeID);
        verify(goalRepository, times(1)).findByEmployeeID(employeeID);
    }
}