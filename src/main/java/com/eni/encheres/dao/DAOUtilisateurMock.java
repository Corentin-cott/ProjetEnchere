package com.eni.encheres.dao;

import com.eni.encheres.bo.Utilisateur;
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
    List<Utilisateur> utilisateurs = new ArrayList<>();
    private int idCourant = 7;

    public DAOUtilisateurMock(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        creationList();
    }

    public DAOUtilisateurMock() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        creationList();
    }

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
        utilisateur.setId(idCourant++);

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


    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void creationList() {
        utilisateurs.add(new Utilisateur(1L, "admin", "admin", "test", "admin@mail.com", "0600000000", "Rue de l'admin", "42069", "Test", passwordEncoder.encode("admin"), 10, true));
        utilisateurs.add(new Utilisateur(2L, "user1", "John", "Doe", "user1@mail.com", "0612345678", "10 Rue de Paris", "75001", "Paris", passwordEncoder.encode("password1"), 20.0, false));
        utilisateurs.add(new Utilisateur(3L, "user2", "Jane", "Smith", "user2@mail.com", "0623456789", "15 Boulevard Saint-Germain", "75005", "Paris", passwordEncoder.encode("password2"), 15.0, true));
        utilisateurs.add(new Utilisateur(4L, "user3", "Albert", "Dupont", "user3@mail.com", "0634567890", "5 Rue Victor Hugo", "69001", "Lyon", passwordEncoder.encode("password3"), 30.0, false));
        utilisateurs.add(new Utilisateur(5L, "user4", "Clara", "Martin", "user4@mail.com", "0645678901", "25 Rue de la Liberté", "13001", "Marseille", passwordEncoder.encode("password4"), 50.0, true));
        utilisateurs.add(new Utilisateur(6L, "", "", "", "", "", "", "", "", passwordEncoder.encode(""), 0, true));
    }
}