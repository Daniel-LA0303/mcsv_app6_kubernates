package org.springcloud.mcsv.cours.mcsvcours.service;

import org.springcloud.mcsv.cours.mcsvcours.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> list();

    Optional<Course> byId(Long id);

    Course save(Course course);

    void delete(Long id);
}
