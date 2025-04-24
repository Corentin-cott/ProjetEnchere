package com.eni.encheres.dao;

import com.eni.encheres.bo.ArticleVendu;
import com.eni.encheres.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Primary
@Repository
public class DAOUtilisateurJpa implements IDAOUtilisateur {
    private final UtilisateurRepository repository;
    PasswordEncoder passwordEncoder;

    @Autowired
    private IDAOArticleVendu articleVendu;

    @Autowired
    public DAOUtilisateurJpa(UtilisateurRepository repository,PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur getUtilisateurByPseudo(String pseudo) { return repository.findByPseudo(pseudo); }

    @Override
    public Utilisateur getUtilisateurByEmail(String email) { return repository.findByEmail(email); }

    @Override
    public Utilisateur getUtilisateurById(long id){return repository.findById(id).orElse(null);}

    @Override
    public void deleteUtilisateurById(long id) {
        supprEncheresUtilisateur(id);
        repository.deleteById(id);
    }

    @Override
    public void addUtilisateur(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        repository.save(utilisateur);
    }

    @Override
    public List<Utilisateur> getUtilisateurs() { return repository.findAll(); }

    @Override
    public void updateUtilisateur(Utilisateur utilisateur) { repository.save(utilisateur); }

    @Override
    public void disableUtilisateur(long id){
        Utilisateur utilisateur = repository.findById(id).orElseThrow();
        supprEncheresUtilisateur(id);

        if(utilisateur.isDisabled()){
            utilisateur.setDisabled(false);
        }
        else{
            utilisateur.setDisabled(true);
        }
        repository.save(utilisateur);
    }

    public void supprEncheresUtilisateur(long id){
        List<ArticleVendu> articles=articleVendu.selectAll();
        List<ArticleVendu> articlesadelete=new ArrayList<>();

        articles.forEach(article->{
            if(article.getVendeur().getId()==id){
                articlesadelete.add(article);
            }
            if(article.getAcheteur()!=null && article.getAcheteur().getId()==id){
                article.setAcheteur(null);
                article.setPrixVente(null);
                articleVendu.updateArticle(article);
            }
        });
        articlesadelete.forEach(article -> articleVendu.deleteArticleById(article.getId()));
    }
  
    @Override
    public void addUtilisateur(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        repository.save(utilisateur);
    }

    @Override
    public List<Utilisateur> getUtilisateurs() { return repository.findAll(); }

    @Override
    public void updateUtilisateur(Utilisateur utilisateur) {
        repository.save(utilisateur); }

    @Override
    public Utilisateur getUtilisateurByToken(String token) {
        System.out.println("tokenRepository :" + token);
        return repository.findByTokenReinitialisation(token);
    }

}

