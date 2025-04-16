package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Categorie;
import com.eni.encheres.bo.Enchere;
import com.eni.encheres.bo.Utilisateur;
import com.eni.encheres.dao.DAOCategorieMock;
import com.eni.encheres.dao.IDAOArticleVendu;
import com.eni.encheres.dao.IDAOEnchere;
import com.eni.encheres.dao.IDAOUtilisateur;
import org.springframework.format.annotation.DateTimeFormat;
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

@Controller
public class ArticleVenduController {

    @Autowired
    ArticleVenduService articleVenduService;
    @Autowired
    IDAOArticleVendu articleVenduDAO;
    @Autowired
    IDAOEnchere enchereIDAO;
    @Autowired
    IDAOUtilisateur utilisateurIDAO;

    @GetMapping("/liste-articles")
    public String articleVendu(Model model) {
        // Appelle le métier
        ServiceResponse serviceResponse = articleVenduService.getAll();

        // Le model envoie des données dans le front/requetes http/html
        model.addAttribute("articlesVendus", serviceResponse.data);

        // Affiche le front
        return "listeArticles.html";
    }

    @PostMapping("/nouvelleVente")
    public String addArticleVendu(
            @RequestParam("utilisateur_name") String pseudo,
            @RequestParam("nom_article") String nom,
            @RequestParam("description_article") String description,
            @RequestParam("categorie") String libelleCategorie,
            @RequestParam("prix_article") double miseAPrix,
            @RequestParam("date_deb_enchere") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("date_fin_enchere") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            Model model
    ) {
        Categorie categorie = DAOCategorieMock.trouveParLibelleMock(libelleCategorie);
        Utilisateur vendeur = utilisateurIDAO.getUtilisateurByPseudo(pseudo);

        ArticleVendu article = new ArticleVendu();
        article.setNom(nom);
        article.setDescription(description);
        article.setCategorie(categorie);
        article.setMiseAPrix(miseAPrix);
        article.setPrixVente(miseAPrix);
        article.setDateDebutEncheres(dateDebut.atStartOfDay());
        article.setDateFinEncheres(dateFin.atStartOfDay());
        article.setVendeur(vendeur);

        Enchere enchere = new Enchere();
        enchere.setArticle(article);
        enchere.setEncherisseur(vendeur);
        enchereIDAO.ajouterEnchere(enchere);

        articleVenduDAO.addArticleVendu(article);

        return "listeEncheres";
    }

}