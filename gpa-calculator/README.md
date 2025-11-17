<<<<<<< HEAD
# Alif_2207016_GPA_Calculator
=======
# GPA Calculator

A JavaFX-based Student Grading System (GPA Calculator) application based on the requirements in GG.txt.

## Features

### Home Screen
- Welcome message with "Start GPA Calculator" button

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

1. **Home Screen**: Click "Start GPA Calculator"
2. **Set Total Credits**: Enter the total credits required (e.g., 15) and click "Set"
3. **Add Courses**: Fill in course details and click "Add Course" for each course
4. **Calculate GPA**: Once total credits are met, click "Calculate GPA"
5. **View Results**: See your GPA displayed in a certificate-style format
6. **Return Home**: Click "Back to Home" to start over

## Design Guidelines Followed
- Clean FXML + Controller structure
- Course model class for separation of concerns
- BorderPane, GridPane, VBox, HBox layouts
- CSS styling for visual appeal
- Simple Scene Builder-style design
- Input validation and user feedback
- Disabled GPA button until credits requirement met
>>>>>>> 638ce74 (gpa calculator project)
