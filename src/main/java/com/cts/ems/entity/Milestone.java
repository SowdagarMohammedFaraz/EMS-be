package com.cts.ems.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long milestoneID;
    
    private String milestoneDesc;
    
    private LocalDate targetDate;
    
    private String progressStatus;
    
    @ManyToOne //many milestones are mapped to one goal
    @JoinColumn(name="goalID",nullable = false)
    @JsonBackReference
    private Goal goal;
    
    @ManyToOne
    @JoinColumn(name="employeeID",nullable = false)
    @JsonIgnore
    private User user;
	
}
