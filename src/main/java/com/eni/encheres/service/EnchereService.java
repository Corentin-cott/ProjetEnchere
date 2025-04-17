package com.eni.encheres.service;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Enchere;
import com.eni.encheres.dao.IDAOEnchere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnchereService {

    @Autowired
    IDAOEnchere daoEnchere;

    public List<Enchere> filtrerEncheres(List<String> filtresAchat, List<String> filtresVente,
                                         String pseudoConnecte, String recherche, Long idCategorie) {
        List<Enchere> toutes = daoEnchere.findAll();
        System.out.println(">> Nb d’enchères récupérées : " + toutes.size());

        LocalDateTime maintenant = LocalDateTime.now();

        // Si aucun filtre, retourne tout
        if ((filtresAchat == null || filtresAchat.isEmpty()) &&
                (filtresVente == null || filtresVente.isEmpty()) &&
                (recherche == null || recherche.isEmpty())) {
            return toutes;
        }

        return toutes.stream()
                .filter(e -> {
                    ArticleVendu article = e.getArticle();

                    // Filtrage par recherche texte (sur le nom)
                    boolean matchTexte = (recherche == null || recherche.isEmpty()) ||
                            article.getNom().toLowerCase().contains(recherche.toLowerCase());

                    // Filtrage achat
                    boolean matchAchat = (filtresAchat == null || filtresAchat.isEmpty()) || filtresAchat.stream().anyMatch(filtre -> {
                        switch (filtre) {
                            case "ouverts":
                                return article.getDateDebutEncheres().isBefore(maintenant);
                            case "enCours":
                                return isEnCours(article, maintenant);
                            case "terminees":
                                return article.getDateFinEncheres().isBefore(maintenant);
                            default:
                                return false;
                        }
                    });

                    // Filtrage vente
                    boolean matchVente = true;
                    if (filtresVente != null && !filtresVente.isEmpty()) {
                        matchVente = pseudoConnecte != null &&
                                article.getVendeur() != null &&
                                pseudoConnecte.equals(article.getVendeur().getPseudo()) &&
                                filtresVente.stream().anyMatch(filtre -> {
                                    switch (filtre) {
                                        case "fermees":
                                            return article.getDateDebutEncheres().isAfter(maintenant);
                                        case "enCours":
                                            return isEnCours(article, maintenant);
                                        case "terminees":
                                            return article.getDateFinEncheres().isBefore(maintenant);
                                        default:
                                            return false;
                                    }
                                });
                    }

                    // Catégorie
                    boolean matchCategorie = (idCategorie == null) ||
                            (article.getCategorie() != null && idCategorie.equals((long) article.getCategorie().getId()));


                    return matchTexte && matchAchat && matchVente && matchCategorie;
                })
                .collect(Collectors.toList());
    }

    private boolean isEnCours(ArticleVendu article, LocalDateTime maintenant) {
        return article.getDateDebutEncheres().isBefore(maintenant)
                && article.getDateFinEncheres().isAfter(maintenant);
    }

    public List<Enchere> getEncheresParArticle(long idArticle) {
        return daoEnchere.findByArticleId(idArticle);
    }

    public ServiceResponse<String> ajouterEnchere(Enchere enchere) {
        daoEnchere.ajouterEnchere(enchere);
        return ServiceResponse.buildResponse("00", "Enchère enregistrée", null);
    }
}
