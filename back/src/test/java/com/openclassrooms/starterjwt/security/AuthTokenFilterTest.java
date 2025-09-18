package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// JUnit & Mockito
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

// Spring Security
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

// Servlet
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Autres imports
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        // Simuler un header Authorization correct
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        // Simuler un JWT valide
        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn("testuser");

        // Simuler un UserDetails
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

        // Exécuter le filtre
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que le filtre a appelé le chain.doFilter
        verify(filterChain, times(1)).doFilter(request, response);

        // Vérifier que SecurityContextHolder a été mis à jour
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        // Simuler un header Authorization avec un token invalide
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");

        // Simuler JWT invalide
        when(jwtUtils.validateJwtToken("invalidToken")).thenReturn(false);

        // Exécuter le filtre
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que le filtre a appelé le chain.doFilter
        verify(filterChain, times(1)).doFilter(request, response);

        // Vérifier que SecurityContextHolder n'a pas d'authentification
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternalWithoutAuthorizationHeader() throws ServletException, IOException {
        // Pas de header Authorization
        when(request.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
