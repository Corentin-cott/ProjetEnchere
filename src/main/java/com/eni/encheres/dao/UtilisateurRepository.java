package com.eni.encheres.dao;


import com.eni.encheres.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByPseudo(String pseudo);
    Utilisateur findByEmail(String email);
}
