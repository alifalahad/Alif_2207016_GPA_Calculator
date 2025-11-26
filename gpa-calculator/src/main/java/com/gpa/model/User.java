package com.gpa.model;

public class User {

    private final String roll;
    private final String name;

    public User(String roll, String name) {
        this.roll = roll;
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }
}
