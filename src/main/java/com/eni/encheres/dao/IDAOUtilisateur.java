package com.eni.encheres.dao;

import com.eni.encheres.bo.Utilisateur;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public interface IDAOUtilisateur {
    public Utilisateur getUtilisateurByPseudo(String pseudo);
    public Utilisateur getUtilisateurByEmail(String email);
    public Utilisateur getUtilisateurById(long id);
    public void deleteUtilisateurById(long id);
    public void addUtilisateur(Utilisateur utilisateur);
    public List<Utilisateur> getUtilisateurs();
    public void updateUtilisateur(Utilisateur utilisateur);
    public void disableUtilisateur(long id);
}
