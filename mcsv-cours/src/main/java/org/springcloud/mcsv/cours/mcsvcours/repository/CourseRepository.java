package org.springcloud.mcsv.cours.mcsvcours.repository;

import org.springcloud.mcsv.cours.mcsvcours.models.entity.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Modifying
    @Query("delete from CourseUser cu where cu.userId=?1")
    public void deleteCourseUserById(Long id);

}
