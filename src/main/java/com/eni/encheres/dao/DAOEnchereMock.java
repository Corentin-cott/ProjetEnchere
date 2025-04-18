package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Enchere;
import com.eni.encheres.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DAOEnchereMock implements IDAOEnchere {

    private List<Enchere> encheres = new ArrayList<>();

    @Autowired
    private IDAOUtilisateur utilisateurdao=new DAOUtilisateurMock(new BCryptPasswordEncoder());

    public DAOEnchereMock() {

        // Enchère EN COURS
        ArticleVendu articleEnCours1 = new ArticleVendu(
                1L,
                "Ordinateur portable",
                "Dell XPS 13",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(2),
                500.0,
                650.0,
                utilisateurdao.getUtilisateurById(1)
        );

        ArticleVendu articleEnCours2 = new ArticleVendu(
                2L,
                "Smartphone",
                "iPhone 13 Pro",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(3),
                800.0,
                1000.0,
                utilisateurdao.getUtilisateurById(2)
        );

        ArticleVendu articleEnCours3 = new ArticleVendu(
                3L,
                "Montre connectée",
                "Apple Watch Series 7",
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().plusDays(1),
                300.0,
                450.0,
                utilisateurdao.getUtilisateurById(3)
        );

        ArticleVendu articleEnCours4 = new ArticleVendu(
                4L,
                "Casque audio sans fil",
                "Sony WH-1000XM4",
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().plusDays(2),
                250.0,
                350.0,
                utilisateurdao.getUtilisateurById(1)
        );

        // Enchère TERMINÉE
        ArticleVendu articleTermine = new ArticleVendu(
                5L,
                "Console PS5",
                "PlayStation 5 avec manette",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(2),
                300.0,
                450.0,
                utilisateurdao.getUtilisateurById(4)
        );

        // Enchère NON DÉMARRÉE
        ArticleVendu articleFutur = new ArticleVendu(
                6L,
                "Vélo électrique",
                "Vélo électrique neuf",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(10),
                800.0,
                0.0,
                utilisateurdao.getUtilisateurById(3)
        );

        encheres.add(new Enchere(1L, LocalDateTime.now().minusHours(3), 620.0, articleEnCours1, utilisateurdao.getUtilisateurById(1)));
        encheres.add(new Enchere(2L, LocalDateTime.now().minusDays(5), 400.0, articleTermine, utilisateurdao.getUtilisateurById(4)));
        encheres.add(new Enchere(1L, LocalDateTime.now().plusDays(3), 820.0, articleFutur, utilisateurdao.getUtilisateurById(3)));
        encheres.add(new Enchere(3L, LocalDateTime.now().minusHours(2), 850.0, articleEnCours2, utilisateurdao.getUtilisateurById(2)));
        encheres.add(new Enchere(4L, LocalDateTime.now().minusHours(4), 400.0, articleEnCours3, utilisateurdao.getUtilisateurById(3)));
        encheres.add(new Enchere(5L, LocalDateTime.now().minusHours(1), 300.0, articleEnCours4, utilisateurdao.getUtilisateurById(1)));
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