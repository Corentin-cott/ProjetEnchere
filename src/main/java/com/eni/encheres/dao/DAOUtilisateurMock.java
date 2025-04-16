package com.eni.encheres.dao;

import com.eni.encheres.bo.Utilisateur;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Profile("dev")
@Service
public class DAOUtilisateurMock implements IDAOUtilisateur {

    PasswordEncoder passwordEncoder;
    List<Utilisateur> utilisateurs = new ArrayList<>();
    private int idCourant = 2;
    public DAOUtilisateurMock(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        utilisateurs.add(new Utilisateur(1L,"admin","admin","test","admin@mail.com","0600000000","Rue de l'admin","42069","Test",passwordEncoder.encode("admin"),10,true));
    }

    public Utilisateur getUtilisateurByPseudo(String pseudo){
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getPseudo().equals(pseudo)) {
                return utilisateur;
            }
        }
        return null;
    }

    public Utilisateur getUtilisateurByEmail(String email){
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getEmail().equals(email)) {
                return utilisateur;
            }
        }
        return null;
    }

    public void deleteUtilisateurById(double id){
        for (int i = 0; i < utilisateurs.size(); i++) {
            if (utilisateurs.get(i).getId() == id) {
                utilisateurs.remove(i);
                break;
            }
        }
    }

    public void addUtilisateur(Utilisateur utilisateur){
        utilisateur.setId(idCourant++);

        // ATTENTION ! il faut bien faire attention à encoder le mot de passe avant d'jouter le membre
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurs.add(utilisateur);
    }

    public List<Utilisateur> getUtilisateurs(){
        return utilisateurs;
    }
}
