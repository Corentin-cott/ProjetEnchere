package com.eni.encheres.dao;

import com.eni.encheres.bo.Enchere;

import java.util.List;

public interface IDAOEnchere {

    List<Enchere> findAll();

    void ajouterEnchere(Enchere enchere);

    List<Enchere> findByArticleId(long idArticle);
<<<<<<< Updated upstream
=======

    Enchere findByEnchereId(long idEnchere);

    Enchere getEncheresParArticleId(long articleId);
>>>>>>> Stashed changes
}
