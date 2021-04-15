package com.quest.etna.repositories;

import com.quest.etna.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT n FROM User n WHERE n.username = :username")
    public User findByUsername(String username);

    @Query("SELECT n FROM User n WHERE n.id = :id")
    public User findById(int id);

    @Query("SELECT n FROM User n")
    public Collection<User> findAll();
}
