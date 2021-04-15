package com.quest.etna.controller;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.helpers.AuthenticateRequestBody;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", "Missing parameters");
            }}), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(new HashMap<String, String>() {{
                put("Error", "Username already exists");
            }}, HttpStatus.CONFLICT);
        }
        User newUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
        return new ResponseEntity<UserDetails>(new UserDetails(newUser.getUsername(), newUser.getRole()), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticateRequestBody authenticateRequestBody) throws Exception {
        if (authenticateRequestBody.getUsername() == null || authenticateRequestBody.getPassword() == null) {
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", "Missing parameters");
            }}), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticateRequestBody.getUsername(), authenticateRequestBody.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
            JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(authenticateRequestBody.getUsername());
            String token = jwtTokenUtil.generateToken(jwtUserDetails);

            return new ResponseEntity<>(new HashMap<String, String>() {{
                put("token", token);
                put("username", authenticateRequestBody.getUsername());
            }}, HttpStatus.OK);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }}), HttpStatus.INTERNAL_SERVER_ERROR);
//            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(username);

            return new ResponseEntity<JwtUserDetails>(jwtUserDetails, HttpStatus.OK);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }}), HttpStatus.INTERNAL_SERVER_ERROR);
//            throw new Exception(e.getMessage());
        }
    }
}
