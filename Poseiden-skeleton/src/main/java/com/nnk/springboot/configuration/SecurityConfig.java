package com.nnk.springboot.configuration;

import com.nnk.springboot.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Classe de configuration de la sécurité Spring.
 *
 * Cette classe configure les filtres de sécurité pour l'application,
 * y compris les règles d'autorisation, le cryptage des mots de passe,
 * et le gestionnaire de succès d'authentification personnalisé.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Définit un encodeur de mot de passe basé sur BCrypt.
     *
     * @return Une instance de {@link PasswordEncoder} utilisant BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la chaîne de filtres de sécurité.
     *
     * Cette méthode définit les règles d'autorisation pour les différentes routes,
     * la page de connexion personnalisée et les paramètres de déconnexion.
     *
     * @param http L'objet {@link HttpSecurity} utilisé pour configurer la sécurité web.
     * @return La chaîne de filtres de sécurité configurée.
     * @throws Exception Si une erreur de configuration se produit.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/user/**").hasRole("ADMIN");
                    auth.requestMatchers("/bidList/**").hasRole("USER");
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .successHandler(customAuthenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/app-logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    /**
     * Définit un gestionnaire de succès d'authentification personnalisé.
     *
     * Ce gestionnaire détermine la redirection après une authentification réussie
     * en fonction des rôles de l'utilisateur.
     *
     * @return Une instance de {@link AuthenticationSuccessHandler}.
     */
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}


