package com.example.jobtitle.service;

import com.example.jobtitle.entity.JobTitle;
import com.example.jobtitle.repository.JobTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class JobTitleService {
    
    @Autowired
    private JobTitleRepository repository;
    
    @Value("${upload.directory}")
    private String uploadDirectory;
    
    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1024 KB
    
    public List<JobTitle> getAllJobTitles() {
        return repository.findAll();
    }
    
    public Optional<JobTitle> getJobTitleById(Long id) {
        return repository.findById(id);
    }
    
    public JobTitle saveJobTitle(JobTitle jobTitle, MultipartFile file) throws IOException {
        // Validate job title
        if (jobTitle.getJobTitle() == null || jobTitle.getJobTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Job Title is required");
        }
        
        if (jobTitle.getJobTitle().length() > 100) {
            throw new IllegalArgumentException("Job Title must not exceed 100 characters");
        }
        
        // Validate description
        if (jobTitle.getDescription() != null && jobTitle.getDescription().length() > 400) {
            throw new IllegalArgumentException("Description must not exceed 400 characters");
        }
        
        // Validate note
        if (jobTitle.getNote() != null && jobTitle.getNote().length() > 400) {
            throw new IllegalArgumentException("Note must not exceed 400 characters");
        }
        
        // Handle file upload
        if (file != null) {
            // Validate file size
            if (file.isEmpty() || file.getSize() == 0) {
                throw new IllegalArgumentException("File cannot be empty");
            }
            
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size must not exceed 1024 KB");
            }
            
            // Save file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDirectory);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            jobTitle.setJobSpecification(fileName);
            jobTitle.setFileSize(file.getSize());
        }
        
        return repository.save(jobTitle);
    }
    
    public void deleteJobTitle(Long id) {
        repository.deleteById(id);
    }
}
