package com.eni.encheres.service;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Categorie;
import com.eni.encheres.dao.IDAOArticleVendu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleVenduService {

    @Autowired
    @Qualifier("DAOArticleVenduJpa")
    IDAOArticleVendu daoArticleVendu;

    public ServiceResponse<List<ArticleVendu>> getAll() {
        List<ArticleVendu> articleVendus = daoArticleVendu.selectAll();

        if (articleVendus.isEmpty()){

            // erreur 1 : Liste Vide
            return ServiceResponse.buildResponse ("2", "Liste vide", articleVendus);
        }

        // erreur 2 : Liste OK
        return ServiceResponse.buildResponse ("2", "Liste des articles a été récupérée avec succès", articleVendus);
    }

    /*
            METHODES ANCIENNEMENT DANS EnchèreService
     */

    public List<ArticleVendu> filtrerEncheres(List<String> filtresAchat, List<String> filtresVente,
                                              String pseudoConnecte, String recherche, Long idCategorie) {

        List<ArticleVendu> toutes = daoArticleVendu.selectAll();

        LocalDateTime maintenant = LocalDateTime.now();

        return toutes.stream()
                .filter(e -> filtreTexte(e, recherche))
                .filter(e -> filtreAchat(e, filtresAchat, pseudoConnecte, maintenant))
                .filter(e -> filtreVente(e, filtresVente, pseudoConnecte, maintenant))
                .filter(e -> filtreCategorie(e, idCategorie))
                .collect(Collectors.toList());
    }

    private boolean filtreTexte(ArticleVendu a, String recherche) {
        if (recherche == null || recherche.isEmpty()) return true;
        return a.getNom().toLowerCase().contains(recherche.toLowerCase());
    }

    private boolean filtreAchat(ArticleVendu a, List<String> filtresAchat, String pseudoConnecte, LocalDateTime maintenant) {
        if (filtresAchat == null || filtresAchat.isEmpty()) return true;

        return filtresAchat.stream().anyMatch(filtre -> {
            switch (filtre) {
                case "EncheresOuvertes":
                    return a.getDateDebutEncheres().isBefore(maintenant)
                            && a.getDateFinEncheres().isAfter(maintenant);

                case "MesEncheresEnCours":
                    return a.getAcheteur() != null
                            && a.getAcheteur().getPseudo().equals(pseudoConnecte)
                            && a.getDateFinEncheres().isAfter(maintenant);

                case "MesEncheresRemportees":
                    return a.getAcheteur() != null
                            && a.getAcheteur().getPseudo().equals(pseudoConnecte)
                            && a.getDateFinEncheres().isBefore(maintenant);

                default:
                    return false;
            }
        });
    }

    private boolean filtreVente(ArticleVendu a, List<String> filtresVente, String pseudoConnecte, LocalDateTime maintenant) {
        if (filtresVente == null || filtresVente.isEmpty()) return true;

        if (pseudoConnecte == null || a.getVendeur() == null ||
                !pseudoConnecte.equals(a.getVendeur().getPseudo())) return false;

        return filtresVente.stream().anyMatch(filtre -> {
            switch (filtre) {
                case "VentesNonDebutees":
                    return a.getDateDebutEncheres().isAfter(maintenant);
                case "VentesEnCours":
                    return isEnCours(a, maintenant);
                case "VentesTerminees":
                    return a.getDateFinEncheres().isBefore(maintenant);
                default:
                    return false;
            }
        });
    }

    private boolean filtreCategorie(ArticleVendu a, Long idCategorie) {
        if (idCategorie == null) return true;
        Categorie categorie = a.getCategorie();
        return idCategorie.equals((long) categorie.getId());
    }

    private boolean isEnCours(ArticleVendu article, LocalDateTime maintenant) {
        return article.getDateDebutEncheres().isBefore(maintenant)
                && article.getDateFinEncheres().isAfter(maintenant);
    }

}