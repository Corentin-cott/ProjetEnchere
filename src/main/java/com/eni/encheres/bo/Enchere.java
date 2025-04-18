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
    private double montantEnchere;
    private long idArticle;

    public Enchere(long idArticle, long noUtilisateur) {
        this.idArticle = idArticle;
        this.noUtilisateur = noUtilisateur;
    }
}
