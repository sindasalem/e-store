package com.quest.etna.controller;

import com.quest.etna.exception.AuthorizationException;
import com.quest.etna.exception.NotFoundException;
import com.quest.etna.helpers.AddressRequestBody;
import com.quest.etna.helpers.AuthentificationHelpers;
import com.quest.etna.model.Address;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;

@CrossOrigin
@RestController
public class AddressController {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthentificationHelpers authentificationHelpers;

    @GetMapping("/address")
    public ResponseEntity<?> index() {
        try {
            User user = authentificationHelpers.getUser();
            if (user.getRole() == UserRole.ROLE_ADMIN) {
                return new ResponseEntity<Collection<Address>>(addressRepository.findAll(), HttpStatus.OK);
            }
            return new ResponseEntity<Collection<Address>>(addressRepository.findAllOfUser(user), HttpStatus.OK);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }}), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<?> item(@PathVariable String id) {
        try {
            int idInteger = Integer.parseInt(id);
            User user = authentificationHelpers.getUser();
            Address address = addressRepository.findById(idInteger);
            if (address == null) throw new NotFoundException("Not found");
            userHasRight(user, address.getUser());
            return new ResponseEntity<Address>(address, HttpStatus.OK);
        } catch (AuthorizationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }}), HttpStatus.UNAUTHORIZED);
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

    @PostMapping("/address")
    public ResponseEntity<?> create(@RequestBody AddressRequestBody addressRequest) {
        System.out.println(addressRequest);
        try {
            User user = authentificationHelpers.getUser();
            if (addressRequest.getCity() == null || addressRequest.getCountry() == null || addressRequest.getPostalCode() == null || addressRequest.getStreet() == null) {
                return new ResponseEntity<>((new HashMap<String, String>() {{
                    put("Error", HttpStatus.BAD_REQUEST.getReasonPhrase());
                }}), HttpStatus.BAD_REQUEST);
            }
            if (addressRequest.getUser_id() != null) {
                int user_id = addressRequest.getUser_id();
                User foundUser = userRepository.findById(user_id);
                if (foundUser == null) throw new NotFoundException("user not found");
                user = foundUser;
            }
            Address address = new Address(
                    addressRequest.getStreet(),
                    addressRequest.getPostalCode(),
                    addressRequest.getCity(),
                    addressRequest.getCountry(),
                    user
            );
            addressRepository.save(address);
            return new ResponseEntity<Address>(address, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }}), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.NOT_FOUND.getReasonPhrase());
            }}), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/address/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Address requestAddress) {
        System.out.println(requestAddress);
        try {
            User user = authentificationHelpers.getUser();
            int idInteger = Integer.parseInt(id);
            if (requestAddress.getCity() == null && requestAddress.getCountry() == null && requestAddress.getPostalCode() == null && requestAddress.getStreet() == null) {
                throw new Exception("bad request");
            }
            Address address = addressRepository.findById(idInteger);
            if (address == null) throw new NotFoundException("Not found");
            userHasRight(user, address.getUser());
            if (requestAddress.getCity() != null) address.setCity(requestAddress.getCity());
            if (requestAddress.getStreet() != null) address.setStreet(requestAddress.getStreet());
            if (requestAddress.getCountry() != null) address.setCountry(requestAddress.getCountry());
            if (requestAddress.getPostalCode() != null) address.setPostalCode(requestAddress.getPostalCode());
            addressRepository.save(address);
            return new ResponseEntity<Address>(address, HttpStatus.OK);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>((new HashMap<String, String>() {{
                put("Error", HttpStatus.BAD_REQUEST.getReasonPhrase());
            }}), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try {
            User user = authentificationHelpers.getUser();
            int idInteger = Integer.parseInt(id);
            Address address = addressRepository.findById(idInteger);
            if (address == null) throw new NotFoundException("Not found");
            userHasRight(user, address.getUser());
            addressRepository.delete(address);
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
