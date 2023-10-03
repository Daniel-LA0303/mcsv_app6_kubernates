package org.springcloud.mcsv.cours.mcsvcours.controller;

import org.springcloud.mcsv.cours.mcsvcours.entity.Course;
import org.springcloud.mcsv.cours.mcsvcours.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> create(@RequestBody Course course){
        Course courseDB = courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Course course, @PathVariable Long id){
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
}
