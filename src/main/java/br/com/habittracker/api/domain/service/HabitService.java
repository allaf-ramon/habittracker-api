package br.com.habittracker.api.domain.service;

import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.in.*;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do caso de uso para criação de Hábitos.
 * Esta é a classe de serviço principal do nosso domínio.
 */
public class HabitService
        implements CreateHabitUseCase, FindHabitByIdUseCase, FindAllHabitsUseCase,
        UpdateHabitUseCase, DeleteHabitUseCase {
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

    @Override
    public Optional<Habit> findById(Long id) {
        return habitRepositoryPort.findById(id);
    }

    @Override
    public List<Habit> findAll() {
        return habitRepositoryPort.findAll();
    }

    @Override
    public Optional<Habit> updateHabit(Long id, Habit habitToUpdate) {
        // Regra de negócio: garantir que o nome não seja nulo ou vazio
        if (habitToUpdate.getName() == null || habitToUpdate.getName().isBlank()) {
            throw new IllegalArgumentException("O nome do hábito não pode ser vazio.");
        }

        return habitRepositoryPort.findById(id)
                .map(existingHabit -> {
                    // Atualiza os campos do hábito existente
                    existingHabit.setName(habitToUpdate.getName());
                    existingHabit.setDescription(habitToUpdate.getDescription());

                    // Salva o hábito atualizado usando a mesma porta 'save'
                    return habitRepositoryPort.save(existingHabit);
                });
    }

    @Override
    public boolean deleteHabit(Long id) {
        // Verifica se o hábito existe
        if (habitRepositoryPort.findById(id).isPresent()) {
            habitRepositoryPort.deleteById(id);
            return true; // Sucesso
        }
        return false; // Hábito não encontrado
    }
}
