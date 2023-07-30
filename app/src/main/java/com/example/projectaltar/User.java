package com.example.projectaltar;

public class User {

    private String name;
    private String age;
    private String gender;

    public User() {
    }

    public User(String Name, String Age, String Gender) {
        this.name = Name;
        this.age = Age;
        this.gender = Gender;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

}

