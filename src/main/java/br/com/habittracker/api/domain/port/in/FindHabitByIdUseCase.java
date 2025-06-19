package br.com.habittracker.api.domain.port.in;

import br.com.habittracker.api.domain.model.Habit;

import java.util.Optional;

@FunctionalInterface
public interface FindHabitByIdUseCase {
    Optional<Habit> findById(Long id);
}
