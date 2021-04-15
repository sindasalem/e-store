package com.quest.etna;

import com.jayway.jsonpath.JsonPath;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.Address;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(AuthenticationController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    @Order(1)
    void testAuthenticate() throws Exception {
        // delete the data used in the test if they exists
        User user = userRepository.findByUsername("yanis");
        if (user != null) userRepository.delete(user);
        String userJson = "{\"username\": \"yanis\", \"password\": \"yanisPassword\"}";

        // register returns created
        mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(userJson)
        ).andDo(print()).andExpect(status().isCreated());

        // register existing user returns conflict
        mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(userJson)
        ).andDo(print()).andExpect(status().isConflict());

        // authenticate is ok and returns token
        MvcResult authenticateTest = mockMvc.perform(
                post("/authenticate").contentType(MediaType.APPLICATION_JSON).content(userJson)
        ).andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.token").isString())
                .andReturn();
        String token = JsonPath.read(authenticateTest.getResponse().getContentAsString(), "$.token");

        // me is ok and returns user details
        mockMvc.perform(
                get("/me").contentType(MediaType.APPLICATION_JSON).content(userJson).header("Authorization", "Bearer " + token)
        ).andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("yanis"))
                .andExpect(jsonPath("$.accountNonLocked").value(true))
                .andExpect(jsonPath("$.credentialsNonExpired").value(true))
                .andExpect(jsonPath("$.accountNonExpired").value(true))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.authorities").isArray());
    }

    @Test
    @Order(2)
    void testUser() throws Exception {
        // delete the data used in the test if they exists
        User user = userRepository.findByUsername("yanis");
        if (user != null) userRepository.delete(user);
        user = userRepository.findByUsername("hichem");
        if (user != null) userRepository.delete(user);
        String username = "yanis";

        User newUser = new User(username, "yanisPassword");
        userRepository.save(newUser);
        User otherUser = new User("hichem", "hichemPassword");
        userRepository.save(otherUser);

        // unauthorized fetch without token
        mockMvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnauthorized());

        // fetch ok with token
        String token = getToken(username);
        mockMvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON).header("Authorization", token)).andDo(print()).andExpect(status().isOk());
        // unauthorized delete if user role is ROLE_USER
        mockMvc.perform(
                delete("/user/" + otherUser.getId()).header("Authorization", token)
        ).andDo(print()).andExpect(status().isUnauthorized());

        // change role of user and delete is ok
        newUser.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(newUser);
        token = getToken(username);
        mockMvc.perform(
                delete("/user/" + otherUser.getId()).header("Authorization", token)
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void testAddress() throws Exception {
        // delete the data used in the test if they exists
        User user = userRepository.findByUsername("yanis");
        if (user != null) {
            Collection<Address> addresses = addressRepository.findAllOfUser(user);
            for (Address address : addresses) {
                addressRepository.delete(address);
            }
            userRepository.delete(user);
        }
        user = userRepository.findByUsername("hichem");
        if (user != null) {
            Collection<Address> addresses = addressRepository.findAllOfUser(user);
            for (Address address : addresses) {
                addressRepository.delete(address);
            }
            userRepository.delete(user);
        }

        String username = "yanis";
        User newUser = new User(username, "yanisPassword");
        userRepository.save(newUser);
        Address newAddress = new Address("street", "postalCode", "city", "country", newUser);
        addressRepository.save(newAddress);

        User otherUser = new User("hichem", "hichemPassword");
        userRepository.save(otherUser);
        Address otherAddress = new Address("street_2", "postalCode_2", "city_2", "country_2", otherUser);
        addressRepository.save(otherAddress);

        // unauthorized fetch without token
        mockMvc.perform(get("/address").contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnauthorized());

        // fetch ok with token
        String token = getToken(username);
        mockMvc.perform(get("/address").contentType(MediaType.APPLICATION_JSON).header("Authorization", token)).andDo(print()).andExpect(status().isOk());
        // add address is ok
        String addressJson = "{\"street\": \"street_added\", \"postalCode\": \"postalCode_added\", \"city\": \"city_added\", \"country\": \"country_added\"}";
        mockMvc.perform(
                post("/address").contentType(MediaType.APPLICATION_JSON).content(addressJson).header("Authorization", token)
        ).andDo(print()).andExpect(status().isCreated());
        // unauthorized delete for another user address
        mockMvc.perform(
                delete("/address/" + otherAddress.getId()).header("Authorization", token)
        ).andDo(print()).andExpect(status().isUnauthorized());

        // change role of user and it delete another user address is ok
        newUser.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(newUser);
        token = getToken(username);
        mockMvc.perform(
                delete("/address/" + otherAddress.getId()).header("Authorization", token)
        ).andDo(print()).andExpect(status().isOk());
    }

    private String getToken(String username) {
        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(username);
        return "Bearer " + jwtTokenUtil.generateToken(jwtUserDetails);
    }

}
