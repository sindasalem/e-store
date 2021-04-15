package com.quest.etna.config;

import com.quest.etna.model.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        JwtUserDetails userDetails = verifyToken(request);
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private JwtUserDetails verifyToken(HttpServletRequest request) {
        String requestToken = request.getHeader("Authorization");
        if (requestToken == null || !requestToken.startsWith("Bearer ")) return null;
        requestToken = requestToken.substring(7);
        try {
            String username = jwtTokenUtil.getUsernameFromToken(requestToken);
            if (username == null || username.isEmpty()) return null;
            JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
            if (userDetails == null) return null;
            boolean verifiedToken = jwtTokenUtil.validateToken(requestToken, userDetails);
            return verifiedToken ? userDetails : null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
