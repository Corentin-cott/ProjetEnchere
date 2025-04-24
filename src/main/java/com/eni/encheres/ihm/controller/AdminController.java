package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.Utilisateur;
import com.eni.encheres.dao.IDAOArticleVendu;
import com.eni.encheres.dao.IDAOUtilisateur;
import com.eni.encheres.security.UtilisateurSpringSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IDAOUtilisateur utilisateurDao;
    @Autowired
    IDAOArticleVendu articleVenduDAO;

    @GetMapping()
    public String listeUtilisateurs(Model model) {
        List<Utilisateur> utilisateurs =utilisateurDao.getUtilisateurs();
        model.addAttribute("utilisateurs", utilisateurs);

        return "admin";
    }

    @PostMapping("/delete")
    public String deleteUtilisateur(@RequestParam int idToDelete, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        UtilisateurSpringSecurity userDetails = (UtilisateurSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utilisateur utilisateurConnecte = userDetails.getUtilisateur();
        utilisateurDao.deleteUtilisateurById(idToDelete);
        if(utilisateurConnecte.getId() == idToDelete) {
            request.getSession().invalidate();
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/admin";
    }

    @PostMapping("/disable")
    public String desactiveUtilisateur(@RequestParam int idToDisable, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        utilisateurDao.disableUtilisateur(idToDisable);

        redirectAttributes.addFlashAttribute("success_desactive", true);
        return "redirect:/admin";
    }
}
