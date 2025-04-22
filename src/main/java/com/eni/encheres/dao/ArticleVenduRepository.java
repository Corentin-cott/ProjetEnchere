package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleVenduRepository extends JpaRepository<ArticleVendu, Long> {
    ArticleVendu findByVendeur(Utilisateur vendeur);
    ArticleVendu findByAcheteur(Utilisateur acheteur);
}
