package org.springcloud.mcsv.users.mcsvusers.controller;

import org.springcloud.mcsv.users.mcsvusers.models.entity.User;
import org.springcloud.mcsv.users.mcsvusers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> list(){
        return userService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){

        Optional<User> user = userService.byId(id);
        if (user.isPresent()){
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult result){

        if (result.hasErrors()){
            return valid(result);
        }

        if(!user.getEmail().isEmpty() && userService.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body(Collections.singletonMap("email", "email already exists"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result,@PathVariable Long id){

        if (result.hasErrors()){
            return valid(result);
        }


        Optional<User> userOptional = userService.byId(id);
        if (userOptional.isPresent()){
            User userDb = userOptional.get();
            if(!user.getEmail().equalsIgnoreCase(userDb.getEmail()) && userService.findByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.badRequest().body(Collections.singletonMap("email", "email already exists"));
            }
            userDb.setName(user.getName());
            userDb.setEmail(user.getEmail());
            userDb.setPassword(user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userDb));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<User> userOptional = userService.byId(id);
        if (userOptional.isPresent()){
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> valid(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "the filed " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
