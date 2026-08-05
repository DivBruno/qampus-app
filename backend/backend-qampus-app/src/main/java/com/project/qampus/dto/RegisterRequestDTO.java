package com.project.qampus.dto;

import com.project.qampus.model.enums.Role;

public record RegisterRequestDTO(String name, String email, String password, Role role) {
    
}
