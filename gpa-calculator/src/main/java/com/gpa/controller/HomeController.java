package com.gpa.controller;

import com.gpa.GPACalculatorApp;
import com.gpa.db.DatabaseManager;
import com.gpa.model.CalculationHistory;
import com.gpa.model.User;
import com.gpa.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

public class HomeController {

    @FXML private TextField loginRollField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Label loginStatusLabel;

    @FXML private TextField registerRollField;
    @FXML private TextField registerNameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private Label registerStatusLabel;

    @FXML private Label userDetailsLabel;
    @FXML private Button startCalculatorButton;
    @FXML private Button logoutButton;

    @FXML private TableView<CalculationHistory> historyTable;
    @FXML private TableColumn<CalculationHistory, String> dateColumn;
    @FXML private TableColumn<CalculationHistory, String> gpaColumn;
    @FXML private TableColumn<CalculationHistory, String> creditsColumn;

    private final ObservableList<CalculationHistory> historyItems = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        DatabaseManager.getInstance();
        if (historyTable != null) {
            historyTable.setItems(historyItems);
        }
        if (dateColumn != null) {
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("displayDate"));
        }
        if (gpaColumn != null) {
            gpaColumn.setCellValueFactory(new PropertyValueFactory<>("gpaText"));
        }
        if (creditsColumn != null) {
            creditsColumn.setCellValueFactory(new PropertyValueFactory<>("creditsText"));
        }
        updateUserSection();
    }

    @FXML
    private void handleStartButton() {
        if (!UserSession.isLoggedIn()) {
            showAlert("Login Required", "Please log in with your roll and password before starting the calculator.");
            return;
        }
        try {
            GPACalculatorApp.showCourseEntryScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        String roll = loginRollField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (roll.isEmpty() || password.isEmpty()) {
            showStatus(loginStatusLabel, "Roll and password are required.", false);
            return;
        }

        Optional<User> user = DatabaseManager.getInstance().authenticateUser(roll, password);
        if (user.isPresent()) {
            UserSession.setCurrentUser(user.get());
            loginRollField.clear();
            loginPasswordField.clear();
            updateUserSection();
            showStatus(loginStatusLabel, "Login successful! Click 'Start GPA Calculator' to begin.", true);
        } else {
            showStatus(loginStatusLabel, "Invalid roll or password.", false);
        }
    }

    @FXML
    private void handleRegister() {
        String roll = registerRollField.getText().trim();
        String name = registerNameField.getText().trim();
        String password = registerPasswordField.getText().trim();

        if (roll.isEmpty() || name.isEmpty() || password.isEmpty()) {
            showStatus(registerStatusLabel, "All registration fields are required.", false);
            return;
        }

        boolean created = DatabaseManager.getInstance().registerUser(roll, name, password);
        if (created) {
            registerRollField.clear();
            registerNameField.clear();
            registerPasswordField.clear();
            showStatus(registerStatusLabel, "Account created successfully! Please log in.", true);
        } else {
            showStatus(registerStatusLabel, "Registration failed. Roll may already exist.", false);
        }
    }

    @FXML
    private void handleLogout() {
        if (!UserSession.isLoggedIn()) {
            return;
        }
        UserSession.clear();
        showStatus(loginStatusLabel, "", true);
        updateUserSection();
    }

    private void updateUserSection() {
        User currentUser = UserSession.getCurrentUser();
        boolean loggedIn = currentUser != null;

        if (startCalculatorButton != null) {
            startCalculatorButton.setDisable(!loggedIn);
        }
        if (logoutButton != null) {
            logoutButton.setDisable(!loggedIn);
        }

        if (userDetailsLabel != null) {
            if (loggedIn) {
                userDetailsLabel.setText(String.format("Logged in as %s (Roll: %s)", currentUser.getName(), currentUser.getRoll()));
                loadHistory(currentUser.getRoll());
            } else {
                userDetailsLabel.setText("Please log in or register to continue.");
                historyItems.clear();
            }
        }
    }

    private void loadHistory(String roll) {
        List<CalculationHistory> history = DatabaseManager.getInstance().getCalculationHistory(roll);
        historyItems.setAll(history);
    }

    private void showStatus(Label label, String message, boolean success) {
        if (label == null) {
            return;
        }
        label.setText(message);
        label.setStyle(success ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #c0392b;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
