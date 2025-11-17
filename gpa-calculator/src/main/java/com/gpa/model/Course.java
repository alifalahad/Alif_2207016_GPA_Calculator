package com.gpa.model;

public class Course {
    private String courseName;
    private String courseCode;
    private double courseCredit;
    private String teacher1Name;
    private String teacher2Name;
    private String grade;

    public Course(String courseName, String courseCode, double courseCredit, 
                  String teacher1Name, String teacher2Name, String grade) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseCredit = courseCredit;
        this.teacher1Name = teacher1Name;
        this.teacher2Name = teacher2Name;
        this.grade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public double getCourseCredit() {
        return courseCredit;
    }

    public String getTeacher1Name() {
        return teacher1Name;
    }

    public String getTeacher2Name() {
        return teacher2Name;
    }

    public String getGrade() {
        return grade;
    }

    public double getGradePoint() {
        switch (grade) {
            case "A+": return 4.0;
            case "A":  return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B":  return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C":  return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D":  return 1.0;
            case "F":  return 0.0;
            default:   return 0.0;
        }
    }
}
