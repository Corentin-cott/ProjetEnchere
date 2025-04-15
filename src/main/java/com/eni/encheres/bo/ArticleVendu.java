package com.eni.encheres.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVendu {
    private long id;
    private String nom;
    private String description;
    private LocalDateTime  dateDebutEncheres;
    private LocalDateTime dateFinEncheres;
    private double miseAPrix;
    private double prixVente;
    private boolean etatVente;

    private Retrait retrait;
    private Categorie categorie;
    private Utilisateur acheteur;
    private Utilisateur vendeur;

    public ArticleVendu(long id, String nom, String description,
                        LocalDateTime  dateDebutEncheres, LocalDateTime dateFinEncheres, double miseAPrix, double prixVente) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
    }
}
