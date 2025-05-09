package com.followup.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("")
public class HomeController {

    @GetMapping("/")
    public String landingPage() {
        return "index";
    }

}
