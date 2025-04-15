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

    public ArticleVendu(long id, String nom, String description,
                        String dateDebutEncheres, String dateFinEncheres, double miseAPrix, double prixVente) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
    }
}
