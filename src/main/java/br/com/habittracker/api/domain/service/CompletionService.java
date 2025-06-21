package br.com.habittracker.api.domain.service;

import br.com.habittracker.api.domain.model.Completion;
import br.com.habittracker.api.domain.port.in.MarkCompletionUseCase;
import br.com.habittracker.api.domain.port.out.CompletionRepositoryPort;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;

import java.time.LocalDate;

public class CompletionService implements MarkCompletionUseCase {
    private final CompletionRepositoryPort completionRepositoryPort;
    private final HabitRepositoryPort habitRepositoryPort; // Precisamos validar se o hábito existe

    public CompletionService(CompletionRepositoryPort completionRepositoryPort, HabitRepositoryPort habitRepositoryPort) {
        this.completionRepositoryPort = completionRepositoryPort;
        this.habitRepositoryPort = habitRepositoryPort;
    }

    @Override
    public void markAsCompleted(Long habitId, LocalDate date) {
        // Regra: Não pode marcar conclusão para um hábito que não existe.
        habitRepositoryPort.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Hábito não encontrado."));

        // Regra: Não marcar como concluído se já estiver marcado.
        if (completionRepositoryPort.findByHabitIdAndDate(habitId, date).isPresent()) {
            return; // Já existe, não faz nada.
        }

        Completion completion = new Completion(null, habitId, date);
        completionRepositoryPort.save(completion);
    }

    @Override
    public void markAsNotCompleted(Long habitId, LocalDate date) {
        // Validação da existência do hábito é implícita aqui.
        // Se a conclusão existe, o hábito também existe.
        completionRepositoryPort.delete(habitId, date);
    }
}
