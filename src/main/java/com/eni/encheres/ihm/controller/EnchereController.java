package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Categorie;
import com.eni.encheres.bo.Enchere;
import com.eni.encheres.dao.IDAOArticleVendu;
import com.eni.encheres.dao.IDAOCategorie;
import com.eni.encheres.service.EnchereService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestMapping("/")
@Controller
public class EnchereController {

    @Autowired
    private EnchereService enchereService;

    @Autowired
    private IDAOCategorie daoCategorie;

    @Autowired
    private IDAOArticleVendu daoArticleVendu;

    @GetMapping
    public String accueil(Model model,@RequestParam(required = false) List<String> filtresAchat,
                          @RequestParam(required = false) List<String> filtresVente,
                          @RequestParam(required = false) String recherche,
                          @RequestParam(required = false) Long idCategorie) {

       List<Enchere> encheres = enchereService.getEncheres();

        List<ArticleVendu> articles=new ArrayList<>();
        encheres.forEach(enchere -> {articles.add(daoArticleVendu.selectById(enchere.getIdArticle()));});

        // Récupération des catégories via daoCategorie
        List<Categorie> categories = daoCategorie.trouveTout();

        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        model.addAttribute("filtresAchat", filtresAchat);
        model.addAttribute("filtresVente", filtresVente);
        model.addAttribute("recherche", recherche);
        model.addAttribute("idCategorie", idCategorie);

        return "listeEncheres";
    }

    @GetMapping("/encheres/filtre")
    public String afficherEncheres(Model model,
                                   @RequestParam(required = false) List<String> filtresAchat,
                                   @RequestParam(required = false) List<String> filtresVente,
                                   @RequestParam(required = false) String recherche,
                                   @RequestParam(required = false) Long idCategorie,
                                   @SessionAttribute(name = "pseudo", required = false) String pseudoConnecte) {

        // Appel au service filtré
        List<Enchere> encheres = enchereService.filtrerEncheres(
                filtresAchat, filtresVente, pseudoConnecte, recherche, idCategorie);

        List<ArticleVendu> articles=new ArrayList<>();
        encheres.forEach(enchere -> {articles.add(daoArticleVendu.selectById(enchere.getIdArticle()));});

        // Récupération des catégories via daoCategorie
        List<Categorie> categories = daoCategorie.trouveTout();

        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        model.addAttribute("filtresAchat", filtresAchat);
        model.addAttribute("filtresVente", filtresVente);
        model.addAttribute("recherche", recherche);
        model.addAttribute("idCategorie", idCategorie);

        return "listeEncheres";
    }
}

