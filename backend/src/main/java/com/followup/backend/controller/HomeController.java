package com.followup.backend.controller;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.followup.backend.dto.AddFollowUpFormDTO;
import com.followup.backend.dto.CourseDTO;
import com.followup.backend.dto.DepartmentDTO;
import com.followup.backend.dto.EditLeadDetailsDTO;
import com.followup.backend.dto.EmployeeReportDTO;
import com.followup.backend.dto.EmployeeSummaryDTO;
import com.followup.backend.dto.FollowUpTrendDTO;
import com.followup.backend.dto.NewFollowUpFormDTO;
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
    private FollowUpNodeRepository followUpNodeRepository;

    @Autowired
    private BasicEmployeeRepository basicEmployeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LeadRepository leadRepository;

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

        NewFollowUpFormDTO newFollowUpForm = new NewFollowUpFormDTO();

        model.addAttribute("user", user);
        model.addAttribute("departments", departments);
        model.addAttribute("courses", courses);
        model.addAttribute("newFollowUpForm", newFollowUpForm);
        return "new-followup";
    }

    @PostMapping("/new-followup")
    public String createNewFollowup(
            @ModelAttribute("newFollowUpForm") NewFollowUpFormDTO newFollowUpForm,
            RedirectAttributes redirAttrs,
            HttpSession session) {

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(newFollowUpForm.getFollowUpDate(), formatter);

        if (date.isBefore(LocalDate.now())) {
            redirAttrs.addFlashAttribute("error", "Date cannot be in the past");
            return "redirect:/new-followup";
        }

        Lead lead = new Lead();
        lead.setName(newFollowUpForm.getLeadName());
        lead.setEmail(newFollowUpForm.getEmail());
        lead.setPhoneNumber(newFollowUpForm.getMobile());
        lead.setAddress(newFollowUpForm.getAddress());
        lead.setDeleted(false);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());
        lead = leadRepository.save(lead);

        FollowUp followUp = new FollowUp();
        followUp.setLead(lead);
        followUp.setDescription(newFollowUpForm.getFollowUpDescription());
        followUp.setStatus(FollowUp.Status.PENDING);
        followUp.setDueDate(LocalDateTime.now());
        followUp.setCreatedAt(LocalDateTime.now());
        followUp.setUpdatedAt(LocalDateTime.now());
        followUp.setDeleted(false);
        followUp.setCourse(courseRepository.findById(newFollowUpForm.getCourseCode()).orElse(null));
        followUp.setEmployee(user);
        followUp = followUpRepository.save(followUp);

        FollowUpNode followUpNode = new FollowUpNode();
        followUpNode.setTitle(newFollowUpForm.getFollowUpTitle());
        followUpNode.setBody(newFollowUpForm.getFollowUpDescription());

        followUpNode.setDate(date.atStartOfDay());
        followUpNode.setDoneBy(user);
        followUpNode.setCreatedAt(LocalDateTime.now());
        followUpNode.setFollowUp(followUp);
        followUpNode = followUpNodeRepository.save(followUpNode);
        followUp.getNodes().add(followUpNode);
        followUpRepository.save(followUp);
        leadRepository.save(lead);

        redirAttrs.addFlashAttribute("message", "Follow-up created successfully");
        return "redirect:/remaining-followup";
    }

    @GetMapping("/remaining-followup/{id}")
    public String showFollowUpDetails(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirAttrs) {

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

            followUp.getNodes().sort((n1, n2) -> n2.getDate().compareTo(n1.getDate()));

            model.addAttribute("followUp", followUp);
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("message", "FollowUp not found");
            return "redirect:/remaining-followup";
        }

        model.addAttribute("user", user);

        return "remaining-followup-details"; // your view name
    }

    @GetMapping("/remaining-followup/{id}/mark-complete")
    public String markFollowUpComplete(@PathVariable Long id, RedirectAttributes redirAttrs, HttpSession session) {
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

        FollowUp followUp = followUpRepository.findById(id).orElse(null);
        if (followUp == null) {
            redirAttrs.addFlashAttribute("error", "Follow-up not found");
            return "redirect:/remaining-followup";
        }

        followUp.setStatus(FollowUp.Status.COMPLETED);
        followUp.setUpdatedAt(LocalDateTime.now());
        followUp.setDeleted(false);
        followUp.setEmployee(user);

        followUpRepository.save(followUp); // ✅ only need to update the follow-up
        redirAttrs.addFlashAttribute("message", "Follow-up marked as completed");

        return "redirect:/remaining-followup";
    }

    @GetMapping("/remaining-followup/{id}/add-followup")
    public String showAddFollowUpNodePage(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirAttrs) {

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

        FollowUp followUp = followUpRepository.findById(id).orElse(null);
        if (followUp == null) {
            redirAttrs.addFlashAttribute("error", "Follow-up not found");
            return "redirect:/remaining-followup";
        }

        AddFollowUpFormDTO addFollowUpForm = new AddFollowUpFormDTO();

        model.addAttribute("user", user);
        model.addAttribute("followUp", followUp);
        model.addAttribute("addFollowUpForm", addFollowUpForm);

        return "add-followup";
    }

    @PostMapping("/remaining-followup/{id}/add-followup")
    public String addFollowUpNode(
            @PathVariable Long id,
            @ModelAttribute("addFollowUpForm") AddFollowUpFormDTO addFollowUpForm,
            RedirectAttributes redirAttrs,
            HttpSession session) {

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

        FollowUp followUp = followUpRepository.findById(id).orElse(null);
        if (followUp == null) {
            redirAttrs.addFlashAttribute("error", "Follow-up not found");
            return "redirect:/remaining-followup";
        }

        LocalDate date = null;

        String rawDate = addFollowUpForm.getDate(); // e.g., "20 MAY 2025"
        String[] parts = rawDate.split(" ");
        if (parts.length == 3) {
            String day = parts[0];
            String month = parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1).toLowerCase(); // MAY -> May
            String year = parts[2];
            String normalizedDate = day + " " + month + " " + year;
            System.out.println("Normalized date: " + normalizedDate);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
            date = LocalDate.parse(normalizedDate, formatter);
        }

        if (date.isBefore(LocalDate.now())) {
            redirAttrs.addFlashAttribute("error", "Date cannot be in the past");
            return "redirect:/remaining-followup/" + id + "/add-followup";
        }

        FollowUpNode followUpNode = new FollowUpNode();
        followUpNode.setTitle(addFollowUpForm.getTitle());
        followUpNode.setBody(addFollowUpForm.getDescription());

        System.out.println("Date: " + date);
        followUpNode.setDate(date.atStartOfDay());
        followUpNode.setDoneBy(user);
        followUpNode.setCreatedAt(LocalDateTime.now());
        followUpNode.setFollowUp(followUp);
        followUpNode = followUpNodeRepository.save(followUpNode);

        followUp.getNodes().add(followUpNode);
        followUp.setUpdatedAt(LocalDateTime.now());
        followUp.setDueDate(date.atStartOfDay());
        followUpRepository.save(followUp);

        redirAttrs.addFlashAttribute("message", "Follow-up node added successfully");
        return "redirect:/remaining-followup/" + id;
    }

    @GetMapping("/remaining-followup/{id}/edit-lead-details")
    public String showEditLeadDetailsPage(HttpSession session, @PathVariable Long id, Model model,
            RedirectAttributes redirAttrs) {
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

        EditLeadDetailsDTO editLeadDetailsDTO = new EditLeadDetailsDTO();
        FollowUp followUp = followUpRepository.findById(id).orElse(null);
        if (followUp != null) {
            editLeadDetailsDTO.setFollowUpId(id);
            editLeadDetailsDTO.setLeadId(followUp.getLead().getId());
            editLeadDetailsDTO.setLeadName(followUp.getLead().getName());
            editLeadDetailsDTO.setMobile(followUp.getLead().getPhoneNumber());
            editLeadDetailsDTO.setEmail(followUp.getLead().getEmail());
            editLeadDetailsDTO.setAddress(followUp.getLead().getAddress());
            editLeadDetailsDTO.setDepartmentId(followUp.getCourse().getDepartment().getId());
            editLeadDetailsDTO.setCourseCode(followUp.getCourse().getCode());
            followUp.getNodes().sort((n1, n2) -> n2.getDate().compareTo(n1.getDate()));
            editLeadDetailsDTO.setNodes(followUp.getNodes());
        }

        model.addAttribute("user", user);
        model.addAttribute("departments", departments);
        model.addAttribute("courses", courses);
        model.addAttribute("editLeadDetailsDTO", editLeadDetailsDTO);
        return "edit-lead-details";
    }

    @PostMapping("/remaining-followup/{id}/edit-lead-details")
    public String editLeadDetails(
            @PathVariable Long id,
            @ModelAttribute("editLeadDetailsDTO") EditLeadDetailsDTO editLeadDetailsDTO,
            RedirectAttributes redirAttrs,
            HttpSession session) {

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

        FollowUp followUp = followUpRepository.findById(id).orElse(null);
        if (followUp == null) {
            redirAttrs.addFlashAttribute("error", "Follow-up not found");
            return "redirect:/remaining-followup";
        }

        Lead lead = leadRepository.findById(followUp.getLead().getId()).orElse(null);
        if (lead == null) {
            redirAttrs.addFlashAttribute("error", "Lead not found");
            return "redirect:/remaining-followup";
        }

        lead.setName(editLeadDetailsDTO.getLeadName());
        lead.setEmail(editLeadDetailsDTO.getEmail());
        lead.setPhoneNumber(editLeadDetailsDTO.getMobile());
        lead.setAddress(editLeadDetailsDTO.getAddress());
        lead.setUpdatedAt(LocalDateTime.now());

        Course course = courseRepository.findById(editLeadDetailsDTO.getCourseCode()).orElse(null);

        if (followUp.getCourse().getCode() != course.getCode()) {
            System.out.println(
                    "\n\nCourse changed from " + followUp.getCourse().getName() + " to " + course.getName() + "\n\n");

            // add a new node for course change
            FollowUpNode followUpNode = new FollowUpNode();
            followUpNode.setTitle("Course Change");
            followUpNode.setBody("Course changed from " + followUp.getCourse().getName() + " to " + course.getName());
            followUpNode.setDate(LocalDateTime.now());
            followUpNode.setDoneBy(user);
            followUpNode.setFollowUp(followUp);
            followUp.getNodes().add(followUpNode);

        }

        if (course != null) {
            followUp.setCourse(course);
        }

        followUp.setUpdatedAt(LocalDateTime.now());
        followUp.setDueDate(LocalDateTime.now());

        leadRepository.save(lead);
        followUpRepository.save(followUp);

        redirAttrs.addFlashAttribute("message", "Lead details updated successfully");
        return "redirect:/remaining-followup/" + id;
    }

    @GetMapping("/completed-followup/{id}")
    public String showCFollowUpDetails(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirAttrs) {

        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || role == null || (!role.equals("FOLLOWUP_EMPLOYEE") && !role.equals("ADMIN"))) {
            redirAttrs.addFlashAttribute("error", "Please login first");
            return "redirect:/login";
        }

        if (role.equals("ADMIN")) {
            Admin user = adminRepository.findByEmail(email);
            if (user == null) {
                redirAttrs.addFlashAttribute("error", "User not found");
                return "redirect:/login";
            }

            model.addAttribute("user", user);
        } else {
            FollowUpEmployee user = followUpEmployeeRepository.findByEmail(email);
            if (user == null) {
                redirAttrs.addFlashAttribute("error", "User not found");
                return "redirect:/login";
            }

            model.addAttribute("user", user);
        }

        Optional<FollowUp> optionalFollowUp = followUpRepository.findById(id);
        if (optionalFollowUp.isEmpty()) {
            redirAttrs.addFlashAttribute("message", "FollowUp not found");
            return "redirect:/completed-followup";
        }
        model.addAttribute("followUp", optionalFollowUp.get());

        System.out.println("\n\nRole: " + session.getAttribute("userRole") + "\n\n");
        System.out.println("\n\nEmail: " + session.getAttribute("userEmail") + "\n\n");

        return "completed-followup-details";
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

        if (email == null || role == null
                || (!role.equals("FOLLOWUP_EMPLOYEE") && !role.equals("ADMIN") && !role.equals("BASIC_EMPLOYEE"))) {
            redirAttrs.addFlashAttribute("error", "Please login first");
            return "redirect:/login";
        }

        if (role.equals("ADMIN")) {
            Admin user = adminRepository.findByEmail(email);
            if (user == null) {
                redirAttrs.addFlashAttribute("error", "User not found");
                return "redirect:/login";
            }

            List<Task> tasks = new ArrayList<>(user.getTasks().stream().filter(t -> !t.isCompleted()).toList());
            tasks.sort((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()));
            
            model.addAttribute("tasks", tasks);
            model.addAttribute("user", user);
        } else if (role.equals("FOLLOWUP_EMPLOYEE")) {
            FollowUpEmployee user = followUpEmployeeRepository.findByEmail(email);
            if (user == null) {
                redirAttrs.addFlashAttribute("error", "User not found");
                return "redirect:/login";
            }

            List<Task> tasks = user.getTasks().stream().filter(t -> !t.isCompleted()).toList();
            tasks.sort((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()));

            model.addAttribute("tasks", tasks);
            model.addAttribute("user", user);
        } else if (role.equals("BASIC_EMPLOYEE")) {
            BasicEmployee user = basicEmployeeRepository.findByEmail(email);
            if (user == null) {
                redirAttrs.addFlashAttribute("error", "User not found");
                return "redirect:/login";
            }
            List<Task> tasks = user.getTasks().stream().filter(t -> !t.isCompleted()).toList();
            tasks.sort((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()));

            model.addAttribute("tasks", tasks);
            model.addAttribute("user", user);
        }

        return "task";
    }

    @GetMapping("/data")
    public String testData() {
        System.out.println("\n\n" + adminRepository.findAll() + "\n\n");
        return "login";
    }

    @GetMapping("/report")
    public String showReportPage(HttpSession session, Model model, RedirectAttributes redirAttrs) {
        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || !role.equals("ADMIN")) {
            redirAttrs.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/login";
        }

        Admin user = adminRepository.findByEmail(email);
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

        List<FollowUp> last12MonthsFollowUps = followUpRepository
                .findByCreatedAtAfter(LocalDate.now().minusMonths(12).atStartOfDay());

        List<FollowUpEmployee> employees = followUpEmployeeRepository.findAll();

        List<EmployeeSummaryDTO> employeeSummaries = employees.stream()
                .map(e -> {
                    long todayCount = e.getCompletedFollowUps().stream()
                            .filter(f -> f.getCreatedAt().toLocalDate().isEqual(LocalDate.now()))
                            .count();

                    long weeklyCount = e.getCompletedFollowUps().stream()
                            .filter(f -> f.getCreatedAt().toLocalDate().isAfter(LocalDate.now().minusDays(6)))
                            .count();

                    long monthlyCount = e.getCompletedFollowUps().stream()
                            .filter(f -> f.getCreatedAt().toLocalDate().isAfter(LocalDate.now().minusDays(30)))
                            .count();

                    return new EmployeeSummaryDTO(
                            e.getId(),
                            e.getName(),
                            e.getDepartment() != null ? e.getDepartment().getName() : "N/A",
                            todayCount,
                            weeklyCount,
                            monthlyCount,
                            e.getDepartment() != null ? e.getDepartment().getId() : null);
                })
                .toList();

        List<FollowUpTrendDTO> followUpTrends = last12MonthsFollowUps.stream()
                .map(f -> new FollowUpTrendDTO(
                        f.getCreatedAt().toLocalDate(),
                        f.getStatus() == FollowUp.Status.COMPLETED ? 1 : 0))
                .toList();

        model.addAttribute("employeeSummaries", employeeSummaries);
        model.addAttribute("followUpTrends", followUpTrends);
        model.addAttribute("departments", departments);
        model.addAttribute("courses", courses);
        model.addAttribute("user", user);

        return "report-dashboard";
    }

    @GetMapping("/employee/{id}/report")
    public String showEmployeeReportPage(@PathVariable Long id, HttpSession session, Model model,
            RedirectAttributes redirAttrs) {
        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || !role.equals("ADMIN")) {
            redirAttrs.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/login";
        }

        Admin user = adminRepository.findByEmail(email);
        if (user == null) {
            redirAttrs.addFlashAttribute("error", "User not found");
            return "redirect:/login";
        }

        EmployeeReportDTO employeeReportDTO = new EmployeeReportDTO();

        FollowUpEmployee employee = followUpEmployeeRepository.findById(id).orElse(null);
        if (employee == null) {
            redirAttrs.addFlashAttribute("error", "Employee not found");
            return "redirect:/report";
        }

        employeeReportDTO.setId(employee.getId());
        employeeReportDTO.setName(employee.getName());
        employeeReportDTO
                .setDepartmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : "N/A");
        employeeReportDTO.setEmail(employee.getEmail());
        employeeReportDTO.setPhoneNumber(employee.getPhoneNumber());

        List<FollowUp> followUps = employee.getCompletedFollowUps();
        employeeReportDTO.setFollowUps(followUps);

        model.addAttribute("employeeReport", employeeReportDTO);
        model.addAttribute("user", user);

        return "employee-report";
    }

    @GetMapping("/master/course")
    public String showMasterCoursePage(HttpSession session, Model model, RedirectAttributes redirAttrs) {

        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || !role.equals("ADMIN")) {
            redirAttrs.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/login";
        }

        Admin user = adminRepository.findByEmail(email);
        if (user == null) {
            redirAttrs.addFlashAttribute("error", "User not found");
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "add-course";

    }

    @GetMapping("/master/department")
    public String showMasterDepartmentPage(HttpSession session, Model model, RedirectAttributes redirAttrs) {

        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || !role.equals("ADMIN")) {
            redirAttrs.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/login";
        }

        Admin user = adminRepository.findByEmail(email);
        if (user == null) {
            redirAttrs.addFlashAttribute("error", "User not found");
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "add-department";

    }

    @GetMapping("/master/employee")
    public String showMasterEmployeePage(HttpSession session, Model model, RedirectAttributes redirAttrs) {

        String email = (String) session.getAttribute("userEmail");
        String role = (String) session.getAttribute("userRole");

        if (email == null || !role.equals("ADMIN")) {
            redirAttrs.addFlashAttribute("error", "Unauthorized access");
            return "redirect:/login";
        }

        Admin user = adminRepository.findByEmail(email);
        if (user == null) {
            redirAttrs.addFlashAttribute("error", "User not found");
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "add-employee";

    }

}
