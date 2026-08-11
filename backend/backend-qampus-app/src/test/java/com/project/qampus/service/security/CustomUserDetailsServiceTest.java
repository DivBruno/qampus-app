package com.project.qampus.service.security;

import com.project.qampus.model.User;
import com.project.qampus.model.enums.Role;
import com.project.qampus.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user-1");
        testUser.setName("Joao");
        testUser.setEmail("joao@qampus.com");
        testUser.setPassword("hashed_pass");
        testUser.setRole(Role.STUDENT);
    }

    @Test
    void shouldLoadUserByUsernameWhenUserExists() {
        when(userRepository.findByEmail("joao@qampus.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("joao@qampus.com");

        assertNotNull(userDetails);
        assertEquals("joao@qampus.com", userDetails.getUsername());
        assertEquals("hashed_pass", userDetails.getPassword());
        verify(userRepository, times(1)).findByEmail("joao@qampus.com");
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("nonexistent@qampus.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistent@qampus.com");
        });

        verify(userRepository, times(1)).findByEmail("nonexistent@qampus.com");
    }
}
