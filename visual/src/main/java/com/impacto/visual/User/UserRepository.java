package com.impacto.visual.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.persontype = :persontype, u.documenttype = :documenttype, " +
           "u.documentnumber = :documentnumber, u.firstname = :firstname, u.lastname = :lastname, " +
           "u.address = :address, u.email = :email, u.phone = :phone WHERE u.id = :id")
    void updateUser(@Param(value = "id") Integer id, 
                    @Param(value = "persontype") String persontype,
                    @Param(value = "documenttype") String documenttype, 
                    @Param(value = "documentnumber") String documentnumber,
                    @Param(value = "firstname") String firstname,
                    @Param(value = "lastname") String lastname, 
                    @Param(value = "address") String address,
                    @Param(value = "email") String email, 
                    @Param(value = "phone") String phone);
}