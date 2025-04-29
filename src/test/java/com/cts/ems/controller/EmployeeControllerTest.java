package com.cts.ems.controller;
 
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
import java.util.Arrays;
import java.util.List;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 
import com.cts.ems.dto.MilestoneDTO;
import com.cts.ems.dto.PasswordDTO;
import com.cts.ems.entity.Feedback;
import com.cts.ems.entity.Goal;
import com.cts.ems.service.FeedbackService;
import com.cts.ems.service.GoalService;
import com.cts.ems.service.MilestoneService;
import com.cts.ems.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
 
class EmployeeControllerTest {
 
    private MockMvc mockMvc;
 
    @Mock
    private UserService userService;
 
    @Mock
    private GoalService goalService;
 
    @Mock
    private FeedbackService feedbackService;
 
    @Mock
    private MilestoneService milestoneService;
 
    @InjectMocks
    private EmployeeController employeeController;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }
 
    @Test
    void testUpdatePassword() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO("testUser", "oldPassword", "newPassword");
 
        mockMvc.perform(post("/employee/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(passwordDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
 
        verify(userService, times(1)).updatePassword(passwordDTO.getUserName(), passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
    }
 
    @Test
    void testGetAllGoal() throws Exception {
        Long employeeID = 1L;
        List<Goal> goals = Arrays.asList(new Goal(), new Goal());
 
        when(goalService.getAllGoal(employeeID)).thenReturn(goals);
 
        mockMvc.perform(get("/employee/getAllGoal/{employeeID}", employeeID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(goals.size()));
 
        verify(goalService, times(1)).getAllGoal(employeeID);
    }
 
    @Test
    void testGiveFeedback() throws Exception {
        Feedback feedback = new Feedback();
 
        mockMvc.perform(post("/employee/giveFeedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(feedback)))
                .andExpect(status().isOk());
 
        verify(feedbackService, times(1)).giveFeedback(feedback);
    }
 
    @Test
    void testUpdateMilestone() throws Exception {
        MilestoneDTO milestoneDTO = new MilestoneDTO();
 
        mockMvc.perform(post("/employee/updateMilestone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(milestoneDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Milestone updated successfully"));
 
        verify(milestoneService, times(1)).updateMilestone(any(MilestoneDTO.class));
    }
 
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
