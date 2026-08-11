package com.project.qampus.service.security;

import com.project.qampus.model.User;
import com.project.qampus.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private User testUser;
    private final String secretKey = "test-secret-key-for-unit-testing";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", secretKey);

        testUser = new User();
        testUser.setId("user-123");
        testUser.setName("Maria Silva");
        testUser.setEmail("maria@qampus.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(Role.STUDENT);
    }

    @Test
    void shouldGenerateValidTokenForUser() {
        String token = tokenService.generateToken(testUser);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldValidateTokenAndReturnUserEmail() {
        String token = tokenService.generateToken(testUser);

        String subject = tokenService.validateToken(token);

        assertEquals("maria@qampus.com", subject);
    }

    @Test
    void shouldReturnNullWhenValidatingInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        String subject = tokenService.validateToken(invalidToken);

        assertNull(subject);
    }

    @Test
    void shouldReturnNullWhenValidatingTokenSignedWithDifferentSecret() {
        TokenService anotherTokenService = new TokenService();
        ReflectionTestUtils.setField(anotherTokenService, "secret", "different-secret-key");

        String token = anotherTokenService.generateToken(testUser);

        String subject = tokenService.validateToken(token);

        assertNull(subject);
    }
}
