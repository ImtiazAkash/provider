package com.service.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.service.provider.dto.AuthResponse;
import com.service.provider.dto.UserDto;
import com.service.provider.model.User;
import com.service.provider.security_configaration.JwtTokenUtil;
import com.service.provider.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

     @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDTO) {
        String createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public AuthResponse loginUser(@RequestBody UserDto userInfo) {
        
        AuthResponse elpResponse = new AuthResponse();
        try {
            User userDetails = this.userService.loadUserByUsername(userInfo.getEmail());

//            validating username and password by spring security and generation token for valid user
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userInfo.getEmail(), userInfo.getPassword());
            
            this.authenticationManager.authenticate(authToken);
            String token = jwtTokenUtil.generateToken(userDetails);

//             
//            setting user login log info
            elpResponse.setJwtToken(token);
            elpResponse.setUser(userDetails);
            elpResponse.getUser().setPassword(null);
            elpResponse.setMessage("Login success");
            elpResponse.setStatus(true);
            return elpResponse;
        } catch (AuthenticationException e) {

            elpResponse.setMessage("Incorrect Username or Password!");
            return elpResponse;
        }
    }

}
