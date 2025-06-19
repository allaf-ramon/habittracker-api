package br.com.habittracker.api.domain.port.in;

import br.com.habittracker.api.domain.model.Habit;

import java.util.List;

@FunctionalInterface
public interface FindAllHabitsUseCase {
    List<Habit> findAll();
}
