package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.*;
import com.eni.encheres.dao.DAOCategorieMock;
import com.eni.encheres.dao.IDAOArticleVendu;
import com.eni.encheres.dao.IDAOEnchere;
import com.eni.encheres.dao.IDAOUtilisateur;
import com.eni.encheres.service.ArticleVenduService;
import com.eni.encheres.service.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class EnchereController {

    @Autowired
    private EnchereService enchereService;

    @Autowired
    private ArticleVenduService articleVenduService;

    @Autowired
    private IDAOArticleVendu daoArticleVendu;

    @Autowired
    IDAOArticleVendu articleVenduDAO;

    @Autowired
    IDAOEnchere enchereIDAO;

    @Autowired
    IDAOUtilisateur utilisateurIDAO;

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
