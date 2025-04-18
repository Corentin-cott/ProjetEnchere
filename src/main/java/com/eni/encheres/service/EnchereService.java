package com.eni.encheres.service;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Categorie;
import com.eni.encheres.bo.Enchere;
import com.eni.encheres.dao.IDAOArticleVendu;
import com.eni.encheres.dao.IDAOCategorie;
import com.eni.encheres.dao.IDAOEnchere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnchereService {

    @Autowired
    IDAOEnchere daoEnchere;

    @Autowired
    private IDAOCategorie daoCategorie;

    @Autowired
    private IDAOArticleVendu daoArticleVendu;

    public List<Enchere> filtrerEncheres(List<String> filtresAchat, List<String> filtresVente,
                                         String pseudoConnecte, String recherche, Long idCategorie) {

        List<Enchere> toutes = daoEnchere.findAll();

        LocalDateTime maintenant = LocalDateTime.now();

        return toutes.stream()
                .filter(e -> filtreTexte(e, recherche))
                .filter(e -> filtreAchat(e, filtresAchat, maintenant))
                .filter(e -> filtreVente(e, filtresVente, pseudoConnecte, maintenant))
                .filter(e -> filtreCategorie(e, idCategorie))
                .collect(Collectors.toList());
    }

    private boolean filtreTexte(Enchere e, String recherche) {
        if (recherche == null || recherche.isEmpty()) return true;
        ArticleVendu article= daoArticleVendu.selectById(e.getIdArticle());
        return article.getNom().toLowerCase().contains(recherche.toLowerCase());
    }

    private boolean filtreAchat(Enchere e, List<String> filtresAchat, LocalDateTime maintenant) {
        if (filtresAchat == null || filtresAchat.isEmpty()) return true;

        ArticleVendu article = daoArticleVendu.selectById(e.getIdArticle());
        return filtresAchat.stream().anyMatch(filtre -> {
            switch (filtre) {
                case "ouverts":
                    return article.getDateDebutEncheres().isBefore(maintenant);
                case "enCours":
                    return isEnCours(article, maintenant);
                case "terminees":
                    return article.getDateFinEncheres().isBefore(maintenant);
                default:
                    return false;
            }
        });
    }

    private boolean filtreVente(Enchere e, List<String> filtresVente, String pseudoConnecte, LocalDateTime maintenant) {
        if (filtresVente == null || filtresVente.isEmpty()) return true;

        ArticleVendu article = daoArticleVendu.selectById(e.getIdArticle());
        if (pseudoConnecte == null || article.getVendeur() == null ||
                !pseudoConnecte.equals(article.getVendeur().getPseudo())) return false;

        return filtresVente.stream().anyMatch(filtre -> {
            switch (filtre) {
                case "fermees":
                    return article.getDateDebutEncheres().isAfter(maintenant);
                case "enCours":
                    return isEnCours(article, maintenant);
                case "terminees":
                    return article.getDateFinEncheres().isBefore(maintenant);
                default:
                    return false;
            }
        });
    }

    private boolean filtreCategorie(Enchere e, Long idCategorie) {
        if (idCategorie == null) return true;
        ArticleVendu article = daoArticleVendu.selectById(e.getIdArticle());
        Categorie categorie = article.getCategorie();
        System.out.println("categorie : " + categorie);
        return idCategorie.equals((long) categorie.getId());
    }

    private boolean isEnCours(ArticleVendu article, LocalDateTime maintenant) {
        return article.getDateDebutEncheres().isBefore(maintenant)
                && article.getDateFinEncheres().isAfter(maintenant);
    }

    public List<Enchere> getEncheresParArticle(long idArticle) {
        return daoEnchere.findByArticleId(idArticle);
    }

    public ServiceResponse<String> ajouterEnchere(Enchere enchere) {
        daoEnchere.ajouterEnchere(enchere);
        return ServiceResponse.buildResponse("00", "Enchère enregistrée", null);
    }

    public List<Enchere> getEncheres(){
        return daoEnchere.findAll();
    }
}



