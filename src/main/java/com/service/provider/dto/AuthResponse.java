package com.service.provider.dto;

import com.service.provider.model.User;
import lombok.Data;

@Data
public class AuthResponse {

    private User user;

    private String jwtToken;

    private String message;

    private boolean status;

    
}
