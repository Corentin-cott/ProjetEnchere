package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.*;
import com.eni.encheres.dao.*;
import com.eni.encheres.security.UtilisateurSpringSecurity;
import com.eni.encheres.service.CategorieService;
import java.nio.file.Path;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.eni.encheres.service.ArticleVenduService;
import com.eni.encheres.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticleVenduController {

    @Autowired
    ArticleVenduService articleVenduService;
    @Autowired
    IDAOArticleVendu articleVenduDAO;
    @Autowired
    IDAOUtilisateur utilisateurIDAO;
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

    @PostMapping("/nouvelleVente")
    public String addArticleVendu(
            RedirectAttributes redirectAttributes,
            @RequestParam("utilisateur_name") String pseudo,
            @RequestParam("nom_article") String nom,
            @RequestParam("description_article") String description,
            @RequestParam("categorie") long id,
            @RequestParam("prix_article") double miseAPrix,
            @RequestParam("dateDebutEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("dateFinEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam("retrait_article") String retraitString,
            @RequestParam("photo_article") MultipartFile photo,
            Model model
    ) {
        Categorie categorie = categorieDAO.trouveParId(id);
        Utilisateur vendeur = utilisateurIDAO.getUtilisateurByPseudo(pseudo);

        ArticleVendu article = new ArticleVendu(nom,description,categorie,miseAPrix,null,dateDebut.atStartOfDay(),dateFin.atStartOfDay(),vendeur);
        articleVenduDAO.addArticleVendu(article);

        String message = "Succès ! Votre article à été mise en vente dans la liste des enchères ! ";

        if (!photo.isEmpty()) {
            try {
                Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads", "articles");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                String fileName = vendeur.getId().toString() + "-" + article.getId().toString() + ".png";

                Path filePath = uploadDir.resolve(fileName);
                Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                article.setPhotoPath("/imgs/Articles/" + fileName);
                articleVenduDAO.updateArticle(article);
            } catch (IOException e) {
                e.printStackTrace();
                message = message + "Mais l'image de votre article à rencontrer une erreur.";
            }
        }

        redirectAttributes.addFlashAttribute("success_creation", true);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/details/"+article.getId().toString();
    }

    @GetMapping
    public String accueil(Model model,@RequestParam(required = false) List<String> filtresAchat,
                          @RequestParam(required = false) List<String> filtresVente,
                          @RequestParam(required = false) String recherche,
                          @RequestParam(required = false) Long idCategorie) {

        List<ArticleVendu> articles = articleVenduDAO.selectAll();

        // Récupération des catégories via daoCategorie
        List<Categorie> categories = categorieDAO.trouveTout();

        model.addAttribute("categories", categories);
        model.addAttribute("articles", articles);
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
        List<ArticleVendu> articles = articleVenduService.filtrerEncheres(
                filtresAchat, filtresVente, pseudoConnecte, recherche, idCategorie
        );

        // Récupération des catégories via daoCategorie
        List<Categorie> categories = categorieDAO.trouveTout();

        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        model.addAttribute("filtresAchat", filtresAchat);
        model.addAttribute("filtresVente", filtresVente);
        model.addAttribute("recherche", recherche);
        model.addAttribute("idCategorie", idCategorie);

        return "listeEncheres";
    }

    @GetMapping("/details/{id}")public String detailsVente(
            @PathVariable int id, Model model
    ){
        ArticleVendu article = articleVenduDAO.selectById(id);
        if (article == null) {
            model.addAttribute("errorNo", "404");
            model.addAttribute("error", "Article non trouvée");
            return "errors/error";
        }
        model.addAttribute("article", article);

        return "detailsVente";
    }

    @PostMapping("/details/{id}/mise")public String nouvellemise(@PathVariable int id, RedirectAttributes redirectAttributes, @RequestParam double nouvelleoffre){
        UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilisateur utilisateurConnecte = userDetails.getUtilisateur();
        ArticleVendu article = articleVenduDAO.selectById(id);
        article.setAcheteur(utilisateurConnecte);
        article.setPrixVente(nouvelleoffre);
        articleVenduDAO.updateArticle(article);
        redirectAttributes.addFlashAttribute("successmise", true);
        return "redirect:/details/"+id;
    }

    @PostMapping("/details/{id}/delete")public String deleteArticle(@PathVariable int id,RedirectAttributes redirectAttributes){
        articleVenduDAO.deleteArticleById(id);
        redirectAttributes.addFlashAttribute("successdeletearticle", true);
        return "redirect:/";
    }

}