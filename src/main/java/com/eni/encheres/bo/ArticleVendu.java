package com.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVendu {
    private long id;
    private String nom;
    private String description;
    private String dateDebutEncheres;
    private String dateFinEncheres;
    private double miseAPrix;
    private double prixVente;
    private boolean etatVente;

    private Retrait retrait;
    private Categorie categorie;
    private Utilisateur acheteur;
    private Utilisateur vendeur;
}
