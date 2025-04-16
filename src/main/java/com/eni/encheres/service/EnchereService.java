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

    public List<Enchere> filtrerEncheres(List<String> filtresAchat, List<String> filtresVente, String pseudoConnecte) {
        List<Enchere> toutes = daoEnchere.findAll();
        LocalDateTime maintenant = LocalDateTime.now();

        return toutes.stream()
                .filter(e -> {
                    ArticleVendu article = e.getArticle();

                    boolean matchAchat = filtresAchat == null || filtresAchat.isEmpty() || filtresAchat.stream().anyMatch(filtre -> {
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

                    boolean matchVente = (filtresVente == null || filtresVente.isEmpty())
                            || (pseudoConnecte != null
                            && article.getVendeur() != null
                            && article.getVendeur().getPseudo() != null
                            && article.getVendeur().getPseudo().equals(pseudoConnecte)
                            && filtresVente.stream().anyMatch(filtre -> {
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
                    }));

                    return matchAchat && matchVente;
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

