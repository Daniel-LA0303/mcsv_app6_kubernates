package org.springcloud.mcsv.cours.mcsvcours.clients;


import org.springcloud.mcsv.cours.mcsvcours.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "mcsv-users", url = "mcsv-usersl:8081") //this annotation is to indicate the name of the microservice to communicate with mcsv-users
public interface UserClientRest {

    //we communicate with the microservice mcsv-users to get the user by id (as controller)
    @GetMapping("/{id}")
    User getUser(@PathVariable Long id);

    @PostMapping("/")
    User create(@RequestBody User user);

    @GetMapping("users-course")
    List<User> getUsersByCourse(@RequestParam Iterable<Long> ids);
}
