package com.eni.encheres.controller;

import com.eni.encheres.bo.Utilisateur;
import com.eni.encheres.dao.IDAOUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String addMembre(Utilisateur utilisateur) {
        utilisateurDao.addUtilisateur(utilisateur);
        return "profil";
    }

    @PostMapping("/delete")
    public String deleteUtilisateur(int idToDelete) {
        utilisateurDao.deleteUtilisateurById(idToDelete);
        return "profil";
    }
}
