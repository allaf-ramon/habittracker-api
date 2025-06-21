package br.com.habittracker.api.domain.model;

public class HabitStats {

    private int currentStreak;
    // Futuramente: private int longestStreak;
    // Futuramente: private double successRate;

    public HabitStats(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }
}
