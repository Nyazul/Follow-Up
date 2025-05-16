package com.followup.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.followup.backend.model.*;
import com.followup.backend.repository.*;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping
public class HomeController {


    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private FollowUpEmployeeRepository followUpEmployeeRepository;

    @Autowired
    private BasicEmployeeRepository basicEmployeeRepository;

    @GetMapping("/")
    public String landingPage() {
        return "login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            RedirectAttributes redirAttrs,
            HttpSession session) {

        if (role.equals("ADMIN")) {
            Admin user = adminRepository.findByEmailAndPassword(email, password);
            if (user != null) {
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole());
                return "redirect:/adminpannel";
            }
        } else {
            FollowUpEmployee user = followUpEmployeeRepository.findByEmailAndPassword(email, password);
            if (user != null) {
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole());
                return "redirect:/remainingfollowup";
            }

            BasicEmployee user1 = basicEmployeeRepository.findByEmailAndPassword(email, password);
            if (user1 != null) {
                session.setAttribute("userEmail", user1.getEmail());
                session.setAttribute("userRole", user1.getRole());
                return "redirect:/task";
            }
        }

        redirAttrs.addFlashAttribute("error", "Invalid email or password");
        return "redirect:/login";
    }

    @Transactional
    @GetMapping("/remainingfollowup")
    public String showRemainingFollowupPage(HttpSession session, Model model, RedirectAttributes redirAttrs) {
        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || role == null || !role.equals("FOLLOWUP_EMPLOYEE")) {
            redirAttrs.addFlashAttribute("error", "Please login first");
            return "redirect:/login";
        }

        FollowUpEmployee user = followUpEmployeeRepository.findByEmail(email);
        if (user == null) {
            redirAttrs.addFlashAttribute("error", "User not found");
            return "redirect:/login";
        }        

        model.addAttribute("user", user);
        return "remainingfollowup";
    }

    @GetMapping("/adminpannel")
    public String adminPannel(HttpSession session, RedirectAttributes redirAttrs) {
        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || !role.equals("ADMIN")) {
            redirAttrs.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/login";
        }

        // Optional: fetch and pass admin user if needed
        return "adminpannel";
    }

    @GetMapping("/task")
    public String showTaskPage(HttpSession session, Model model, RedirectAttributes redirAttrs) {
        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || !role.equals("BASIC_EMPLOYEE")) {
            redirAttrs.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/login";
        }

        BasicEmployee user = basicEmployeeRepository.findByEmail(email);
        if (user == null) {
            redirAttrs.addFlashAttribute("error", "User not found");
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "task";
    }

    @GetMapping("/data")
    public String testData() {
        System.out.println("\n\n" + adminRepository.findAll() + "\n\n");
        return "login";
    }
}
