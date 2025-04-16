package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.Enchere;
import com.eni.encheres.service.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class EnchereController {

    @Autowired
    EnchereService enchereService;

    @GetMapping("/encheres")
    public String afficherEncheres(
            @RequestParam(required = false) List<String> encheresAchat,
            @RequestParam(required = false) List<String> encheresVente,
            Model model,
            Principal principal) {

        // Récupération du pseudo utilisateur connecté
        String pseudoUtilisateur = principal.getName();

        // Appelle le service en lui passant les filtres achat, vente et l’utilisateur
        List<Enchere> enchereList = enchereService.filtrerEncheres(encheresAchat, encheresVente, pseudoUtilisateur);

        model.addAttribute("encheres", enchereList);
        return "listeEncheres";
    }
}
