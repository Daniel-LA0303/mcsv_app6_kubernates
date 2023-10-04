package org.springcloud.mcsv.cours.mcsvcours.service;

import org.springcloud.mcsv.cours.mcsvcours.models.User;
import org.springcloud.mcsv.cours.mcsvcours.models.entity.Course;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CourseService {

    //persintence methods
    List<Course> list();

    Optional<Course> byId(Long id);

    Course save(Course course);

    void delete(Long id);

    //this methods are for the table user_course
    Optional<User> asignedUser(User user, Long id);
    Optional<User> createUser(User user, Long id);
    Optional<User> deleteUser(User user, Long id);

    //with this method we get one list of users by one course
    Optional<Course> byIdWithUsers(Long id);

    //when delete one user this user deletes in the courses too
    void deleteUserCourseById(Long id);

}
