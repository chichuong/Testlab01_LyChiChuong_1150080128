package com.example.jobtitle.controller;

import com.example.jobtitle.entity.JobTitle;
import com.example.jobtitle.service.JobTitleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/jobtitle")
public class JobTitleController {
    
    @Autowired
    private JobTitleService service;
    
    @GetMapping("/list")
    public String listJobTitles(Model model) {
        model.addAttribute("jobTitles", service.getAllJobTitles());
        return "job-list";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("jobTitle", new JobTitle());
        return "job-form";
    }
    
    @PostMapping("/save")
    public String saveJobTitle(@Valid @ModelAttribute("jobTitle") JobTitle jobTitle,
                              BindingResult result,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              Model model) {
        if (result.hasErrors()) {
            return "job-form";
        }
        
        try {
            service.saveJobTitle(jobTitle, file);
            return "redirect:/jobtitle/list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "job-form";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        JobTitle jobTitle = service.getJobTitleById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid job title Id:" + id));
        model.addAttribute("jobTitle", jobTitle);
        return "job-form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteJobTitle(@PathVariable Long id) {
        service.deleteJobTitle(id);
        return "redirect:/jobtitle/list";
    }
}
