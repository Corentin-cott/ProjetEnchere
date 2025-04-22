package com.eni.encheres.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVendu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "dateDebEnchere", nullable = false)
    private LocalDateTime dateDebutEncheres;

    @Column(name = "dateFinEnchere", nullable = false)
    private LocalDateTime dateFinEncheres;

    @Column(name = "miseAprix", nullable = false)
    private Double miseAPrix;

    @Column(name = "prixVente")
    private Double prixVente;

    @Column(name = "estVendu")
    private boolean etatVente = false;

    // Relations

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorieId", nullable = false)
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendeurId", nullable = false)
    private Utilisateur vendeur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acheteurId")
    private Utilisateur acheteur;

    // Constructeurs personnalisés (inchangés ou ajustés légèrement si nécessaire)


    public ArticleVendu(String nom, String description, Categorie categorie, Double miseAPrix, Double prixVente,
                        LocalDateTime dateDebut, LocalDateTime dateFin, Utilisateur vendeur ) {
        this.nom = nom;
        this.description = description;
        this.dateDebutEncheres = dateDebut;
        this.dateFinEncheres = dateFin;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.vendeur = vendeur;
        this.categorie = categorie;
    }

}