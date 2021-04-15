package com.quest.etna.controller;

import com.quest.etna.exception.AuthorizationException;
import com.quest.etna.exception.DuplicateException;
import com.quest.etna.exception.NotFoundException;
import com.quest.etna.helpers.AuthentificationHelpers;
import com.quest.etna.helpers.UserRequestBody;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthentificationHelpers authentificationHelpers;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    public ResponseEntity<?> index() {
        try {
            User connectedUser = authentificationHelpers.getUser();
            return new ResponseEntity<Collection<User>>(userRepository.findAll(), HttpStatus.OK);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }}), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> item(@PathVariable String id) {
        try {
            int idInteger = Integer.parseInt(id);
            User connectedUser = authentificationHelpers.getUser();
            User user = userRepository.findById(idInteger);
            if (user == null) throw new NotFoundException("Not found");
//            userHasRight(connectedUser, user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.NOT_FOUND.getReasonPhrase());
            }}), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.BAD_REQUEST.getReasonPhrase());
            }}), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody UserRequestBody requestUser) {
        try {
            User connectedUser = authentificationHelpers.getUser();
            int idInteger = Integer.parseInt(id);
            if (requestUser.getUsername() == null && requestUser.getPassword() == null) {
                throw new Exception("bad request");
            }
            User user = userRepository.findById(idInteger);
            if (user == null) throw new NotFoundException("Not found");
            userHasRight(connectedUser, user);
            if (requestUser.getUsername() != null) {
                if (!requestUser.getUsername().equals(user.getUsername())) {
                    User foundUserWithUsername = userRepository.findByUsername(requestUser.getUsername());
                    if (foundUserWithUsername != null) throw new DuplicateException("username already exists");
                }
                user.setUsername(requestUser.getUsername());
            }
            if (requestUser.getPassword() != null) {
                if (connectedUser.getRole() != UserRole.ROLE_ADMIN) throw new AuthorizationException("unauthorized");
                user.setPassword(passwordEncoder.encode(requestUser.getPassword()));
            }
            if (requestUser.getRole() != null) {
                if (connectedUser.getRole() != UserRole.ROLE_ADMIN) throw new AuthorizationException("unauthorized");
                user.setRole(requestUser.getRole());
            }
            userRepository.save(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.NOT_FOUND.getReasonPhrase());
            }}), HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }}), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AuthorizationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }}), HttpStatus.UNAUTHORIZED);
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.CONFLICT.getReasonPhrase());
            }}), HttpStatus.CONFLICT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.BAD_REQUEST.getReasonPhrase());
            }}), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            User connectedUser = authentificationHelpers.getUser();
            int idInteger = Integer.parseInt(id);
            User user = userRepository.findById(idInteger);
            if (user == null) throw new NotFoundException("Not found");
            userHasRight(connectedUser, user);
            userRepository.delete(user);
            return new ResponseEntity<>(new HashMap<String, Boolean>() {{
                put("success", Boolean.TRUE);
            }}, HttpStatus.OK);
        } catch (AuthorizationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }}), HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, Boolean>() {{
                put("Error", Boolean.FALSE);
            }}), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.BAD_REQUEST.getReasonPhrase());
            }}), HttpStatus.BAD_REQUEST);
        }
    }

    private void userHasRight(User user, User ressourceUser) throws AuthorizationException {
        if (user == ressourceUser) return;
        if (user.getRole() == UserRole.ROLE_ADMIN) return;
        throw new AuthorizationException("Unauthorized");
    }
}
