package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Categorie;
import com.eni.encheres.bo.Enchere;
import com.eni.encheres.dao.IDAOArticleVendu;
import com.eni.encheres.service.EnchereService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class EnchereController {

    @Autowired
    private EnchereService enchereService;

    @Autowired
    private IDAOArticleVendu daoArticleVendu;

    @GetMapping("/")
    public String redirigerVersEncheres() {
        return "redirect:/encheres";
    }

    @GetMapping("/encheres")
    public String afficherEncheres(
            @RequestParam(required = false) List<String> encheresAchat,
            @RequestParam(required = false) List<String> encheresVente,
            @RequestParam(required = false) String recherche,
            @RequestParam(required = false) Long idCategorie,
            Model model,
            Principal principal) {

        // Récupération du pseudo utilisateur connecté
        String pseudoUtilisateur = (principal != null) ? principal.getName() : null;

        // Appelle le service en lui passant les filtres achat, vente et l’utilisateur
        List<Enchere> enchereList = enchereService.filtrerEncheres(
                encheresAchat,
                encheresVente,
                pseudoUtilisateur,
                recherche,
                idCategorie
        );

        //Ajout des catégories
        List<Categorie> categories = daoArticleVendu.selectAll().stream()
                .map(ArticleVendu::getCategorie)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("encheres", enchereList);
        model.addAttribute("categories", categories);
        model.addAttribute("username", pseudoUtilisateur != null ? pseudoUtilisateur : "Anonyme");

        return "listeEncheres";
    }
}
