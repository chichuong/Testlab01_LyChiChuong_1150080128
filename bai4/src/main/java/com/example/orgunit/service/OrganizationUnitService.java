package com.example.orgunit.service;

import com.example.orgunit.entity.OrganizationUnit;
import com.example.orgunit.repository.OrganizationUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationUnitService {
    
    @Autowired
    private OrganizationUnitRepository repository;
    
    public List<OrganizationUnit> getAllUnits() {
        return repository.findAll();
    }
    
    public Optional<OrganizationUnit> getUnitById(Long id) {
        return repository.findById(id);
    }
    
    public OrganizationUnit saveUnit(OrganizationUnit unit) {
        // Validate name is not empty
        if (unit.getName() == null || unit.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        return repository.save(unit);
    }
    
    public void deleteUnit(Long id) {
        repository.deleteById(id);
    }
}
