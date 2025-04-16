package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Utilisateur;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAOArticleVenduMock implements IDAOArticleVendu {

    public List<ArticleVendu> articlesVendus = new ArrayList<>();

    Utilisateur vendeur = new Utilisateur(1L, "Alice", "alice@example.com", "motdepasse");

    public DAOArticleVenduMock() {
        articlesVendus.add(new ArticleVendu(
                1,
                "PC",
                "Imac 24",
                LocalDateTime.of(2025, 4, 10, 0, 0),
                LocalDateTime.of(2025, 4, 15, 0, 0),
                1000,
                1200,
                vendeur
        ));

        articlesVendus.add(new ArticleVendu(
                2,
                "Vélo",
                "VTT Blanc",
                LocalDateTime.of(2025, 4, 10, 0, 0),
                LocalDateTime.of(2025, 4, 15, 0, 0),
                1000,
                1200,
                vendeur
        ));

        articlesVendus.add(new ArticleVendu(
                3,
                "Téléphone",
                "Iphone 15",
                LocalDateTime.of(2025, 4, 15, 0, 0),
                LocalDateTime.of(2025, 5, 15, 0, 0),
                200,
                300,
                vendeur
        ));
    }

    @Override
    public List<ArticleVendu> selectAll() {
        return articlesVendus;
    }
}
