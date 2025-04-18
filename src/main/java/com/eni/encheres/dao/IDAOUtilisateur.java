package com.eni.encheres.dao;

import com.eni.encheres.bo.Utilisateur;

import java.util.List;

public interface IDAOUtilisateur {
    public Utilisateur getUtilisateurByPseudo(String pseudo);
    public Utilisateur getUtilisateurByEmail(String email);
    public Utilisateur getUtilisateurById(double id);
    public void deleteUtilisateurById(double id);
    public void addUtilisateur(Utilisateur utilisateur);
    public List<Utilisateur> getUtilisateurs();
    public void updateUtilisateur(Utilisateur utilisateur);
}
