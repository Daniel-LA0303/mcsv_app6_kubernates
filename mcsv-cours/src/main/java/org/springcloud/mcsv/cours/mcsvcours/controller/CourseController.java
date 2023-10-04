package org.springcloud.mcsv.cours.mcsvcours.controller;

import feign.FeignException;
import org.springcloud.mcsv.cours.mcsvcours.models.User;
import org.springcloud.mcsv.cours.mcsvcours.models.entity.Course;
import org.springcloud.mcsv.cours.mcsvcours.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> list(){
        return ResponseEntity.ok(courseService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable  Long id){
        Optional<Course> optionalCourse = courseService.byId(id);
        if(!optionalCourse.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optionalCourse.get());
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody Course course, BindingResult result){
        if (result.hasErrors()){
            return valid(result);
        }

        Course courseDB = courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Course course, BindingResult result, @PathVariable Long id){

        if (result.hasErrors()){
            return valid(result);
        }

        Optional<Course> optionalCourse = courseService.byId(id);
        if(!optionalCourse.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Course courseDB = optionalCourse.get();
        courseDB.setName(course.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseDB));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Course> optionalCourse = courseService.byId(id);
        if(optionalCourse.isPresent()){
            courseService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    //asigned user to course
    @PutMapping("/asigned-user/{courseId}")
    public ResponseEntity<?> asignedUser(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> optionalUser = null;
        try{
            optionalUser = courseService.asignedUser(user, courseId);
        }catch(FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "user not found" + e.getMessage()));
        }
        if(optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crate-user/{courseId}")
    public ResponseEntity<?> creeUser(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> optionalUser = null;
        try{
            optionalUser = courseService.createUser(user, courseId);
        }catch(FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "We do not create the user by a error" + e.getMessage()));
        }
        if(optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-user/{courseId}")
    public ResponseEntity<?> deleteUser(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> optionalUser;
        try{
            optionalUser = courseService.deleteUser(user, courseId);

        }catch(FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "We do not delete the user by a error" + e.getMessage()));
        }
        if(optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get());
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
