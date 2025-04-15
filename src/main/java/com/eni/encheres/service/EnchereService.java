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

    public List<Enchere> getEncheresEnCours() {
        List<Enchere> toutes = daoEnchere.findAll(); // tu dois avoir ou ajouter cette méthode dans le DAO

        LocalDateTime maintenant = LocalDateTime.now();

        return toutes.stream()
                .filter(e -> {
                    ArticleVendu article = e.getArticle();
                    return article.getDateDebutEncheres().isBefore(maintenant)
                            && article.getDateFinEncheres().isAfter(maintenant);
                })
                .collect(Collectors.toList());
    }


    public ServiceResponse<String> ajouterEnchere(Enchere enchere) {
        // logiquement ici tu peux vérifier que le montant est supérieur à la mise actuelle
        daoEnchere.ajouterEnchere(enchere);
        return ServiceResponse.buildResponse("00", "Enchère enregistrée", null);
    }

    public List<Enchere> getEncheresParArticle(long idArticle) {
        return daoEnchere.findByArticleId(idArticle);
    }
}
