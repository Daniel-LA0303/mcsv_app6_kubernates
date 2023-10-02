package org.springcloud.mcsv.users.mcsvusers.repositories;

import org.springcloud.mcsv.users.mcsvusers.models.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
