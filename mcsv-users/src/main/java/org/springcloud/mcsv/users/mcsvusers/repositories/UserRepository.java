package org.springcloud.mcsv.users.mcsvusers.repositories;

import org.springcloud.mcsv.users.mcsvusers.models.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email); //validate the email

    @Query("select u from User u where u.email=?1")
    Optional<User> perEmail(String email);

    boolean existsByEmail(String email);

}
