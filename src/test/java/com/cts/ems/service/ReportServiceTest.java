package com.cts.ems.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.ems.entity.Feedback;
import com.cts.ems.entity.PerformanceReview;
import com.cts.ems.entity.Report;
import com.cts.ems.exception.ReportNotFoundException;
import com.cts.ems.repository.FeedbackRepository;
import com.cts.ems.repository.ReportRepository;
import com.cts.ems.repository.ReviewRepository;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private ReportService reportService;

    private PerformanceReview review;
    private Feedback feedback;
    private Report report;

    @BeforeEach
    public void setUp() {
        review = new PerformanceReview();
        review.setEmployeeID(1L);
        review.setPerformanceScore("Excellent");
        review.setFeedback("Great job!");

        feedback = new Feedback();
        feedback.setEmployeeID(1L);
        feedback.setComments("Keep up the good work!");

        report = new Report();
        report.setEmployeeID(1L);
        report.setPerformancesummary("Excellent");
        report.setFeedbacksummary("Keep up the good work!,Great job!");
        report.setDate(LocalDate.now());
    }

    @Test
    public void testGetReportByID_ReportNotFound() {
        when(reviewRepository.findByEmployeeID(1L)).thenReturn(null);
        when(feedbackRepository.findByEmployeeID(1L)).thenReturn(null);

        ReportNotFoundException exception = assertThrows(ReportNotFoundException.class, () -> {
            reportService.getReportByID(1L);
        });

        assertEquals("Report not found for employee ID: 1", exception.getMessage());
    }

    @Test
    public void testGetReportByID_Success() {
        when(reviewRepository.findByEmployeeID(1L)).thenReturn(review);
        when(feedbackRepository.findByEmployeeID(1L)).thenReturn(feedback);
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        Report generatedReport = reportService.getReportByID(1L);

        assertNotNull(generatedReport);
        assertEquals(1L, generatedReport.getEmployeeID());
        assertEquals("Excellent", generatedReport.getPerformancesummary());
        assertEquals("Keep up the good work!,Great job!", generatedReport.getFeedbacksummary());
        assertEquals(LocalDate.now(), generatedReport.getDate());
    }
}