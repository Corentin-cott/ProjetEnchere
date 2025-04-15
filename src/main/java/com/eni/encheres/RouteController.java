package com.eni.encheres;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RouteController {

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/liste-encheres";
    }

    @GetMapping("/liste-encheres")
    public String listeEncheresPage() {
        return "listeEncheres";
    }

    @GetMapping("/connection")
    public String connectionPage() {
        return "connection";
    }
}
