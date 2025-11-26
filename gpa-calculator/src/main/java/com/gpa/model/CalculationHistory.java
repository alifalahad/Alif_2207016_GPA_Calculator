package com.gpa.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalculationHistory {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    private final long id;
    private final String calculatedAt;
    private final double gpa;
    private final double totalCredits;

    public CalculationHistory(long id, String calculatedAt, double gpa, double totalCredits) {
        this.id = id;
        this.calculatedAt = calculatedAt;
        this.gpa = gpa;
        this.totalCredits = totalCredits;
    }

    public long getId() {
        return id;
    }

    public String getCalculatedAt() {
        return calculatedAt;
    }

    public double getGpa() {
        return gpa;
    }

    public double getTotalCredits() {
        return totalCredits;
    }

    public String getDisplayDate() {
        try {
            return LocalDateTime.parse(calculatedAt).format(DISPLAY_FORMAT);
        } catch (Exception e) {
            return calculatedAt == null ? "Unknown" : calculatedAt;
        }
    }

    public String getGpaText() {
        return String.format("%.2f", gpa);
    }

    public String getCreditsText() {
        return String.format("%.1f", totalCredits);
    }
}
