package com.eni.encheres.controller;

import com.eni.encheres.bo.Enchere;
import com.eni.encheres.service.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EnchereController {

    @Autowired
    EnchereService enchereService;

    @GetMapping("/encheres")
    public String afficherEncheres(Model model) {
        List<Enchere> enchereList = enchereService.getEncheresEnCours();
        model.addAttribute("encheres", enchereList);
        return "listeEncheres";
    }
}
