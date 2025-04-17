package com.eni.encheres.bo;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "no_utilisateur", nullable = false)
    private Utilisateur vendeur;

    public ArticleVendu(Long id, String nom, String description,
                        LocalDateTime dateDebut, LocalDateTime dateFin,
                        Double miseAPrix, Double prixVente,
                        Utilisateur vendeur) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebutEncheres = dateDebut;
        this.dateFinEncheres = dateFin;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.vendeur = vendeur;
    }

    public ArticleVendu(long id, String nom, String description, LocalDateTime dateDebutEncheres,
                        LocalDateTime dateFinEncheres, double miseAPrix, double prixVente,
                        Utilisateur vendeur) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.vendeur = vendeur;
    }

    public ArticleVendu(long id, String nom, String description, LocalDateTime dateDebutEncheres,
                        LocalDateTime dateFinEncheres, double miseAPrix, double prixVente,
                        Utilisateur vendeur, Categorie categorie) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.vendeur = vendeur;
        this.categorie = categorie;
    }
}
