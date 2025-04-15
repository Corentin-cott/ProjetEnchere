package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DAOArticleVenduMock implements IDAOArticleVendu {

    public List<ArticleVendu> articlesVendus = new ArrayList<ArticleVendu>();

    public DAOArticleVenduMock() {
        articlesVendus.add(new ArticleVendu(2L, "PC", "Imac 24","10/04/2025", "15/04/2025", 1000, 1200));
        articlesVendus.add(new ArticleVendu(3L, "Vélo", "VTT Blanc","10/04/2025", "15/04/2025", 1000, 1200));
        articlesVendus.add(new ArticleVendu(1L, "Téléphone", "Iphone 15","15/04/2025", "15/05/2025", 200, 300));
    }

    @Override
    public List<ArticleVendu> selectAll() {
        return articlesVendus;
    }
}
