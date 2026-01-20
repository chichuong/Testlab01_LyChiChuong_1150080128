package com.example.baitaptuan4.organization;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService service;

    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new OrganizationForm());
        model.addAttribute("savedOrgId", null); // để disable Director
        return "org-form";
    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("form") OrganizationForm form,
            BindingResult binding,
            Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("savedOrgId", null);
            return "org-form";
        }

        try {
            Organization saved = service.create(form);
            model.addAttribute("message", "Save successfully");
            model.addAttribute("savedOrgId", saved.getOrgId()); // enable Director
            return "org-form";
        } catch (OrgNameAlreadyExistsException ex) {
            binding.rejectValue("orgName", "duplicate", ex.getMessage());
            model.addAttribute("savedOrgId", null);
            return "org-form";
        }
    }
}
