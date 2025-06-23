package br.com.habittracker.api.domain.port.in;

import br.com.habittracker.api.domain.model.Habit;

import java.util.Map;

/**
 * Novo Use Case para buscar todos os hábitos e seu status de conclusão para hoje.
 * Retorna um Map para facilitar a consulta pelo status (true/false).
 */
@FunctionalInterface
public interface FindAllHabitsWithTodayStatusUseCase {
    Map<Habit, Boolean> findAllWithTodayStatus();
}
