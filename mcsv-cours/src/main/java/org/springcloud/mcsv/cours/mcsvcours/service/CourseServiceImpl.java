package org.springcloud.mcsv.cours.mcsvcours.service;

import org.springcloud.mcsv.cours.mcsvcours.clients.UserClientRest;
import org.springcloud.mcsv.cours.mcsvcours.models.User;
import org.springcloud.mcsv.cours.mcsvcours.models.entity.Course;
import org.springcloud.mcsv.cours.mcsvcours.models.entity.CourseUser;
import org.springcloud.mcsv.cours.mcsvcours.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserClientRest userClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Course> list() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> byId(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    //this methods are for the table user_course
    @Override
    @Transactional
    public Optional<User> asignedUser(User user, Long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);
        //validation
        if(optionalCourse.isPresent()){
            User userMcsv = userClientRest.getUser(user.getId()); //we get the user from the microservice user

            Course course = optionalCourse.get(); //get the course
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMcsv.getId()); //we add the user id at the courseUser

            course.addUser(courseUser); //we add the user to the course
            courseRepository.save(course);
            return Optional.of(userMcsv);
        }


        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> createUser(User user, Long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);
        //validation
        if(optionalCourse.isPresent()){
            User userNewMcsv = userClientRest.create(user); //we create the user in the microservice user

            Course course = optionalCourse.get(); //get the course
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userNewMcsv.getId()); //we add the user id at the courseUser

            course.addUser(courseUser); //we add the user to the course
            courseRepository.save(course);
            return Optional.of(userNewMcsv);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> deleteUser(User user, Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        //validation
        if(optionalCourse.isPresent()){
            User userMcsv = userClientRest.getUser(user.getId()); //we get the user from the microservice user

            Course course = optionalCourse.get(); //get the course
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMcsv.getId()); //we add the user id at the courseUser

            course.removeUser(courseUser); //we add the user to the course
            courseRepository.save(course);
            return Optional.of(userMcsv);
        }
        return Optional.empty();
    }
}
