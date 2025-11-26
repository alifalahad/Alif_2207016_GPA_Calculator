# GPA Calculator

A JavaFX-based Student Grading System (GPA Calculator) application based on the requirements in GG.txt.

## Features

### Home Screen
- Register a new account with roll number, name, and password
- Log in with roll + password to unlock the calculator
- View previously saved GPA calculations for the logged in student
- Start the GPA calculator or log out of the active session

### Persistent Accounts & History
- SQLite database stored at `~/.gpa-calculator/gpa-calculator.db` keeps user credentials secure on the local machine
- Every GPA calculation saves the entered courses, credits, and GPA under the authenticated roll number for future viewing

### Course Entry Screen
- Set total required credits
- Add courses with:
  - Course Name
  - Course Code
  - Course Credit
  - Teacher 1 Name
  - Teacher 2 Name (optional)
  - Grade (A+, A, A-, B+, B, B-, C+, C, C-, D+, D, F)
- Real-time credit tracking
- "Calculate GPA" button activates when required credits are met

### GPA Result Screen
- Certificate-style award form layout
- Displays calculated GPA
- Shows all course details
- Back to Home button

## Grade Scale
- A+/A: 4.0
- A-: 3.7
- B+: 3.3
- B: 3.0
- B-: 2.7
- C+: 2.3
- C: 2.0
- C-: 1.7
- D+: 1.3
- D: 1.0
- F: 0.0

## Project Structure
```
gpa-calculator/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── gpa/
│       │           ├── GPACalculatorApp.java (Main Application)
│       │           ├── model/
│       │           │   └── Course.java (Course Model)
│       │           └── controller/
│       │               ├── HomeController.java
│       │               ├── CourseEntryController.java
│       │               └── GPAResultController.java
│       └── resources/
│           └── com/
│               └── gpa/
│                   ├── home.fxml
│                   ├── courseEntry.fxml
│                   ├── gpaResult.fxml
│                   └── style.css
└── pom.xml
```

## How to Run

### Prerequisites
- Java 11 or higher
- Maven

### Run Command
```bash
cd gpa-calculator
mvn clean javafx:run
```

## Usage Instructions

1. **Home Screen**: Register with your roll, name, and password or log in with an existing account.
2. **Start Calculator**: Once authenticated, click "Start GPA Calculator" to enter courses.
3. **Set Total Credits**: Enter the required credits (e.g., 15) and click "Set".
4. **Add Courses**: Fill in course details and click "Add Course" until you reach the required credits.
5. **Calculate GPA**: Click "Calculate GPA" to view results; the calculation is automatically stored for the logged in user.
6. **View History**: Return to the home screen to review your saved GPA history or start another calculation.

## Design Guidelines Followed
- Clean FXML + Controller structure
- Course model class for separation of concerns
- BorderPane, GridPane, VBox, HBox layouts
- CSS styling for visual appeal
- Simple Scene Builder-style design
- Input validation and user feedback
- Disabled GPA button until credits requirement met
