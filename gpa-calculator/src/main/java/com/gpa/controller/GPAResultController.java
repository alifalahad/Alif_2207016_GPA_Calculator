package com.gpa.controller;

import com.gpa.GPACalculatorApp;
import com.gpa.model.Course;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class GPAResultController {

    @FXML private VBox coursesContainer;
    @FXML private Label gpaLabel;
    @FXML private Label totalCreditsLabel;

    public void setCourses(List<Course> courses, double totalCredits) {
        double totalPoints = 0.0;
        double totalCreditSum = 0.0;

        for (Course course : courses) {
            Label courseLabel = new Label();
            courseLabel.setText(String.format(
                "%-30s | Code: %-10s | Credits: %.1f | Grade: %-3s | Teachers: %s, %s",
                course.getCourseName(),
                course.getCourseCode(),
                course.getCourseCredit(),
                course.getGrade(),
                course.getTeacher1Name(),
                course.getTeacher2Name().isEmpty() ? "N/A" : course.getTeacher2Name()
            ));
            courseLabel.getStyleClass().add("course-item");
            coursesContainer.getChildren().add(courseLabel);

            totalPoints += course.getCourseCredit() * course.getGradePoint();
            totalCreditSum += course.getCourseCredit();
        }

        double gpa = totalPoints / totalCreditSum;
        gpaLabel.setText(String.format("%.2f", gpa));
        totalCreditsLabel.setText(String.format("%.1f", totalCreditSum));
    }

    @FXML
    private void handleBackToHome() {
        try {
            GPACalculatorApp.showHomeScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleNewCalculator() {
        try {
            GPACalculatorApp.showCourseEntryScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
