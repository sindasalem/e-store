package com.quest.etna.service;

import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
