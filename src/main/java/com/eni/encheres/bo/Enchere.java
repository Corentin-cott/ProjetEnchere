package com.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enchere {
    private String dateEnchere;
    private double montantEnchere;
    private ArticleVendu article;
    private Utilisateur encherisseur;
}
