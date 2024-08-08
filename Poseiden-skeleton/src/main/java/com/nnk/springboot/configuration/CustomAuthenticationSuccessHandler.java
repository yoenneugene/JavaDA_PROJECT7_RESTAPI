package com.nnk.springboot.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Méthode appelée lorsque l'authentification est réussie.
     *
     * Cette méthode redirige l'utilisateur vers une page spécifique en fonction de son rôle.
     * Si l'utilisateur a le rôle "ROLE_ADMIN", il est redirigé vers la liste des utilisateurs.
     * Si l'utilisateur a le rôle "ROLE_USER", il est redirigé vers la liste des offres.
     *
     * @param request L'objet {@link HttpServletRequest} contenant la requête HTTP du client.
     * @param response L'objet {@link HttpServletResponse} contenant la réponse HTTP.
     * @param authentication L'objet {@link Authentication} contenant les informations d'authentification de l'utilisateur.
     *
     * @throws IOException En cas d'erreur d'entrée/sortie lors de la redirection.
     * @throws ServletException En cas d'erreur de servlet.
     * @throws IllegalStateException Si aucun rôle reconnu n'est trouvé pour l'utilisateur authentifié.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Obtenir les rôles de l'utilisateur authentifié
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/user/list");
                return;
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                response.sendRedirect("/bidList/list");
                return;
            }
        }
        throw new IllegalStateException();
    }
}
