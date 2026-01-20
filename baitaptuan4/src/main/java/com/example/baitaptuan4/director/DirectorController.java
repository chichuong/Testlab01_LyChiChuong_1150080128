package com.example.baitaptuan4.director;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/directors")
public class DirectorController {

    @GetMapping("/manage")
    public String manage(@RequestParam("orgId") Integer orgId, Model model) {
        model.addAttribute("orgId", orgId);
        return "director-management";
    }
}
