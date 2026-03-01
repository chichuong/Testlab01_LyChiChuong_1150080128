package com.example.jobtitle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "job_title")
public class JobTitle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Job Title is required")
    @Size(max = 100, message = "Job Title must not exceed 100 characters")
    @Column(name = "job_title", nullable = false, length = 100)
    private String jobTitle;
    
    @Size(max = 400, message = "Description must not exceed 400 characters")
    @Column(name = "description", length = 400)
    private String description;
    
    @Column(name = "job_specification")
    private String jobSpecification; // File path
    
    @Column(name = "file_size")
    private Long fileSize; // in bytes
    
    @Size(max = 400, message = "Note must not exceed 400 characters")
    @Column(name = "note", length = 400)
    private String note;
    
    // Constructors
    public JobTitle() {
    }
    
    public JobTitle(String jobTitle, String description, String jobSpecification, Long fileSize, String note) {
        this.jobTitle = jobTitle;
        this.description = description;
        this.jobSpecification = jobSpecification;
        this.fileSize = fileSize;
        this.note = note;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getJobSpecification() {
        return jobSpecification;
    }
    
    public void setJobSpecification(String jobSpecification) {
        this.jobSpecification = jobSpecification;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
}
