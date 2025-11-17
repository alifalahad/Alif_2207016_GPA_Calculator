package com.gpa.controller;

import com.gpa.GPACalculatorApp;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void handleStartButton() {
        try {
            GPACalculatorApp.showCourseEntryScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
