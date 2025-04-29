package com.cts.ems.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 
import com.cts.ems.entity.Feedback;
import com.cts.ems.repository.FeedbackRepository;
import com.cts.ems.service.FeedbackService;
 
public class FeedbackServiceTest {
 
    @Mock
    private FeedbackRepository feedbackRepository;
 
    @InjectMocks
    private FeedbackService feedbackService;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testGiveFeedback_Success() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFromemployeeID(1L);
        feedback.setEmployeeID(2L);
        feedback.setFeedbacktype("Positive");
        feedback.setComments("Great job!"); // Ensure comments are set
 
        // Act
        feedbackService.giveFeedback(feedback);
 
        // Assert
        verify(feedbackRepository, times(1)).save(feedback);
    }
 
    @Test
    public void testGiveFeedback_NullFromEmployeeID() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFromemployeeID(null);
        feedback.setEmployeeID(2L);
        feedback.setFeedbacktype("Positive");
        feedback.setComments("Great job!");
 
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.giveFeedback(feedback);
        });
        assertEquals("Employee IDs cannot be null", exception.getMessage());
    }
 
    @Test
    public void testGiveFeedback_NullEmployeeID() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFromemployeeID(1L);
        feedback.setEmployeeID(null);
        feedback.setFeedbacktype("Positive");
        feedback.setComments("Great job!");
 
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.giveFeedback(feedback);
        });
        assertEquals("Employee IDs cannot be null", exception.getMessage());
    }
 
    @Test
    public void testGiveFeedback_NullFeedbackType() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFromemployeeID(1L);
        feedback.setEmployeeID(2L);
        feedback.setFeedbacktype(null);
        feedback.setComments("Great job!");
 
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.giveFeedback(feedback);
        });
        assertEquals("Feedback type is required", exception.getMessage());
    }
 
    @Test
    public void testGiveFeedback_EmptyFeedbackType() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFromemployeeID(1L);
        feedback.setEmployeeID(2L);
        feedback.setFeedbacktype("");
        feedback.setComments("Great job!");
 
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.giveFeedback(feedback);
        });
        assertEquals("Feedback type is required", exception.getMessage());
    }
 
    @Test
    public void testGiveFeedback_NullComments() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFromemployeeID(1L);
        feedback.setEmployeeID(2L);
        feedback.setFeedbacktype("Positive");
        feedback.setComments(null);
 
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.giveFeedback(feedback);
        });
        assertEquals("Comments are required", exception.getMessage());
    }
 
    @Test
    public void testGiveFeedback_EmptyComments() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setFromemployeeID(1L);
        feedback.setEmployeeID(2L);
        feedback.setFeedbacktype("Positive");
        feedback.setComments("");
 
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            feedbackService.giveFeedback(feedback);
        });
        assertEquals("Comments are required", exception.getMessage());
    }
}
