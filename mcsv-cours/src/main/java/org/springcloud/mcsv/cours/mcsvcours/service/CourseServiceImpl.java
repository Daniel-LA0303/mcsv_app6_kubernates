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
            Course course = optionalCourse.get(); //get the course
            User userMcsv = userClientRest.getUser(user.getId()); //we get the user from the microservice user

            //very important here: we need to do this validation
            boolean existsUser = course.getCourseUsers().stream().anyMatch(u -> u.getUserId().equals(userMcsv.getId()));
            if(existsUser){
                System.out.println("The user already exists in the course");
                return Optional.empty();
            }
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
            Course course = optionalCourse.get(); //get the course
            User userNewMcsv = userClientRest.create(user); //we create the user in the microservice user

            //Course course = optionalCourse.get(); //get the course
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
            System.out.println("userMcsv: " + userMcsv);
            Course course = optionalCourse.get(); //get the course

            CourseUser courseUserToRemove = course.getCourseUsers().stream()
                    .filter(u -> u.getUserId().equals(userMcsv.getId()))
                    .findFirst()
                    .orElse(null);

            if (courseUserToRemove != null) {
                course.removeUser(courseUserToRemove);
            }
            courseRepository.save(course);
            return Optional.of(userMcsv);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> byIdWithUsers(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        //validation
        if(optionalCourse.isPresent()){
            Course course = optionalCourse.get(); //get the course
            if(!course.getCourseUsers().isEmpty()){
                //we get the ids
                List<Long> ids = course.getCourseUsers().stream().map(u -> u.getUserId()).collect(java.util.stream.Collectors.toList());
                //we communicate with the microservice user to get the users
                List<User> users = userClientRest.getUsersByCourse(ids);
                course.setUsers(users); //we set the users to the course
            }
            return Optional.of(course);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteUserCourseById(Long id) {
        courseRepository.deleteCourseUserById(id);
    }
}
