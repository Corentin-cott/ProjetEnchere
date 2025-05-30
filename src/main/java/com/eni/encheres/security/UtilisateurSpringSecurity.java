package com.eni.encheres.security;

import com.eni.encheres.bo.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurSpringSecurity implements UserDetails {

    private Utilisateur utilisateur;

    /**
     * Comment je recupère les permissions  ?
     * => à partir du membre
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // si le membre est admin
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (utilisateur.isAdmin()) {
            // je lui donne le rôle "admin" (équivalent à la permission "ROLE_admin")
            authorities.add(new SimpleGrantedAuthority("ROLE_admin"));
        }
        if(utilisateur.isDisabled()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_disabled"));
        }
        // sinon je lui donne le rôle "user (équivalent à la permission "ROLE_user")
        authorities.add(new SimpleGrantedAuthority("ROLE_user"));
        return authorities;
    }

    /**
     * Comment je recupère le mot de passe ?
     * => à partir du membre
     */
    @Override
    public String getPassword() {
        return this.utilisateur.getMotDePasse();
    }

    /**
     * Comment je recupère le pseudo  ?
     * => à partir du membre
     */
    @Override
    public String getUsername() { return this.utilisateur.getPseudo(); }
}
