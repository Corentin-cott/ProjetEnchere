package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Categorie;
import com.eni.encheres.bo.Utilisateur;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DAOArticleVenduMock implements IDAOArticleVendu {

    public List<ArticleVendu> articlesVendus = new ArrayList<>();
    private int idCourant = 100;

    Utilisateur vendeur = new Utilisateur(1L, "Alice", "alice@example.com", "motdepasse");

    IDAOCategorie daoCategorie = new DAOCategorieMock();
    IDAOUtilisateur daoUtilisateur = new DAOUtilisateurMock();

    @Override
    public ArticleVendu selectById(long id) {
        return articlesVendus.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public DAOArticleVenduMock() {
        articlesVendus.add(new ArticleVendu(
                1,
                "PC",
                "Imac 24",
                LocalDateTime.of(2025, 4, 10, 0, 0),
                LocalDateTime.of(2025, 4, 15, 0, 0),
                1000,
                1200,
                daoUtilisateur.getUtilisateurById(1),
                daoCategorie.trouveParId(1)
        ));

        articlesVendus.add(new ArticleVendu(
                2,
                "Vélo",
                "VTT Blanc",
                LocalDateTime.of(2025, 4, 10, 0, 0),
                LocalDateTime.of(2025, 4, 15, 0, 0),
                1000,
                1200,
                daoUtilisateur.getUtilisateurById(2),
                daoCategorie.trouveParId(2)
        ));

        articlesVendus.add(new ArticleVendu(
                3,
                "Téléphone",
                "Iphone 15",
                LocalDateTime.of(2025, 4, 15, 0, 0),
                LocalDateTime.of(2025, 5, 15, 0, 0),
                200,
                300,
                daoUtilisateur.getUtilisateurById(5),
                daoCategorie.trouveParId(3)
        ));
    }

    @Override
    public List<ArticleVendu> selectAll() {
        return articlesVendus;
    }

    @Override
    public void addArticleVendu(ArticleVendu articleVendu) {
        articleVendu.setId(idCourant++);
        articlesVendus.add(articleVendu);
    }
}
