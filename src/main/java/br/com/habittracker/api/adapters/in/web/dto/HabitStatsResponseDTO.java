package br.com.habittracker.api.adapters.in.web.dto;

public class HabitStatsResponseDTO {
    private int currentStreak;
    private int longestStreak;

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
}
