package com.eni.encheres.service;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Enchere;
import com.eni.encheres.dao.IDAOEnchere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EnchereService {

    @Autowired
    IDAOEnchere daoEnchere;

    public List<Enchere> filtrerEncheres(List<String> filtresAchat, List<String> filtresVente, String pseudoConnecte) {
        List<Enchere> toutes = daoEnchere.findAll();
        LocalDateTime maintenant = LocalDateTime.now();

        Set<Enchere> resultats = new HashSet<>();

        if (filtresAchat != null && !filtresAchat.isEmpty()) {
            toutes.stream()
                    .filter(e -> {
                        ArticleVendu article = e.getArticle();
                        return filtresAchat.stream().anyMatch(filtre -> {
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
                    })
                    .forEach(resultats::add);
        }

        if (filtresVente != null && !filtresVente.isEmpty() && pseudoConnecte != null) {
            System.out.println("Filtrage des enchères pour l'utilisateur : " + pseudoConnecte);
            System.out.println("Filtres de vente reçus : " + filtresVente);

            toutes.stream()
                    .filter(e -> {
                        ArticleVendu article = e.getArticle();
                        System.out.println("Vérification de l'article : " + article.getNom());
                        return article.getVendeur() != null &&
                                article.getVendeur().getPseudo() != null &&
                                article.getVendeur().getPseudo().equals(pseudoConnecte) &&
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
                    })
                    .forEach(resultats::add);
        }

        // Si aucun filtre, retourner tout
        if ((filtresAchat == null || filtresAchat.isEmpty()) && (filtresVente == null || filtresVente.isEmpty())) {
            return toutes;
        }

        return new ArrayList<>(resultats);
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