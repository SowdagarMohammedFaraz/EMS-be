package com.cts.ems.service;
 
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 
import com.cts.ems.entity.PerformanceReview;
import com.cts.ems.repository.ReviewRepository;
 
public class ReviewServiceTest {
 
    @Mock
    private ReviewRepository reviewRepository;
 
    @InjectMocks
    private ReviewService reviewService;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    public void testAddReview_Success() {
        // Arrange
        PerformanceReview review = new PerformanceReview();
        review.setEmployeeID(1L);
               review.setFeedback("Great performance");
 
        // Act
        reviewService.addReview(review);
 
        // Assert
        verify(reviewRepository, times(1)).save(review);
    }
}