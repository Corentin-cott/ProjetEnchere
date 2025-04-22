package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Utilisateur;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public interface IDAOArticleVendu {

        List<ArticleVendu> selectAll();
        public void addArticleVendu(ArticleVendu articleVendu);

        public ArticleVendu selectById(long id);
        ArticleVendu selectByVendeur(Utilisateur vendeur);
        ArticleVendu selectByAcheteur(Utilisateur acheteur);
        public void deleteArticleById(long id);
        public void updateArticle(ArticleVendu article);

}
