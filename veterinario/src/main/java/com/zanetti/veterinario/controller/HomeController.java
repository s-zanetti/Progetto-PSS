package com.zanetti.veterinario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// msotra la pagina home.html
    @GetMapping("/")
    public String home() {
        return "home";
    }
}