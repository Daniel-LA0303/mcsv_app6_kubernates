package org.springcloud.mcsv.users.mcsvusers.service;

import org.springcloud.mcsv.users.mcsvusers.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> list();

    Optional<User> byId(Long id);

    User save(User user);

    void delete(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
