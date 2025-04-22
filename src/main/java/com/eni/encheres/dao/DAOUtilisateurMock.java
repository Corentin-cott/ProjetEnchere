package com.eni.encheres.dao;

import com.eni.encheres.bo.Utilisateur;
import lombok.Getter;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Service
public class DAOUtilisateurMock implements IDAOUtilisateur {


    PasswordEncoder passwordEncoder;
    @Getter
    List<Utilisateur> utilisateurs = new ArrayList<>();

    /*
    public DAOUtilisateurMock(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        creationList();
    }

    public DAOUtilisateurMock() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        creationList();
    }
    */

    public Utilisateur getUtilisateurByPseudo(String pseudo) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getPseudo().equals(pseudo)) {
                return utilisateur;
            }
        }
        return null;
    }

    public Utilisateur getUtilisateurByEmail(String email) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getEmail().equals(email)) {
                return utilisateur;
            }
        }
        return null;
    }

    @Override
    public Utilisateur getUtilisateurById(double id) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getId() == id) {
                return utilisateur;
            }
        }
        return null;
    }

    public void deleteUtilisateurById(double id) {
        for (int i = 0; i < utilisateurs.size(); i++) {
            if (utilisateurs.get(i).getId() == id) {
                utilisateurs.remove(i);
                break;
            }
        }
    }

    public void addUtilisateur(Utilisateur utilisateur) {
        // ATTENTION ! il faut bien faire attention à encoder le mot de passe avant d'jouter le membre
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurs.add(utilisateur);
    }

    public void updateUtilisateur(Utilisateur utilisateur) {
        for (int i = 0; i < utilisateurs.size(); i++) {
            if (utilisateurs.get(i).getId() == utilisateur.getId()) {
                if (!utilisateur.getMotDePasse().isEmpty()) {
                    utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
                } else {
                    utilisateur.setMotDePasse(utilisateurs.get(i).getMotDePasse());
                }
                utilisateurs.set(i, utilisateur);
                return;
            }
        }
    }

}