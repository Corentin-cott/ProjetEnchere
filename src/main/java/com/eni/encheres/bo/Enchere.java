package com.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enchere {
    private long noUtilisateur;
    private LocalDateTime dateEnchere;
    private double montantEnchere;
    private ArticleVendu article;
    private Utilisateur encherisseur;

    public Enchere(ArticleVendu article, Utilisateur encherisseur) {
        this.article = article;
        this.encherisseur = encherisseur;
    }
}
