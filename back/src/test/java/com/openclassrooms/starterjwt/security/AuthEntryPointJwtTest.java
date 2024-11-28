package com.openclassrooms.starterjwt.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class AuthEntryPointJwtTest {
    private AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    @Test
    void testCommence() throws IOException, ServletException {
        //GIVEN

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        AuthenticationException authException = new AuthenticationException("Unauthorized error message") {};

        //WHEN

        authEntryPointJwt.commence(request, response, authException);

        //THEN

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentType()).isEqualTo(MimeTypeUtils.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseBody = objectMapper.readValue(response.getContentAsString(), Map.class);

        assertThat(responseBody.get("status")).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(responseBody.get("error")).isEqualTo("Unauthorized");
        assertThat(responseBody.get("message")).isEqualTo("Unauthorized error message");
        assertThat(responseBody.get("path")).isEqualTo(request.getServletPath());
    }
}
