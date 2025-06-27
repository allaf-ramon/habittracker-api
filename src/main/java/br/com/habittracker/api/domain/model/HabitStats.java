package br.com.habittracker.api.domain.model;

public class HabitStats {

    private int currentStreak;
    private int longestStreak;
    private double successRate;

    public HabitStats(int currentStreak, int longestStreak, double successRate) {
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.successRate = successRate;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public double getSuccessRate() {
        return successRate;
    }
}
