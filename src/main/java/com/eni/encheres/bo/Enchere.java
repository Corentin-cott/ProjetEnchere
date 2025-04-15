package com.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enchere {
    private long noUtilisateur;
    private String dateEnchere;
    private double montantEnchere;
    private ArticleVendu article;
    private Utilisateur encherisseur;

    public Enchere(long noUtilisateur, String dateEnchere, double montantEnchere, ArticleVendu article) {
        this.noUtilisateur = noUtilisateur;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.article = article;
    }
}
