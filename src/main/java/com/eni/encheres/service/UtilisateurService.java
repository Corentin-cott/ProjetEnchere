package com.eni.encheres.service;

import com.eni.encheres.bo.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    public Utilisateur getUtilisateurByPseudo(String pseudo);
    public void deleteUtilisateurById(double id);
    public void addUtilisateur(Utilisateur utilisateur);
    public List<Utilisateur> getUtilisateurs();
}
