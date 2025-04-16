package com.eni.encheres.bo;

import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    private long id;
    private String pseudo;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String rue;
    private String codePostal;
    private String ville;
    private String motDePasse;
    private double credit;
    private boolean admin;

    @OneToMany(mappedBy = "vendeur")
    private List<ArticleVendu> articles;

    public Utilisateur(Long id, String nom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal, String ville, String motDePasse) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.motDePasse = motDePasse;
        this.credit = 0;
        this.admin = false;
    }

    public Utilisateur(long id, boolean admin, double credit, String motDePasse, String ville, String codePostal, String rue, String telephone, String email, String prenom, String nom, String pseudo) {
        this.id = id;
        this.admin = admin;
        this.credit = credit;
        this.motDePasse = motDePasse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.rue = rue;
        this.telephone = telephone;
        this.email = email;
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
    }
}
