package com.eni.encheres.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

// on a une classe dans laquelle on va définir la configuration de Spring Security

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    GestionPersonaliseeUtilisateurs gestionPersonaliseeUtilisateurs;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/").authenticated()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
        )
                // on effectue une authentification basique (user/mdp)
                .httpBasic(AbstractHttpConfigurer::disable)
                // on utilise le formulaire par défaut de Spring
                .formLogin(form -> form
                        .loginPage("/connection") // ta page de login custom
                        .loginProcessingUrl("/connection")     // POST → soumission du form ici
                        .defaultSuccessUrl("/", false)
                        .permitAll()
                )
                // quand on se déconnecte=> on redirige vers l'accueil
                .logout((logout) -> logout.logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean // on définit un bean pour l'utilitaire d'encryption de mot de passe
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ATTENTION : désactiver ce bean lorsqu'on utilise une gestion personalisée des utilisateurs
     //@Bean  //on définit un bean pour la gestion des utilisateurs en mémoire
    public InMemoryUserDetailsManager userDetailsService() {
        // je gère à la manoo une liste d'utilisateurs Spring qui doivent implémenter l'interface UserDetails (ici on prend la classe User de Spring Security)
        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(User.withUsername("membre").password(passwordEncoder().encode("membre123"))
                .roles("user").build());
        userDetailsList.add(User.withUsername("admin").password(passwordEncoder().encode("admin123"))
                .roles("admin", "user").build());
        return new InMemoryUserDetailsManager(userDetailsList);
    }

}