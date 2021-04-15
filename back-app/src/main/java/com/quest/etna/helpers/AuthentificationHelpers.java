package com.quest.etna.helpers;

import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthentificationHelpers {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    public JwtUserDetails getUserDetails() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return jwtUserDetailsService.loadUserByUsername(username);
    }

    public User getUser() throws AuthenticationException {
        JwtUserDetails userDetails = this.getUserDetails();
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username);
    }
}
