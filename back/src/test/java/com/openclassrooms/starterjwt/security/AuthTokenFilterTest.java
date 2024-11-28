package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {
    private final AuthTokenFilter authTokenFilter = new AuthTokenFilter();

    @Test
    void parseJwtValidToken() {
        // GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();
        String validToken = "validToken";
        request.addHeader("Authorization", "Bearer " + validToken);

        // WHEN
        String result = authTokenFilter.parseJwt(request);

        // THEN
        assertThat(result).isEqualTo(validToken);
    }

    @Test
    void parseJwtNoTokenInHeader() {
        //GIVEN
        MockHttpServletRequest request = new MockHttpServletRequest();

        // WHEN
        String result = authTokenFilter.parseJwt(request);

        // THEN
        assertThat(result).isNull();
    }
}
