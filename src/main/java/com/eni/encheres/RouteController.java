package com.eni.encheres;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouteController {

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/connection";
    }

    @GetMapping("/connection")
    public String loginPage() {
        return "connection";
    }
}
