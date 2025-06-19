package br.com.habittracker.api.domain.service;

import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.in.CreateHabitUseCase;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;

import java.time.LocalDate;

/**
 * Implementação do caso de uso para criação de Hábitos.
 * Esta é a classe de serviço principal do nosso domínio.
 */
public class HabitService implements CreateHabitUseCase {
    private final HabitRepositoryPort habitRepositoryPort;

    // A implementação do repositório será injetada aqui (veremos isso depois)
    public HabitService(HabitRepositoryPort habitRepositoryPort) {
        this.habitRepositoryPort = habitRepositoryPort;
    }

    @Override
    public Habit createHabit(Habit habit) {
        // Regra de negócio: garantir que o nome não seja nulo ou vazio
        if (habit.getName() == null || habit.getName().isBlank()) {
            throw new IllegalArgumentException("O nome do hábito não pode ser vazio.");
        }

        // Regra de negócio: definir a data de criação no momento do cadastro
        habit.setCreationDate(LocalDate.now());

        // Delega a responsabilidade de salvar para a porta de saída
        return habitRepositoryPort.save(habit);
    }
}
