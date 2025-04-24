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
    public String AjoutOuUpdateArticleVendu(
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "id_article", required = false) Long idArticle,
            @RequestParam("utilisateur_name") String pseudo,
            @RequestParam("nom_article") String nom,
            @RequestParam("description_article") String description,
            @RequestParam("categorie") long idCategorie,
            @RequestParam("prix_article") double miseAPrix,
            @RequestParam("dateDebutEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("dateFinEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam("retrait_article") String retraitString,
            @RequestParam("photo_article") MultipartFile photo,
            Model model
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UtilisateurSpringSecurity) {
            UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) principal;
            Utilisateur utilisateurConnecte = userDetails.getUtilisateur();
            Utilisateur utilisateur = utilisateurIDAO.getUtilisateurById(utilisateurConnecte.getId());
            model.addAttribute("utilisateurConnecte", utilisateur);
        } else {
            String username = principal.toString();
            System.out.println("Utilisateur non authentifié avec UserDetails : " + username);
        }

        Utilisateur vendeur = utilisateurIDAO.getUtilisateurByPseudo(pseudo);
        Categorie categorie = categorieDAO.trouveParId(idCategorie);

        ArticleVendu article;

        if (idArticle != null && idArticle != 0) {
            // 🔁 Modification
            article = articleVenduDAO.selectById(idArticle);
            article.setNom(nom);
            article.setDescription(description);
            article.setCategorie(categorie);
            article.setMiseAPrix(miseAPrix);
            article.setDateDebutEncheres(dateDebut.atStartOfDay());
            article.setDateFinEncheres(dateFin.atStartOfDay());
            articleVenduDAO.updateArticle(article);
        } else {
            // ➕ Nouvelle création
            article = new ArticleVendu(nom, description, categorie, miseAPrix, null,
                    dateDebut.atStartOfDay(), dateFin.atStartOfDay(), vendeur);
            articleVenduDAO.addArticleVendu(article);
        }

        // 📷 Gestion de l’image
        if (!photo.isEmpty()) {
            try {
                Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads", "articles");
                if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

                String fileName = vendeur.getId() + "-" + article.getId() + ".png";
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                article.setPhotoPath("/imgs/Articles/" + fileName);
                articleVenduDAO.updateArticle(article);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        redirectAttributes.addFlashAttribute("success_creation", true);
        redirectAttributes.addFlashAttribute("message", "Article enregistré avec succès !");
        return "redirect:/details/" + article.getId();
    }

    @PostMapping("/details/{id}/modifier")
    public String modifierEnchere(@PathVariable Long id, Model model) {
        // Récupérer l’article avec l’ID
        ArticleVendu article = articleVenduDAO.selectById(id);
        model.addAttribute("article", article);

        // Ajouter les champs nécessaires pour pré-remplir le formulaire
        model.addAttribute("adresse", article.getVendeur().getRue() + ", " +
                article.getVendeur().getCodePostal() + " " +
                article.getVendeur().getVille());

        model.addAttribute("categories", categorieDAO.trouveTout());

        return "nouvelleVente"; // vue avec le formulaire pré-rempli
    }

    @GetMapping("/details/{id}/modifier")
    public String afficherFormulaireModification(@PathVariable Long id, Model model) {
        ArticleVendu article = articleVenduDAO.selectById(id);
        model.addAttribute("article", article);
        model.addAttribute("categories", categorieDAO.trouveTout());

        String adresse = article.getVendeur().getRue() + ", " +
                article.getVendeur().getCodePostal() + " " +
                article.getVendeur().getVille();
        model.addAttribute("adresse", adresse);

        return "nouvelleVente"; // le nom de ta page .html
    }

    @GetMapping
    public String accueil(Model model,@RequestParam(required = false) List<String> filtresAchat,
                          @RequestParam(required = false) List<String> filtresVente,
                          @RequestParam(required = false) String recherche,
                          @RequestParam(required = false) Long idCategorie)
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UtilisateurSpringSecurity) {
            UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) principal;
            Utilisateur utilisateurConnecte = userDetails.getUtilisateur();
            Utilisateur utilisateur = utilisateurIDAO.getUtilisateurById(utilisateurConnecte.getId());
            model.addAttribute("utilisateurConnecte", utilisateur);
        } else {
            String username = principal.toString();
            System.out.println("Utilisateur non authentifié avec UserDetails : " + username);
        }

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
                                   @RequestParam(required = false) Long idCategorie) {

        // Appel au service filtré
        List<ArticleVendu> articles = articleVenduService.filtrerEncheres(
                filtresAchat, filtresVente, recherche, idCategorie
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UtilisateurSpringSecurity) {
            UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) principal;
            Utilisateur utilisateurConnecte = userDetails.getUtilisateur();
            Utilisateur utilisateur = utilisateurIDAO.getUtilisateurById(utilisateurConnecte.getId());
            model.addAttribute("utilisateurConnecte", utilisateur);
        } else {
            String username = principal.toString();
            System.out.println("Utilisateur non authentifié avec UserDetails : " + username);
        }
        
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
        Utilisateur utilisateur = utilisateurIDAO.getUtilisateurById(utilisateurConnecte.getId());

        if(nouvelleoffre>utilisateur.getCredit()){
            redirectAttributes.addFlashAttribute("error", true);
            return "redirect:/details/"+id;
        }
        else {
            utilisateur.setCredit(utilisateur.getCredit()-nouvelleoffre);

            ArticleVendu article = articleVenduDAO.selectById(id);

            if(article.getAcheteur()!=null) {
                if(utilisateur.getId()==article.getAcheteur().getId()){
                    utilisateur.setCredit(utilisateur.getCredit() + article.getPrixVente());
                }
                else{
                    article.getAcheteur().setCredit(article.getAcheteur().getCredit() + article.getPrixVente());
                    utilisateurIDAO.updateUtilisateur(article.getAcheteur());
                }
            }

            article.setAcheteur(utilisateur);
            article.setPrixVente(nouvelleoffre);

            utilisateurIDAO.updateUtilisateur(utilisateur);
            articleVenduDAO.updateArticle(article);

            redirectAttributes.addFlashAttribute("successmise", true);

            return "redirect:/details/" + id;
        }
    }

    @PostMapping("/details/{id}/delete")public String deleteArticle(@PathVariable int id,RedirectAttributes redirectAttributes){
        articleVenduDAO.deleteArticleById(id);
        redirectAttributes.addFlashAttribute("successdeletearticle", true);
        return "redirect:/";
    }

}