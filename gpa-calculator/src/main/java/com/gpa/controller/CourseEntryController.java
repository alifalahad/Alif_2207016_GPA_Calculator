package com.gpa.controller;

import com.gpa.model.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class CourseEntryController {

    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeComboBox;
    @FXML private TextField totalCreditField;
    @FXML private Label currentCreditLabel;
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> nameColumn;
    @FXML private TableColumn<Course, String> codeColumn;
    @FXML private TableColumn<Course, Double> creditColumn;
    @FXML private TableColumn<Course, String> teacher1Column;
    @FXML private TableColumn<Course, String> teacher2Column;
    @FXML private TableColumn<Course, String> gradeColumn;
    @FXML private TableColumn<Course, Void> deleteColumn;
    @FXML private Button calculateGPAButton;

    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private double totalCreditsEntered = 0.0;
    private double requiredTotalCredits = 0.0;

    @FXML
    private void initialize() {
        gradeComboBox.setItems(FXCollections.observableArrayList(
            "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F"
        ));
        calculateGPAButton.setDisable(true);
        currentCreditLabel.setText("Current Credits: 0.0");
        
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        teacher1Column.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher2Column.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            
            {
                deleteButton.setOnAction(event -> {
                    Course course = getTableView().getItems().get(getIndex());
                    handleDeleteCourse(course);
                });
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        
        coursesTable.setItems(courses);
    }

    @FXML
    private void handleSetTotalCredits() {
        try {
            requiredTotalCredits = Double.parseDouble(totalCreditField.getText());
            if (requiredTotalCredits <= 0) {
                showAlert("Invalid Input", "Total credits must be positive!");
                return;
            }
            totalCreditField.setDisable(true);
            showAlert("Success", "Total credits set to: " + requiredTotalCredits);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for total credits!");
        }
    }

    @FXML
    private void handleAddCourse() {
        String courseName = courseNameField.getText().trim();
        String courseCode = courseCodeField.getText().trim();
        String creditText = courseCreditField.getText().trim();
        String teacher1 = teacher1Field.getText().trim();
        String teacher2 = teacher2Field.getText().trim();
        String grade = gradeComboBox.getValue();

        if (courseName.isEmpty() || courseCode.isEmpty() || creditText.isEmpty() || 
            teacher1.isEmpty() || grade == null) {
            showAlert("Missing Fields", "Please fill all required fields!");
            return;
        }

        if (requiredTotalCredits == 0) {
            showAlert("Set Total Credits", "Please set total credits first!");
            return;
        }

        try {
            double credit = Double.parseDouble(creditText);
            if (credit <= 0) {
                showAlert("Invalid Credit", "Course credit must be positive!");
                return;
            }

            if (totalCreditsEntered + credit > requiredTotalCredits) {
                showAlert("Credit Limit Exceeded", 
                    "Adding this course will exceed the total credit limit!");
                return;
            }

            Course course = new Course(courseName, courseCode, credit, teacher1, teacher2, grade);
            courses.add(course);
            totalCreditsEntered += credit;

            updateCreditLabel();
            clearFields();

            if (totalCreditsEntered >= requiredTotalCredits) {
                calculateGPAButton.setDisable(false);
            }

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for course credit!");
        }
    }

    @FXML
    private void handleCalculateGPA() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gpa/gpaResult.fxml"));
            Parent root = loader.load();

            GPAResultController resultController = loader.getController();
            resultController.setCourses(new ArrayList<>(courses), requiredTotalCredits);

            Stage stage = (Stage) calculateGPAButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 700);
            scene.getStylesheets().add(getClass().getResource("/com/gpa/style.css").toExternalForm());
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open GPA result screen!");
        }
    }

    private void handleDeleteCourse(Course course) {
        courses.remove(course);
        totalCreditsEntered -= course.getCourseCredit();
        updateCreditLabel();
        
        if (totalCreditsEntered < requiredTotalCredits) {
            calculateGPAButton.setDisable(true);
        }
    }

    private void updateCreditLabel() {
        currentCreditLabel.setText(String.format("Current Credits: %.1f / %.1f", 
            totalCreditsEntered, requiredTotalCredits));
    }

    private void clearFields() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeComboBox.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void handleBackToHome() {
        try {
            com.gpa.GPACalculatorApp.showHomeScreen();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to home screen!");
        }
    }
}
