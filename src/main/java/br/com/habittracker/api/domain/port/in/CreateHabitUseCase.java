package br.com.habittracker.api.domain.port.in;

import br.com.habittracker.api.domain.model.Habit;

/**
 * Porta de Entrada (Input Port) que define o contrato para o caso de uso
 * de criação de um novo Hábito.
 */
@FunctionalInterface // É uma boa prática, pois a interface tem apenas um método.
public interface CreateHabitUseCase {
    /**
     * Executa o caso de uso para criar e persistir um novo hábito.
     *
     * @param habit O objeto Hábito a ser criado, sem ID.
     * @return O objeto Hábito após ser salvo, agora com um ID.
     */
    Habit createHabit(Habit habit);
}
