package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Enchere;
import com.eni.encheres.bo.Utilisateur;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DAOEnchereMock implements IDAOEnchere {

    private List<Enchere> encheres = new ArrayList<>();

    public DAOEnchereMock() {
        Utilisateur utilisateur1 = new Utilisateur(1L, "Alice", "alice@example.com", "motdepasse");
        Utilisateur utilisateur2 = new Utilisateur(2L, "Bob", "bob@example.com", "motdepasse");

        // Enchère EN COURS
        ArticleVendu articleEnCours = new ArticleVendu(
                1L,
                "Ordinateur portable",
                "Dell XPS 13",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2),
                500.0,
                650.0
        );

        // Enchère TERMINÉE
        ArticleVendu articleTermine = new ArticleVendu(
                2L,
                "Console PS5",
                "PlayStation 5 avec manette",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(2),
                300.0,
                450.0
        );

        // Enchère NON DÉMARRÉE
        ArticleVendu articleFutur = new ArticleVendu(
                3L,
                "Vélo électrique",
                "Vélo électrique neuf",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(10),
                800.0,
                0.0
        );

        encheres.add(new Enchere(1L, LocalDateTime.now().minusHours(3), 620.0, articleEnCours, utilisateur1));
        encheres.add(new Enchere(2L, LocalDateTime.now().minusDays(5), 400.0, articleTermine, utilisateur2));
        encheres.add(new Enchere(1L, LocalDateTime.now().plusDays(3), 820.0, articleFutur, utilisateur1));
    }

    @Override
    public List<Enchere> findAll() {
        return encheres;
    }

    @Override
    public void ajouterEnchere(Enchere enchere) {
        encheres.add(enchere);
    }

    @Override
    public List<Enchere> findByArticleId(long idArticle) {
        return encheres.stream()
                .filter(e -> e.getArticle().getId() == idArticle)
                .collect(Collectors.toList());
    }
}