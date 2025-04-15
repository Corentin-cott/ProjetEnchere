package com.eni.encheres.security;

import com.eni.encheres.bo.Utilisateur;
import com.eni.encheres.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // ne pas oublier de mettre Service
public class GestionPersonaliseeUtilisateurs implements UserDetailsService {

    @Autowired
    private UtilisateurService utilisateurService;

    /*
     * Elle doit donc définir comment on recupère un UtilisateurSpringSecurity
     * à partir d'un pseudo
     * => A partir de notre service qui gère les membres
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur membreTrouve =  utilisateurService.getUtilisateurByPseudo(username);

        // je retourne le membre dans son "wrapper"
        return new UtilisateurSpringSecurity(membreTrouve);
    }
}
