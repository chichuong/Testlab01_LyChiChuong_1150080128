package com.example.orgunit;

import com.example.orgunit.entity.OrganizationUnit;
import com.example.orgunit.repository.OrganizationUnitRepository;
import com.example.orgunit.service.OrganizationUnitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationUnitTest {
    
    @Autowired
    private OrganizationUnitService service;
    
    @Autowired
    private OrganizationUnitRepository repository;
    
    /**
     * Test Case 1: Thêm unit với dữ liệu hợp lệ (có cả Name và Description)
     * Kết quả mong đợi: Unit được lưu thành công vào database
     */
    @Test
    public void testSaveUnitWithValidData() {
        OrganizationUnit unit = new OrganizationUnit();
        unit.setName("IT Department");
        unit.setDescription("Information Technology Department");
        
        OrganizationUnit saved = service.saveUnit(unit);
        
        assertNotNull("Unit ID should not be null after saving", saved.getUnitId());
        assertEquals("IT Department", saved.getName());
        assertEquals("Information Technology Department", saved.getDescription());
        
        // Clean up
        repository.deleteById(saved.getUnitId());
    }
    
    /**
     * Test Case 2: Thêm unit với Name rỗng (vi phạm validation)
     * Kết quả mong đợi: Ném ra IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSaveUnitWithEmptyName() {
        OrganizationUnit unit = new OrganizationUnit();
        unit.setName("");
        unit.setDescription("Test Description");
        
        service.saveUnit(unit);
    }
    
    /**
     * Test Case 3: Thêm unit với Name null
     * Kết quả mong đợi: Ném ra IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSaveUnitWithNullName() {
        OrganizationUnit unit = new OrganizationUnit();
        unit.setName(null);
        unit.setDescription("Test Description");
        
        service.saveUnit(unit);
    }
    
    /**
     * Test Case 4: Thêm unit chỉ có Name, không có Description
     * Kết quả mong đợi: Unit được lưu thành công (Description là optional)
     */
    @Test
    public void testSaveUnitWithoutDescription() {
        OrganizationUnit unit = new OrganizationUnit();
        unit.setName("HR Department");
        
        OrganizationUnit saved = service.saveUnit(unit);
        
        assertNotNull("Unit should be saved successfully", saved.getUnitId());
        assertEquals("HR Department", saved.getName());
        assertNull("Description should be null", saved.getDescription());
        
        // Clean up
        repository.deleteById(saved.getUnitId());
    }
    
    /**
     * Test Case 5: Thêm unit với Name chứa khoảng trắng
     * Kết quả mong đợi: Lỗi vì Name rỗng sau khi trim
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSaveUnitWithWhitespaceName() {
        OrganizationUnit unit = new OrganizationUnit();
        unit.setName("   ");
        unit.setDescription("Test Description");
        
        service.saveUnit(unit);
    }
    
    /**
     * Test Case 6: Lấy danh sách tất cả units
     * Kết quả mong đợi: Trả về list không null
     */
    @Test
    public void testGetAllUnits() {
        assertNotNull("List should not be null", service.getAllUnits());
    }
    
    /**
     * Test Case 7: Update unit đã tồn tại
     * Kết quả mong đợi: Unit được cập nhật thành công
     */
    @Test
    public void testUpdateExistingUnit() {
        // Create unit
        OrganizationUnit unit = new OrganizationUnit();
        unit.setName("Finance Department");
        unit.setDescription("Original Description");
        OrganizationUnit saved = service.saveUnit(unit);
        
        // Update unit
        saved.setName("Finance & Accounting");
        saved.setDescription("Updated Description");
        OrganizationUnit updated = service.saveUnit(saved);
        
        assertEquals("Finance & Accounting", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        
        // Clean up
        repository.deleteById(saved.getUnitId());
    }
    
    /**
     * Test Case 8: Xóa unit theo ID
     * Kết quả mong đợi: Unit bị xóa khỏi database
     */
    @Test
    public void testDeleteUnit() {
        // Create unit
        OrganizationUnit unit = new OrganizationUnit();
        unit.setName("Temp Department");
        OrganizationUnit saved = service.saveUnit(unit);
        Long id = saved.getUnitId();
        
        // Delete unit
        service.deleteUnit(id);
        
        // Verify deletion
        assertFalse("Unit should not exist after deletion", 
                   service.getUnitById(id).isPresent());
    }
}
