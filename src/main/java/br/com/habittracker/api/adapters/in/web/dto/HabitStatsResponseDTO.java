package br.com.habittracker.api.adapters.in.web.dto;

public class HabitStatsResponseDTO {
    private int currentStreak;

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
}
