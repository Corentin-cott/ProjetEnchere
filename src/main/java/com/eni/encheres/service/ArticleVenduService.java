package com.eni.encheres.service;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.dao.IDAOArticleVendu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleVenduService {

    @Autowired
    IDAOArticleVendu daoArticleVendu;

    public ServiceResponse<List<ArticleVendu>> getAll() {
        List<ArticleVendu> articleVendus = daoArticleVendu.selectAll();

        if (articleVendus.isEmpty()){

            // erreur 1 : Liste Vide
            return ServiceResponse.buildResponse ("2", "Liste vide", articleVendus);
        }

        // erreur 2 : Liste OK
        return ServiceResponse.buildResponse ("2", "Liste des articles a été récupérée avec succès", articleVendus);
    }
}
