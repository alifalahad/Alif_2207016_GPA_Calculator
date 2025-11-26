package com.gpa.db;

import com.gpa.model.CalculationHistory;
import com.gpa.model.Course;
import com.gpa.model.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseManager {

    private static final DatabaseManager INSTANCE = new DatabaseManager();
    private final Path dbDirectory;
    private final String dbUrl;

    private DatabaseManager() {
        dbDirectory = Paths.get(System.getProperty("user.home"), ".gpa-calculator");
        dbUrl = "jdbc:sqlite:" + dbDirectory.resolve("gpa-calculator.db").toString();
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    public final void initializeDatabase() {
        try {
            if (!Files.exists(dbDirectory)) {
                Files.createDirectories(dbDirectory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                "roll TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ")");

            statement.execute("CREATE TABLE IF NOT EXISTS calculations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_roll TEXT NOT NULL," +
                "gpa REAL NOT NULL," +
                "total_credits REAL NOT NULL," +
                "calculated_at TEXT NOT NULL," +
                "FOREIGN KEY(user_roll) REFERENCES users(roll)" +
                ")");

            statement.execute("CREATE TABLE IF NOT EXISTS calculation_courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "calculation_id INTEGER NOT NULL," +
                "course_name TEXT NOT NULL," +
                "course_code TEXT NOT NULL," +
                "credits REAL NOT NULL," +
                "grade TEXT NOT NULL," +
                "teacher1 TEXT NOT NULL," +
                "teacher2 TEXT," +
                "FOREIGN KEY(calculation_id) REFERENCES calculations(id)" +
                ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String roll, String name, String password) {
        if (userExists(roll)) {
            return false;
        }
        
        String sql = "INSERT INTO users (roll, name, password) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roll);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean userExists(String roll) {
        String sql = "SELECT COUNT(*) FROM users WHERE roll = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roll);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<User> authenticateUser(String roll, String password) {
        String sql = "SELECT roll, name FROM users WHERE roll = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roll);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("roll"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void saveCalculation(User user, List<Course> courses) {
        if (user == null || courses == null || courses.isEmpty()) {
            return;
        }

        double totalCredits = 0.0;
        double totalPoints = 0.0;
        for (Course course : courses) {
            totalCredits += course.getCourseCredit();
            totalPoints += course.getCourseCredit() * course.getGradePoint();
        }
        double gpa = totalCredits == 0 ? 0.0 : totalPoints / totalCredits;

        String calculationSql = "INSERT INTO calculations (user_roll, gpa, total_credits, calculated_at) VALUES (?, ?, ?, ?)";
        String courseSql = "INSERT INTO calculation_courses (calculation_id, course_name, course_code, credits, grade, teacher1, teacher2) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            try {
                connection.setAutoCommit(false);
                long calculationId;

                try (PreparedStatement calculationStatement = connection.prepareStatement(calculationSql, Statement.RETURN_GENERATED_KEYS)) {
                    calculationStatement.setString(1, user.getRoll());
                    calculationStatement.setDouble(2, gpa);
                    calculationStatement.setDouble(3, totalCredits);
                    calculationStatement.setString(4, LocalDateTime.now().toString());
                    calculationStatement.executeUpdate();

                    try (ResultSet generatedKeys = calculationStatement.getGeneratedKeys()) {
                        calculationId = generatedKeys.next() ? generatedKeys.getLong(1) : -1L;
                    }
                }

                if (calculationId != -1) {
                    try (PreparedStatement courseStatement = connection.prepareStatement(courseSql)) {
                        for (Course course : courses) {
                            courseStatement.setLong(1, calculationId);
                            courseStatement.setString(2, course.getCourseName());
                            courseStatement.setString(3, course.getCourseCode());
                            courseStatement.setDouble(4, course.getCourseCredit());
                            courseStatement.setString(5, course.getGrade());
                            courseStatement.setString(6, course.getTeacher1Name());
                            courseStatement.setString(7, course.getTeacher2Name());
                            courseStatement.addBatch();
                        }
                        courseStatement.executeBatch();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CalculationHistory> getCalculationHistory(String roll) {
        List<CalculationHistory> history = new ArrayList<>();
        String sql = "SELECT id, gpa, total_credits, calculated_at FROM calculations WHERE user_roll = ? ORDER BY datetime(calculated_at) DESC";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roll);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                history.add(new CalculationHistory(
                    resultSet.getLong("id"),
                    resultSet.getString("calculated_at"),
                    resultSet.getDouble("gpa"),
                    resultSet.getDouble("total_credits")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }
}
