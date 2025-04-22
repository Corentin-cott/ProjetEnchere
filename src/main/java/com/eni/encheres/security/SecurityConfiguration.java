package com.eni.encheres.security;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.rememberme.key}")
    private String remembermeKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("nouvelleVente").authenticated()
                        .requestMatchers("/profil/nouveau").not().authenticated()
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
                        .failureUrl("/connection?error")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key(remembermeKey)
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(60 * 24 * 60 * 60)
                        .userDetailsService(gestionPersonaliseeUtilisateurs)
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
}