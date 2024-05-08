package com.example.springBatchExample.entity;

public class Student {

    private int id;
    private String firstname;
    private String lastname;
    private int age;
    private Gender gender;
    private String dateOfBirthday;
    private String email;
    private float score;
    private Status status;

    public Student() {
    }

    public Student(int id, String firstname, String lastname, int age, Gender gender, String dateOfBirthday, String email, float score, Status status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.gender = gender;
        this.dateOfBirthday = dateOfBirthday;
        this.email = email;
        this.score = score;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", dateOfBirthday='" + dateOfBirthday + '\'' +
                ", email='" + email + '\'' +
                ", score=" + score +
                ", status=" + status +
                '}';
    }
}
