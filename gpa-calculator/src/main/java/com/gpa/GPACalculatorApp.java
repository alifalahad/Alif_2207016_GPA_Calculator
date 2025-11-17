package com.gpa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GPACalculatorApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("GPA Calculator");
        showHomeScreen();
        primaryStage.show();
    }

    public static void showHomeScreen() throws Exception {
        Parent root = FXMLLoader.load(GPACalculatorApp.class.getResource("/com/gpa/home.fxml"));
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(GPACalculatorApp.class.getResource("/com/gpa/style.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void showCourseEntryScreen() throws Exception {
        Parent root = FXMLLoader.load(GPACalculatorApp.class.getResource("/com/gpa/courseEntry.fxml"));
        Scene scene = new Scene(root, 700, 650);
        scene.getStylesheets().add(GPACalculatorApp.class.getResource("/com/gpa/style.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
