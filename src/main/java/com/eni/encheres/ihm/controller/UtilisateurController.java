package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.Utilisateur;
import com.eni.encheres.dao.IDAOUtilisateur;
import com.eni.encheres.security.UtilisateurSpringSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profil")
public class UtilisateurController {

    @Autowired
    private IDAOUtilisateur utilisateurDao;

    @GetMapping
    public String getUtilisateur() {

        UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilisateur utilisateurConnecte = userDetails.getUtilisateur();


        long id = utilisateurConnecte.getId();

        return "redirect:/profil/" + id;
    }

    @GetMapping("/{id}")
    public String afficherProfilUtilisateur(@PathVariable int id, Model model) {
        UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilisateur utilisateurConnecte = userDetails.getUtilisateur();
        if(id!=utilisateurConnecte.getId()) {
            return "redirect:/profil/" + utilisateurConnecte.getId();
        }
        Utilisateur utilisateur = utilisateurDao.getUtilisateurById(id); // ou autre méthode
        model.addAttribute("utilisateur", utilisateur);
        return "profil"; // vue dédiée au profil utilisateur
    }


    @GetMapping("/nouveau")
    public String créerUtilisateur(Model model) {

        // je mets dans mon modèle un membre qui sera rempli par mon formulaire
        model.addAttribute("utilisateur", new Utilisateur());

        // je redirige sur le template "membres.html"
        return "profil";
    }

    @PostMapping("/nouveau")
    public String addUtilisateur(@RequestParam String motDePasse,
                                 @RequestParam String confirmationMotDePasse,
                                 @ModelAttribute Utilisateur utilisateur,
                                 Model model) {
        if (!motDePasse.equals(confirmationMotDePasse)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            return "profil";
        }
        if (utilisateurDao.getUtilisateurs().stream().anyMatch(u -> u.getPseudo().equals(utilisateur.getPseudo()))) {
            model.addAttribute("error", "Ce pseudo est déjà utilisé.");
            return "profil";
        }
        if (utilisateurDao.getUtilisateurs().stream().anyMatch(u -> u.getEmail().equals(utilisateur.getEmail()))) {
            model.addAttribute("error", "Cet email est déjà utilisé.");
            return "profil";
        }
        model.addAttribute("success", true);
        utilisateurDao.addUtilisateur(utilisateur);
        return "connection";
    }

    @PostMapping("/{id}")
    public String modifierUtilisateur(@RequestParam String motDePasse,
                                 @RequestParam String confirmationMotDePasse,
                                 @ModelAttribute Utilisateur utilisateur,
                                 Model model,@PathVariable int id) {
        if (!motDePasse.equals(confirmationMotDePasse)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            return "redirect:/profil/\"+id";
        }
        if (utilisateurDao.getUtilisateurs().stream().anyMatch(u -> u.getPseudo().equals(utilisateur.getPseudo())&&u.getId()!=id)) {
            model.addAttribute("error", "Ce pseudo est déjà utilisé.");
            return "redirect:/profil/\"+id";
        }
        if (utilisateurDao.getUtilisateurs().stream().anyMatch(u -> u.getEmail().equals(utilisateur.getEmail())&&u.getId()!=id)) {
            model.addAttribute("error", "Cet email est déjà utilisé.");
            return "redirect:/profil/\"+id";
        }
        model.addAttribute("success", true);
        utilisateurDao.updateUtilisateur(utilisateur);
        return "profil";
    }

    @PostMapping("/delete")
    public String deleteUtilisateur(@RequestParam int idToDelete, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        utilisateurDao.deleteUtilisateurById(idToDelete);
        request.getSession().invalidate(); // sécurité
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/"; // vers ton endpoint perso
    }
}
