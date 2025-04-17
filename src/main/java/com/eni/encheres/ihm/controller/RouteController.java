package com.eni.encheres.ihm.controller;

import com.eni.encheres.service.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class RouteController {

    @Autowired
    private EnchereService enchereService;

//    @GetMapping("/")
//    public String home(Model model, Principal principal) {
//        String username = principal != null ? principal.getName() : "Anonyme";
//        model.addAttribute("username", username);
//        return "listeEncheres";
//
//    }

    @GetMapping("/connection")
    public String loginPage() {
        return "connection";
    }
}
