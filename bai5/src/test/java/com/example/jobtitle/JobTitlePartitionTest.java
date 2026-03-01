package com.example.jobtitle;

import com.example.jobtitle.entity.JobTitle;
import com.example.jobtitle.repository.JobTitleRepository;
import com.example.jobtitle.service.JobTitleService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Test Cases cho màn hình Add Job Title
 * Sử dụng Partition Testing theo bảng phân vùng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobTitlePartitionTest {
    
    @Autowired
    private JobTitleService service;
    
    @Autowired
    private JobTitleRepository repository;
    
    @After
    public void cleanup() {
        // Clean up test data after each test
        repository.deleteAll();
    }
    
    // ============ PARTITION 1-3: JOB TITLE TESTING ============
    
    /**
     * TC1: Job Title - Chuỗi rỗng (length = 0)
     * Lower Boundary: length(input) >= 0
     * Upper Boundary: length(input) <= 0
     * Kết quả: FAIL - Job Title is required
     */
    @Test
    public void testPartition1_JobTitle_Empty() {
        JobTitle job = new JobTitle();
        job.setJobTitle("");
        job.setDescription("Test Description");
        
        try {
            service.saveJobTitle(job, null);
            fail("Should throw exception for empty job title");
        } catch (Exception e) {
            assertEquals("Job Title is required", e.getMessage());
        }
    }
    
    /**
     * TC2: Job Title - Chuỗi hợp lệ (0 < length <= 100)
     * Lower Boundary: length(input) > 0
     * Upper Boundary: length(input) <= 100
     * Kết quả: PASS - Lưu thành công
     */
    @Test
    public void testPartition2_JobTitle_Valid() throws Exception {
        JobTitle job = new JobTitle();
        job.setJobTitle("Software Engineer");
        job.setDescription("Test Description");
        
        JobTitle saved = service.saveJobTitle(job, null);
        
        assertNotNull(saved.getId());
        assertEquals("Software Engineer", saved.getJobTitle());
    }
    
    /**
     * TC3: Job Title - Chuỗi quá dài (length > 100)
     * Lower Boundary: length(input) > 100
     * Upper Boundary: length(input) <= length(longest string)
     * Kết quả: FAIL - Job Title must not exceed 100 characters
     */
    @Test
    public void testPartition3_JobTitle_TooLong() {
        JobTitle job = new JobTitle();
        // Create a string with 101 characters
        String longTitle = "A".repeat(101);
        job.setJobTitle(longTitle);
        job.setDescription("Test Description");
        
        try {
            service.saveJobTitle(job, null);
            fail("Should throw exception for title > 100 chars");
        } catch (Exception e) {
            assertEquals("Job Title must not exceed 100 characters", e.getMessage());
        }
    }
    
    // ============ PARTITION 4-6: DESCRIPTION TESTING ============
    
    /**
     * TC4: Description - Chuỗi rỗng (length = 0)
     * Lower Boundary: length(input) >= 0
     * Upper Boundary: length(input) <= 0
     * Kết quả: PASS - Description is optional
     */
    @Test
    public void testPartition4_Description_Empty() throws Exception {
        JobTitle job = new JobTitle();
        job.setJobTitle("Manager");
        job.setDescription("");
        
        JobTitle saved = service.saveJobTitle(job, null);
        
        assertNotNull(saved.getId());
        assertEquals("", saved.getDescription());
    }
    
    /**
     * TC5: Description - Chuỗi hợp lệ (0 < length <= 400)
     * Lower Boundary: length(input) > 0
     * Upper Boundary: length(input) <= 400
     * Kết quả: PASS - Lưu thành công
     */
    @Test
    public void testPartition5_Description_Valid() throws Exception {
        JobTitle job = new JobTitle();
        job.setJobTitle("Developer");
        job.setDescription("This is a valid description with reasonable length");
        
        JobTitle saved = service.saveJobTitle(job, null);
        
        assertNotNull(saved.getId());
        assertEquals("This is a valid description with reasonable length", saved.getDescription());
    }
    
    /**
     * TC6: Description - Chuỗi quá dài (length > 400)
     * Lower Boundary: length(input) > 400
     * Upper Boundary: length(input) <= length(longest string)
     * Kết quả: FAIL - Description must not exceed 400 characters
     */
    @Test
    public void testPartition6_Description_TooLong() {
        JobTitle job = new JobTitle();
        job.setJobTitle("Analyst");
        // Create a string with 401 characters
        String longDesc = "A".repeat(401);
        job.setDescription(longDesc);
        
        try {
            service.saveJobTitle(job, null);
            fail("Should throw exception for description > 400 chars");
        } catch (Exception e) {
            assertEquals("Description must not exceed 400 characters", e.getMessage());
        }
    }
    
    // ============ PARTITION 7-9: JOB SPECIFICATION (FILE) TESTING ============
    
    /**
     * TC7: Job Specification - File rỗng (size = 0 KB)
     * Lower Boundary: File size >= 0 kB
     * Upper Boundary: File size <= 0 kB
     * Kết quả: FAIL - File cannot be empty
     */
    @Test
    public void testPartition7_FileSpec_EmptyFile() {
        JobTitle job = new JobTitle();
        job.setJobTitle("Tester");
        
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", 
            "empty.txt", 
            "text/plain", 
            new byte[0]
        );
        
        try {
            service.saveJobTitle(job, emptyFile);
            fail("Should throw exception for empty file");
        } catch (Exception e) {
            assertEquals("File cannot be empty", e.getMessage());
        }
    }
    
    /**
     * TC8: Job Specification - File hợp lệ (0 < size <= 1024 KB)
     * Lower Boundary: File size = 0 kB
     * Upper Boundary: File size <= 1024 kB
     * Kết quả: PASS - File uploaded successfully
     */
    @Test
    public void testPartition8_FileSpec_ValidFile() throws Exception {
        JobTitle job = new JobTitle();
        job.setJobTitle("Designer");
        
        // Create a valid file (100 KB)
        byte[] content = new byte[100 * 1024];
        MockMultipartFile validFile = new MockMultipartFile(
            "file",
            "spec.pdf",
            "application/pdf",
            content
        );
        
        JobTitle saved = service.saveJobTitle(job, validFile);
        
        assertNotNull(saved.getId());
        assertNotNull(saved.getJobSpecification());
        assertEquals(100 * 1024L, saved.getFileSize().longValue());
    }
    
    /**
     * TC9: Job Specification - File quá lớn (size > 1024 KB)
     * Lower Boundary: File size > 1024 kB
     * Upper Boundary: File size <= Largest File
     * Kết quả: FAIL - File size must not exceed 1024 KB
     */
    @Test
    public void testPartition9_FileSpec_FileTooLarge() {
        JobTitle job = new JobTitle();
        job.setJobTitle("Coordinator");
        
        // Create a file larger than 1024 KB (1025 KB)
        byte[] content = new byte[1025 * 1024];
        MockMultipartFile largeFile = new MockMultipartFile(
            "file",
            "large.pdf",
            "application/pdf",
            content
        );
        
        try {
            service.saveJobTitle(job, largeFile);
            fail("Should throw exception for file > 1024 KB");
        } catch (Exception e) {
            assertEquals("File size must not exceed 1024 KB", e.getMessage());
        }
    }
    
    // ============ PARTITION 10-12: NOTE TESTING ============
    
    /**
     * TC10: Note - Chuỗi rỗng (length = 0)
     * Lower Boundary: length(input) >= 0
     * Upper Boundary: length(input) <= 0
     * Kết quả: PASS - Note is optional
     */
    @Test
    public void testPartition10_Note_Empty() throws Exception {
        JobTitle job = new JobTitle();
        job.setJobTitle("Supervisor");
        job.setNote("");
        
        JobTitle saved = service.saveJobTitle(job, null);
        
        assertNotNull(saved.getId());
        assertEquals("", saved.getNote());
    }
    
    /**
     * TC11: Note - Chuỗi hợp lệ (0 < length <= 400)
     * Lower Boundary: length(input) > 0
     * Upper Boundary: length(input) <= 400
     * Kết quả: PASS - Lưu thành công
     */
    @Test
    public void testPartition11_Note_Valid() throws Exception {
        JobTitle job = new JobTitle();
        job.setJobTitle("Assistant");
        job.setNote("This is a valid note");
        
        JobTitle saved = service.saveJobTitle(job, null);
        
        assertNotNull(saved.getId());
        assertEquals("This is a valid note", saved.getNote());
    }
    
    /**
     * TC12: Note - Chuỗi quá dài (length > 400)
     * Lower Boundary: length(input) > 400
     * Upper Boundary: length(input) <= length(longest string)
     * Kết quả: FAIL - Note must not exceed 400 characters
     */
    @Test
    public void testPartition12_Note_TooLong() {
        JobTitle job = new JobTitle();
        job.setJobTitle("Director");
        // Create a string with 401 characters
        String longNote = "A".repeat(401);
        job.setNote(longNote);
        
        try {
            service.saveJobTitle(job, null);
            fail("Should throw exception for note > 400 chars");
        } catch (Exception e) {
            assertEquals("Note must not exceed 400 characters", e.getMessage());
        }
    }
}
