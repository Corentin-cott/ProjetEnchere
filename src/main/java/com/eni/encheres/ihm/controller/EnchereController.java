package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.*;
import com.eni.encheres.dao.DAOCategorieMock;
import com.eni.encheres.dao.IDAOArticleVendu;
import com.eni.encheres.dao.IDAOEnchere;
import com.eni.encheres.dao.IDAOUtilisateur;
import com.eni.encheres.service.ArticleVenduService;
import com.eni.encheres.dao.IDAOCategorie;
import com.eni.encheres.service.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.time.LocalDate;
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
    private ArticleVenduService articleVenduService;

    @Autowired
    private IDAOCategorie daoCategorie;

    @Autowired
    private IDAOArticleVendu daoArticleVendu;

    @Autowired
    IDAOArticleVendu articleVenduDAO;

    @Autowired
    IDAOEnchere enchereIDAO;

    @Autowired
    IDAOUtilisateur utilisateurIDAO;

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

    @GetMapping("/encheres/details/{id}")
    public String detailsVente(
            @PathVariable int id,
            Model model
    )
    {
        Enchere enchere = enchereService.getEncheresParId(id);
        if (enchere == null) {
            return "redirect:/error";
        }

        model.addAttribute("enchere", enchere);
        return "detailsVente";
    }

    @PostMapping("/encheres/details/nouvelleVente")
    public String addArticleVendu(
            @RequestParam("utilisateur_name") String pseudo,
            @RequestParam("nom_article") String nom,
            @RequestParam("description_article") String description,
            @RequestParam("categorie") String libelleCategorie,
            @RequestParam("prix_article") double miseAPrix,
            @RequestParam("date_deb_enchere") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("date_fin_enchere") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam("retrait_article") String retraitString,
            Model model
    ) {
        /* Vérification de si l'article éxiste déjà */
        ArticleVendu ifArticle = articleVenduService.getArticleVenduByName(nom);
        if (ifArticle != null) {
            model.addAttribute("errorNo", "506");
            model.addAttribute("error", "L'article éxiste déjà.");
            return "errors/error";
        }

        Categorie categorie = DAOCategorieMock.trouveParLibelleMock(libelleCategorie);
        Utilisateur vendeur = utilisateurIDAO.getUtilisateurByPseudo(pseudo);
        Retrait retrait = new Retrait(vendeur.getRue(), vendeur.getCodePostal(), vendeur.getVille());

        ArticleVendu article = new ArticleVendu(nom,description,categorie,miseAPrix,miseAPrix,dateDebut.atStartOfDay(),dateFin.atStartOfDay(),retrait,vendeur);

        Enchere enchere = new Enchere(article,vendeur);
        enchereIDAO.ajouterEnchere(enchere);

        articleVenduDAO.addArticleVendu(article);

        model.addAttribute("enchere", enchere);
        model.addAttribute("success", true);
        return "detailsVente";
    }
}

