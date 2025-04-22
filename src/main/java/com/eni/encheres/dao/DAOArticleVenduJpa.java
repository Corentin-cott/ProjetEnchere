// DAOArticleVenduJpa.java
package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
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
    public List<ArticleVendu> selectAll() {
        return repository.findAll();
    }

    @Override
    public void addArticleVendu(ArticleVendu articleVendu) {
        repository.save(articleVendu);
    }
}
