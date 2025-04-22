// DAOArticleVenduJpa.java
package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public class DAOArticleVenduJpa implements IDAOArticleVendu {

    private final ArticleVenduRepository repository;

    @Autowired
    public DAOArticleVenduJpa(ArticleVenduRepository repository) {
        this.repository = repository;
    }

    @Override
    public ArticleVendu selectById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ArticleVendu selectByVendeur(Utilisateur vendeur) {
        return repository.findByVendeur(vendeur);
    }

    @Override
    public ArticleVendu selectByAcheteur(Utilisateur acheteur) {
        return repository.findByAcheteur(acheteur);
    }

    @Override
    public List<ArticleVendu> selectAll() {
        return repository.findAll();
    }

    @Override
    public void addArticleVendu(ArticleVendu articleVendu) {
        repository.save(articleVendu);
    }

    public void deleteArticleById(long id){repository.deleteById(id);};
    public void updateArticle(ArticleVendu article){repository.save(article);};
}
