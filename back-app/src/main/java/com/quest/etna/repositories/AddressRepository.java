package com.quest.etna.repositories;

import com.quest.etna.model.Address;
import com.quest.etna.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {
    @Query("SELECT n FROM Address n WHERE n.id = :id")
    public Address findById(int id);

    @Query("SELECT n FROM Address n WHERE n.id = :id AND n.user = :user")
    public Address findByIdOfUser(int id, User user);

    @Query("SELECT n FROM Address n")
    public Collection<Address> findAll();

    @Query("SELECT n FROM Address n WHERE n.user = :user")
    public Collection<Address> findAllOfUser(User user);
}
