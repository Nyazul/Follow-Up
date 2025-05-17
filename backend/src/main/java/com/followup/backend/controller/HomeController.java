package com.followup.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.followup.backend.dto.CourseDTO;
import com.followup.backend.dto.DepartmentDTO;
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
    private FollowUpRepository followUpRepository;

    @Autowired
    private BasicEmployeeRepository basicEmployeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

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
                return "redirect:/admin-pannel";
            }
        } else {
            FollowUpEmployee user = followUpEmployeeRepository.findByEmailAndPassword(email, password);
            if (user != null) {
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userRole", user.getRole());
                return "redirect:/remaining-followup";
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
    @GetMapping("/remaining-followup")
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
        return "remaining-followup";
    }

    @Transactional
    @GetMapping("/completed-followup")
    public String showCompletedFollowupPage(HttpSession session, Model model, RedirectAttributes redirAttrs) {
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
        return "completed-followup";
    }

    @GetMapping("/new-followup")
    public String showNewFollowupPage(HttpSession session, Model model, RedirectAttributes redirAttrs) {
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

        List<DepartmentDTO> departments = departmentRepository.findAll()
                .stream()
                .map(d -> new DepartmentDTO(d.getId(), d.getName()))
                .toList();

        List<CourseDTO> courses = courseRepository.findAll()
                .stream()
                .map(c -> new CourseDTO(c.getCode(), c.getName(), c.getDepartment().getId()))
                .toList();

        model.addAttribute("user", user);
        model.addAttribute("departments", departments);
        model.addAttribute("courses", courses);
        return "new-followup";
    }
    

    @GetMapping("/remaining-followup/{id}")
    public String showFollowUpDetails(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirAttrs) {

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

        try {
            FollowUp followUp = followUpRepository.findById(id).orElseThrow(() -> new Exception("FollowUp not found"));
            model.addAttribute("followUp", followUp);
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("message", "FollowUp not found");
            return "redirect:/remaining-followup";
        }

        model.addAttribute("user", user);

        return "remaining-followup-details"; // your view name
    }

    @GetMapping("/admin-pannel")
    public String adminPannel(HttpSession session, RedirectAttributes redirAttrs) {
        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || !role.equals("ADMIN")) {
            redirAttrs.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/login";
        }

        // Optional: fetch and pass admin user if needed
        return "admin-pannel";
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
