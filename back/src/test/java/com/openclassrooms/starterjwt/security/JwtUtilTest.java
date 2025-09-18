package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    private JwtUtils jwtUtils;
    private final String testSecret = "testSecretKey";
    private final int testExpirationMs = 3600000; // 1 heure


    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        jwtUtils = new JwtUtils();

        // Injecter la valeur de jwtSecret par réflexion
        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, testSecret);

        // Injecter la valeur de jwtExpirationMs par réflexion
        Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expirationField.setAccessible(true);
        expirationField.setInt(jwtUtils, testExpirationMs);
    }

    @Test
    void testGenerateAndValidateToken() {
        // Créer un principal de type UserDetailsImpl (adapté à votre implementation)
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "user","USER",false,"password");

        // Simuler un Authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Générer le token
        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Vérifier le nom d'utilisateur extrait du token
        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("testuser", username);

        // Vérifier la validité du token
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testInvalidToken() {
        // Token invalide (structure incorrecte)
        String invalidToken = "abc.def.ghi";

        // Devrait retourner false
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    void testExpiredToken() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        // Réduire la durée de vie du token pour le test
        Field expirationField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expirationField.setAccessible(true);
        expirationField.setInt(jwtUtils, 1);

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "user","USER",false,"password");
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        // Attendre un peu pour laisser le token expirer
        Thread.sleep(2);

        // Maintenant le token doit être expiré
        assertFalse(jwtUtils.validateJwtToken(token));
    }

}
