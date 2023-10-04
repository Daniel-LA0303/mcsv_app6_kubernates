package org.springcloud.mcsv.cours.mcsvcours.models.entity;

import org.springcloud.mcsv.cours.mcsvcours.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    //This is the relationship between courses and users
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    //          - if delete users, delete user of course also
    @JoinColumn(name = "course_id") //this is the name of the column in the table courses_users
    private List<CourseUser> courseUsers;

    @Transient //with this annotation, this field is not persisted in the database
    private List<User> users; //we need this field to show the users of the course in the view, the users we will get from another microservice

    public Course() {
        courseUsers = new ArrayList<>();
        users = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //------------------------------------------------------------
    //this method add a user to the course
    public void addUser(CourseUser courseUser) {
        courseUsers.add(courseUser);
    }

    //this method remove a user from the course
    public void removeUser(CourseUser courseUser) {
        courseUsers.remove(courseUser);
    }

    public List<CourseUser> getCourseUsers() {
        return courseUsers;
    }

    public void setCourseUsers(List<CourseUser> courseUsers) {
        this.courseUsers = courseUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
