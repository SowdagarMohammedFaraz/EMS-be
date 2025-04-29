package com.cts.ems.controller;
 
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
 
import com.cts.ems.dto.UserRequest;
import com.cts.ems.entity.Goal;
import com.cts.ems.entity.PerformanceReview;
import com.cts.ems.enums.Role;
import com.cts.ems.service.GoalService;
import com.cts.ems.service.ReportService;
import com.cts.ems.service.ReviewService;
import com.cts.ems.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
 
class ManagerControllerTest {
 
    private MockMvc mockMvc;
 
    @Mock
    private UserService userService;
 
    @Mock
    private GoalService goalService;
 
    @Mock
    private ReviewService reviewService;
 
    @Mock
    private ReportService reportService;
 
    @InjectMocks
    private ManagerController managerController;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }
 
    @Test
    void testAddEmployee() throws Exception {
        UserRequest request = new UserRequest("testUser", "test@example.com", "password", "IT", "1234567890", Role.EMPLOYEE);
 
        mockMvc.perform(post("/manager/addEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee added successfully"));
 
        verify(userService, times(1)).register(request.getUsername(), request.getEmail(), request.getPassword(), request.getDept(), request.getMobileNo(), request.getRole());
    }
 
    @Test
    void testAddGoal() throws Exception {
        Goal goal = new Goal();
 
        mockMvc.perform(post("/manager/addGoal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(goal)))
                .andExpect(status().isOk())
                .andExpect(content().string("Goal assigned successfully"));
 
        verify(goalService, times(1)).addGoal(goal);
    }
 
    @Test
    void testGiveReview() throws Exception {
        PerformanceReview review = new PerformanceReview();
 
        mockMvc.perform(post("/manager/giveReview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(review)))
                .andExpect(status().isOk())
                .andExpect(content().string("Review submitted successfully"));
 
        verify(reviewService, times(1)).addReview(review);
    }
 
    @Test
    void testGetReportByID() throws Exception {
        Long employeeID = 1L;
 
        mockMvc.perform(get("/manager/getReportByID/{employeeID}", employeeID))
                .andExpect(status().isOk());
 
        verify(reportService, times(1)).getReportByID(employeeID);
    }
 
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}