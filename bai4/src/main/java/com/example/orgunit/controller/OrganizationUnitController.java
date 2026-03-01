package com.example.orgunit.controller;

import com.example.orgunit.entity.OrganizationUnit;
import com.example.orgunit.service.OrganizationUnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orgunit")
public class OrganizationUnitController {
    
    @Autowired
    private OrganizationUnitService service;
    
    @GetMapping("/list")
    public String listUnits(Model model) {
        model.addAttribute("units", service.getAllUnits());
        return "list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("unit", new OrganizationUnit());
        return "add-form";
    }
    
    @PostMapping("/save")
    public String saveUnit(@Valid @ModelAttribute("unit") OrganizationUnit unit, 
                          BindingResult result, 
                          Model model) {
        if (result.hasErrors()) {
            return "add-form";
        }
        
        try {
            service.saveUnit(unit);
            return "redirect:/orgunit/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "add-form";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        OrganizationUnit unit = service.getUnitById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid unit Id:" + id));
        model.addAttribute("unit", unit);
        return "add-form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUnit(@PathVariable Long id) {
        service.deleteUnit(id);
        return "redirect:/orgunit/list";
    }
}
