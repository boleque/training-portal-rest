package com.tallink.trainings.repository;

import com.tallink.trainings.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
