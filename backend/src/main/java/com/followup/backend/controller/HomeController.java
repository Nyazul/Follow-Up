package com.followup.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.followup.backend.model.User;
import com.followup.backend.repository.AdminRepository;
import com.followup.backend.repository.BasicEmployeeRepository;
import com.followup.backend.repository.FollowUpEmployeeRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    FollowUpEmployeeRepository followUpEmployeeRepository;

    @Autowired
    BasicEmployeeRepository basicEmployeeRepository;

    @GetMapping("/")
    public String landingPage() {
        return "login";
    }

    @GetMapping("/login")
    public String helloPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(
        @RequestParam String email, 
        @RequestParam String password, 
        @RequestParam String role,
        RedirectAttributes redirAttrs,
        HttpSession session) {

        User user = null;

        if (role.equals("ADMIN")) {
            

            if ((user = adminRepository.findByEmailAndPassword(email, password)) != null) {
                session.setAttribute("user", user);
                return "adminpannel";
            } else {
                redirAttrs.addFlashAttribute("error", "Invalid email or password");
                return "redirect:/login";
            }
            
        } else if (role.equals("EMPLOYEE")) {
            if ((user = followUpEmployeeRepository.findByEmailAndPassword(email, password)) != null) {
                session.setAttribute("user", user);
                return "remainingfollowup";

            } else if ((user = basicEmployeeRepository.findByEmailAndPassword(email, password)) != null) {
                session.setAttribute("user", user);
                return "task";

            } else {
                redirAttrs.addFlashAttribute("error", "Invalid email or password");
                return "redirect:/login";
            }
        }
        
        return "";
        
    }
    

}
