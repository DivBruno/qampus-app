package com.project.qampus.service.security;

import com.project.qampus.model.User;
import com.project.qampus.model.enums.Role;
import com.project.qampus.repositories.BlacklistedTokenRepository;
import com.project.qampus.repositories.UserRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlacklistedTokenRepository blacklistRepository;

    @InjectMocks
    private SecurityFilter securityFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;
    private User testUser;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        testUser = new User();
        testUser.setId("user-1");
        testUser.setName("Ana");
        testUser.setEmail("ana@qampus.com");
        testUser.setRole(Role.PROFESSOR);

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterChainWhenNoTokenProvided() throws ServletException, IOException {
        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(blacklistRepository, never()).existsByToken(anyString());
        verify(tokenService, never()).validateToken(anyString());
    }

    @Test
    void shouldReturn401UnauthorizedWhenTokenIsBlacklisted() throws ServletException, IOException {
        String rawToken = "blacklisted-token";
        request.addHeader("Authorization", "Bearer " + rawToken);

        when(blacklistRepository.existsByToken(rawToken)).thenReturn(true);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertEquals(401, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(tokenService, never()).validateToken(anyString());
    }

    @Test
    void shouldAuthenticateUserWhenValidTokenProvided() throws ServletException, IOException {
        String rawToken = "valid-jwt-token";
        request.addHeader("Authorization", "Bearer " + rawToken);

        when(blacklistRepository.existsByToken(rawToken)).thenReturn(false);
        when(tokenService.validateToken(rawToken)).thenReturn("ana@qampus.com");
        when(userRepository.findByEmail("ana@qampus.com")).thenReturn(Optional.of(testUser));

        securityFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(testUser, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR")));
    }

    @Test
    void shouldNotAuthenticateWhenTokenValidationReturnsNull() throws ServletException, IOException {
        String rawToken = "invalid-jwt-token";
        request.addHeader("Authorization", "Bearer " + rawToken);

        when(blacklistRepository.existsByToken(rawToken)).thenReturn(false);
        when(tokenService.validateToken(rawToken)).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(userRepository, never()).findByEmail(anyString());
    }
}
