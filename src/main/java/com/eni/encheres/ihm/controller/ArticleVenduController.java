package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.*;
import com.eni.encheres.dao.*;
import com.eni.encheres.security.UtilisateurSpringSecurity;
import com.eni.encheres.service.CategorieService;
import jdk.jshell.execution.Util;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.eni.encheres.service.ArticleVenduService;
import com.eni.encheres.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ArticleVenduController {

    @Autowired
    ArticleVenduService articleVenduService;
    @Autowired
    private CategorieService categorieService;
    @Autowired
    private IDAOCategorie categorieDAO;

    @GetMapping("/liste-articles")
    public String articleVendu(Model model) {
        // Appelle le métier
        ServiceResponse serviceResponse = articleVenduService.getAll();

        // Le model envoie des données dans le front/requetes http/html
        model.addAttribute("articlesVendus", serviceResponse.data);

        // Affiche le front
        return "listeArticles.html";
    }

    @GetMapping("/nouvelleVente")
    public String nouvelleVente(Model model) {
        // Liste les catégories
        List<Categorie> categories = categorieService.getAll();
        // Récupère l'utilisateur
        UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilisateur utilisateurConnecte = userDetails.getUtilisateur();
        String adresse = utilisateurConnecte.getRue() + ", " + utilisateurConnecte.getCodePostal() + " " + utilisateurConnecte.getVille();

        model.addAttribute("categories", categories);
        model.addAttribute("utilisateurConnecte", utilisateurConnecte);
        model.addAttribute("adresse", adresse);
        return "nouvelleVente.html";
    }

}