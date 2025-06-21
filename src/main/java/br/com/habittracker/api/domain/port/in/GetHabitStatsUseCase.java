package br.com.habittracker.api.domain.port.in;

import br.com.habittracker.api.domain.model.HabitStats;

@FunctionalInterface
public interface GetHabitStatsUseCase {
    HabitStats getStats(Long habitId);
}
