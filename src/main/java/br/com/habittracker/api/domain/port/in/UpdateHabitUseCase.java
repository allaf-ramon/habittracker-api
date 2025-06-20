package br.com.habittracker.api.domain.port.in;

import br.com.habittracker.api.domain.model.Habit;

import java.util.Optional;

@FunctionalInterface
public interface UpdateHabitUseCase {
    /**
     * Atualiza um hábito existente.
     * @param id O ID do hábito a ser atualizado.
     * @param habitToUpdate O objeto Hábito com as novas informações.
     * @return Um Optional contendo o hábito atualizado, ou vazio se o hábito não for encontrado.
     */
    Optional<Habit> updateHabit(Long id, Habit habitToUpdate);
}
