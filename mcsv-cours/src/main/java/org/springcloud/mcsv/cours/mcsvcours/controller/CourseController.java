package org.springcloud.mcsv.cours.mcsvcours.controller;

import org.springcloud.mcsv.cours.mcsvcours.entity.Course;
import org.springcloud.mcsv.cours.mcsvcours.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private static ResponseEntity<Map<String, String>> valid(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "the filed " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
