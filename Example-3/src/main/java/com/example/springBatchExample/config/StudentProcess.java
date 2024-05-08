package com.example.springBatchExample.config;

import com.example.springBatchExample.entity.Gender;
import com.example.springBatchExample.entity.Status;
import com.example.springBatchExample.entity.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentProcess implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student studentItem) throws Exception {
        final int id = studentItem.getId();
        final String firstname = studentItem.getFirstname();
        final String lastname = studentItem.getLastname();
        final int age = studentItem.getAge();
        final Gender gender = studentItem.getGender();
        final String dateOfBirthday = studentItem.getDateOfBirthday();
        final String email = studentItem.getEmail();
        final float score = studentItem.getScore();
        final Status status = studentItem.getStatus();

        return new Student(id, firstname, lastname, age, gender, dateOfBirthday, email, score, status);
    }

}
