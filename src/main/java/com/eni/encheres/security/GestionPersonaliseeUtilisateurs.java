package com.eni.encheres.security;

import com.eni.encheres.bo.Utilisateur;
import com.eni.encheres.dao.IDAOUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // ne pas oublier de mettre Service
public class GestionPersonaliseeUtilisateurs implements UserDetailsService {

    @Autowired
    private IDAOUtilisateur utilisateurDao;

    /*
     * Elle doit donc définir comment on recupère un UtilisateurSpringSecurity
     * à partir d'un pseudo
     * => A partir de notre service qui gère les membres
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur membreTrouve =  utilisateurDao.getUtilisateurByPseudo(username);
        if(membreTrouve == null) {
            membreTrouve =  utilisateurDao.getUtilisateurByEmail(username);
            if(membreTrouve == null) {
                throw new UsernameNotFoundException("Utilisateur non trouvé");
            }
        }
        return new UtilisateurSpringSecurity(membreTrouve);
    }
}
