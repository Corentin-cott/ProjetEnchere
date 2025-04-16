package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.Utilisateur;
import com.eni.encheres.dao.IDAOUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;

@Controller
@RequestMapping("/profil")
public class UtilisateurController {

    @Autowired
    private IDAOUtilisateur utilisateurDao;

    @GetMapping
    public String getUtilisateur(Model model) {
        // je mets dans mon modèle la liste des membres
        model.addAttribute("utilisateurs", utilisateurDao.getUtilisateurs());

        // je mets dans mon modèle un membre qui sera rempli par mon formulaire
        model.addAttribute("utilisateur", new Utilisateur());

        // je redirige sur le template "membres.html"
        return "profil";
    }

    @PostMapping
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

        utilisateurDao.addUtilisateur(utilisateur);
        return "connection";
    }

    @PostMapping("/delete")
    public String deleteUtilisateur(int idToDelete) {
        utilisateurDao.deleteUtilisateurById(idToDelete);
        return "profil";
    }
}
