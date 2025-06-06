package com.eni.encheres.ihm.controller;

import com.eni.encheres.bo.Utilisateur;
import com.eni.encheres.dao.IDAOUtilisateur;
import com.eni.encheres.security.UtilisateurSpringSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/profil")
public class UtilisateurController {

    @Autowired
    private IDAOUtilisateur utilisateurDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;


    @GetMapping()
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

        if(id!=utilisateurConnecte.getId()&&!utilisateurConnecte.isAdmin()) {

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
            String errorMessage = messageSource.getMessage("profil_password_different",null,LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMessage);
            return "profil";
        }
        if (utilisateurDao.getUtilisateurs().stream().anyMatch(u -> u.getPseudo().equals(utilisateur.getPseudo()))) {
            String errorMessage = messageSource.getMessage("profil_pseudo_pasunique",null,LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMessage);
            return "profil";
        }
        if (utilisateurDao.getUtilisateurs().stream().anyMatch(u -> u.getEmail().equals(utilisateur.getEmail()))) {
            String errorMessage = messageSource.getMessage("profil_email_pasunique",null,LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMessage);
            return "profil";
        }
        model.addAttribute("success", true);
        utilisateurDao.addUtilisateur(utilisateur);
        return "connection";
    }

    @PostMapping("/{id}")
    public String modifierUtilisateur(@RequestParam String motDePasse,
                                 @RequestParam String confirmationMotDePasse,
                                 @ModelAttribute Utilisateur utilisateurForm,
                                 Model model,@PathVariable int id) {

        Utilisateur utilisateurOriginal = utilisateurDao.getUtilisateurById(id);

        if (!motDePasse.isBlank()) {
            if (!motDePasse.equals(confirmationMotDePasse)) {
                String errorMessage = messageSource.getMessage("profil_password_different",null,LocaleContextHolder.getLocale());
                model.addAttribute("error", errorMessage);
                return "redirect:/profil/\"+id";
            }
            utilisateurOriginal.setMotDePasse(motDePasse);
        }
        if (utilisateurDao.getUtilisateurs().stream().anyMatch(u -> u.getPseudo().equals(utilisateurForm.getPseudo())&&u.getId()!=id)) {
            String errorMessage = messageSource.getMessage("profil_pseudo_pasunique",null,LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMessage);
            return "redirect:/profil/\"+id";
        }
        if (utilisateurDao.getUtilisateurs().stream().anyMatch(u -> u.getEmail().equals(utilisateurForm.getEmail())&&u.getId()!=id)) {
            String errorMessage = messageSource.getMessage("profil_email_pasunique",null,LocaleContextHolder.getLocale());
            model.addAttribute("error", errorMessage);
            return "redirect:/profil/\"+id";
        }

        utilisateurOriginal.setPseudo(utilisateurForm.getPseudo());
        utilisateurOriginal.setNom(utilisateurForm.getNom());
        utilisateurOriginal.setPrenom(utilisateurForm.getPrenom());
        utilisateurOriginal.setEmail(utilisateurForm.getEmail());
        utilisateurOriginal.setTelephone(utilisateurForm.getTelephone());
        utilisateurOriginal.setRue(utilisateurForm.getRue());
        utilisateurOriginal.setCodePostal(utilisateurForm.getCodePostal());
        utilisateurOriginal.setVille(utilisateurForm.getVille());


        utilisateurDao.updateUtilisateur(utilisateurOriginal);
        model.addAttribute("success", true);
        return "profil";
    }


    @PostMapping("/delete")
    public String deleteUtilisateur(@RequestParam int idToDelete, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        utilisateurDao.deleteUtilisateurById(idToDelete);
        request.getSession().invalidate();
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/";
    }

    @GetMapping("/motdepasse/oubli")
    public String afficherFormulaireOubli() {
        return "motDePasseOublie";
    }

    @GetMapping("/motdepasse/reinitialisation/{token}")
    public String afficherFormulaireReinitialisation(@PathVariable String token, Model model) {
        Utilisateur utilisateur = utilisateurDao.getUtilisateurByToken(token);

        if (utilisateur == null) {
            return "redirect:/connection";
        }

        model.addAttribute("token", token);
        return "motDePasseReinitialisation";
    }

    @PostMapping("/motdepasse/reinitialisation")
    public String traiterDemandeReinitialisation(@RequestParam("email") String email, Model model) {
        // Vérifier si l'utilisateur existe avec l'email fourni
        Utilisateur utilisateur = utilisateurDao.getUtilisateurByEmail(email);

        if (utilisateur == null) {
            String errorMessage = messageSource.getMessage("newpassword_usernotfound",null,LocaleContextHolder.getLocale());
            model.addAttribute("message", errorMessage);
            return "motDePasseOublie";  // Affiche à nouveau le formulaire d'oubli
        }

        // Générer un token de réinitialisation pour l'utilisateur
        String token = UUID.randomUUID().toString(); // Exemple de génération de token
        utilisateur.setTokenReinitialisation(token);
        utilisateurDao.updateUtilisateur(utilisateur);  // Sauvegarder l'utilisateur avec le token

        // Envoyer un lien avec le token à l'utilisateur (pas montré ici, mais vous pouvez utiliser un email ou une page)
        String errorMessage = messageSource.getMessage("newpassword_linksend",null,LocaleContextHolder.getLocale());
        model.addAttribute("message", errorMessage);

        // Rediriger vers la page de réinitialisation avec le token dans l'URL
        return "redirect:/profil/motdepasse/reinitialisation/" + token;
    }

    @PostMapping("/motdepasse/reinitialisation/{token}")
    public String traiterReinitialisationMotDePasse(@PathVariable String token,
                                                    @RequestParam("motdepasse") String motdepasse,@RequestParam("confirmmotdepasse") String confirm,
                                                    Model model) {
        // Récupérer l'utilisateur à l'aide du token
        Utilisateur utilisateur = utilisateurDao.getUtilisateurByToken(token);

        if (utilisateur == null) {
            // Si l'utilisateur est introuvable, rediriger vers la page de connexion
            String errorMessage = messageSource.getMessage("newpassword_linkdead",null,LocaleContextHolder.getLocale());
            model.addAttribute("message", errorMessage);
            return "redirect:/connection";
        }
        if(!motdepasse.equals(confirm)) {
            return "motdepasseReinitialisation";
        }
        // Encoder le nouveau mot de passe avant de l'enregistrer
        utilisateur.setMotDePasse(passwordEncoder.encode(motdepasse));

        // Mettre à jour l'utilisateur dans la base de données
        utilisateurDao.updateUtilisateur(utilisateur);

        String errorMessage = messageSource.getMessage("newpassword_success",null,LocaleContextHolder.getLocale());
        model.addAttribute("message", errorMessage);
        return "redirect:/connection";
    }
}