package com.example.usermgmt;

import com.example.usermgmt.entity.User;
import com.example.usermgmt.repository.UserRepository;
import com.example.usermgmt.service.UserService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Test Cases cho màn hình User Management
 * Sử dụng Partition Testing theo bảng phân vùng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserManagementPartitionTest {
    
    @Autowired
    private UserService service;
    
    @Autowired
    private UserRepository repository;
    
    @After
    public void cleanup() {
        repository.deleteAll();
    }
    
    // ============ PARTITION 1-3: USERNAME TESTING ============
    
    /**
     * TC1: Username - Chuỗi rỗng (length = 0)
     * Kết quả: FAIL - Username is required
     */
    @Test
    public void testPartition1_Username_Empty() {
        User user = new User();
        user.setUsername("");
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for empty username");
        } catch (Exception e) {
            assertEquals("Username is required", e.getMessage());
        }
    }
    
    /**
     * TC2: Username - Chuỗi hợp lệ (0 < length <= 50)
     * Kết quả: PASS - Lưu thành công
     */
    @Test
    public void testPartition2_Username_Valid() throws Exception {
        User user = new User();
        user.setUsername("john_doe");
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        
        User saved = service.saveUser(user);
        
        assertNotNull(saved.getId());
        assertEquals("john_doe", saved.getUsername());
    }
    
    /**
     * TC3: Username - Chuỗi quá dài (length > 50)
     * Kết quả: FAIL - Username must not exceed 50 characters
     */
    @Test
    public void testPartition3_Username_TooLong() {
        User user = new User();
        user.setUsername("a".repeat(51));
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for username > 50 chars");
        } catch (Exception e) {
            assertEquals("Username must not exceed 50 characters", e.getMessage());
        }
    }
    
    // ============ PARTITION 4-6: FULL NAME TESTING ============
    
    /**
     * TC4: Full Name - Chuỗi rỗng (length = 0)
     * Kết quả: FAIL - Full name is required
     */
    @Test
    public void testPartition4_FullName_Empty() {
        User user = new User();
        user.setUsername("user1");
        user.setFullName("");
        user.setEmail("user1@example.com");
        user.setPassword("password123");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for empty full name");
        } catch (Exception e) {
            assertEquals("Full name is required", e.getMessage());
        }
    }
    
    /**
     * TC5: Full Name - Chuỗi hợp lệ (0 < length <= 100)
     * Kết quả: PASS - Lưu thành công
     */
    @Test
    public void testPartition5_FullName_Valid() throws Exception {
        User user = new User();
        user.setUsername("user2");
        user.setFullName("Jane Smith");
        user.setEmail("jane@example.com");
        user.setPassword("password123");
        
        User saved = service.saveUser(user);
        
        assertNotNull(saved.getId());
        assertEquals("Jane Smith", saved.getFullName());
    }
    
    /**
     * TC6: Full Name - Chuỗi quá dài (length > 100)
     * Kết quả: FAIL - Full name must not exceed 100 characters
     */
    @Test
    public void testPartition6_FullName_TooLong() {
        User user = new User();
        user.setUsername("user3");
        user.setFullName("a".repeat(101));
        user.setEmail("user3@example.com");
        user.setPassword("password123");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for full name > 100 chars");
        } catch (Exception e) {
            assertEquals("Full name must not exceed 100 characters", e.getMessage());
        }
    }
    
    // ============ PARTITION 7-9: EMAIL TESTING ============
    
    /**
     * TC7: Email - Chuỗi rỗng (length = 0)
     * Kết quả: FAIL - Email is required
     */
    @Test
    public void testPartition7_Email_Empty() {
        User user = new User();
        user.setUsername("user4");
        user.setFullName("Test User");
        user.setEmail("");
        user.setPassword("password123");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for empty email");
        } catch (Exception e) {
            assertEquals("Email is required", e.getMessage());
        }
    }
    
    /**
     * TC8: Email - Format hợp lệ (valid email format)
     * Kết quả: PASS - Lưu thành công
     */
    @Test
    public void testPartition8_Email_Valid() throws Exception {
        User user = new User();
        user.setUsername("user5");
        user.setFullName("Valid User");
        user.setEmail("valid@example.com");
        user.setPassword("password123");
        
        User saved = service.saveUser(user);
        
        assertNotNull(saved.getId());
        assertEquals("valid@example.com", saved.getEmail());
    }
    
    /**
     * TC9: Email - Format không hợp lệ (invalid email format)
     * Kết quả: FAIL - Email format is invalid
     */
    @Test
    public void testPartition9_Email_InvalidFormat() {
        User user = new User();
        user.setUsername("user6");
        user.setFullName("Invalid User");
        user.setEmail("invalid-email");
        user.setPassword("password123");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for invalid email format");
        } catch (Exception e) {
            assertEquals("Email format is invalid", e.getMessage());
        }
    }
    
    /**
     * TC10: Email - Quá dài (length > 100)
     * Kết quả: FAIL - Email must not exceed 100 characters
     */
    @Test
    public void testPartition10_Email_TooLong() {
        User user = new User();
        user.setUsername("user7");
        user.setFullName("Test User");
        user.setEmail("a".repeat(90) + "@example.com"); // > 100 chars
        user.setPassword("password123");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for email > 100 chars");
        } catch (Exception e) {
            assertEquals("Email must not exceed 100 characters", e.getMessage());
        }
    }
    
    // ============ PARTITION 11-13: PASSWORD TESTING ============
    
    /**
     * TC11: Password - Chuỗi rỗng (length = 0)
     * Kết quả: FAIL - Password is required
     */
    @Test
    public void testPartition11_Password_Empty() {
        User user = new User();
        user.setUsername("user8");
        user.setFullName("Test User");
        user.setEmail("test8@example.com");
        user.setPassword("");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for empty password");
        } catch (Exception e) {
            assertEquals("Password is required", e.getMessage());
        }
    }
    
    /**
     * TC12: Password - Quá ngắn (length < 6)
     * Kết quả: FAIL - Password must be at least 6 characters
     */
    @Test
    public void testPartition12_Password_TooShort() {
        User user = new User();
        user.setUsername("user9");
        user.setFullName("Test User");
        user.setEmail("test9@example.com");
        user.setPassword("123");
        
        try {
            service.saveUser(user);
            fail("Should throw exception for password < 6 chars");
        } catch (Exception e) {
            assertEquals("Password must be at least 6 characters", e.getMessage());
        }
    }
    
    /**
     * TC13: Password - Hợp lệ (6 <= length <= 100)
     * Kết quả: PASS - Lưu thành công
     */
    @Test
    public void testPartition13_Password_Valid() throws Exception {
        User user = new User();
        user.setUsername("user10");
        user.setFullName("Valid User");
        user.setEmail("test10@example.com");
        user.setPassword("validpass123");
        
        User saved = service.saveUser(user);
        
        assertNotNull(saved.getId());
        assertEquals("validpass123", saved.getPassword());
    }
    
    /**
     * TC14: Password - Quá dài (length > 100)
     * Kết quả: FAIL - Password must not exceed 100 characters
     */
    @Test
    public void testPartition14_Password_TooLong() {
        User user = new User();
        user.setUsername("user11");
        user.setFullName("Test User");
        user.setEmail("test11@example.com");
        user.setPassword("a".repeat(101));
        
        try {
            service.saveUser(user);
            fail("Should throw exception for password > 100 chars");
        } catch (Exception e) {
            assertEquals("Password must not exceed 100 characters", e.getMessage());
        }
    }
}
