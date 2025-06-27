package br.com.habittracker.api.domain.service;

import br.com.habittracker.api.domain.model.Completion;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.model.HabitStats;
import br.com.habittracker.api.domain.port.in.GetHabitStatsUseCase;
import br.com.habittracker.api.domain.port.out.CompletionRepositoryPort;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatsService implements GetHabitStatsUseCase {

    private final CompletionRepositoryPort completionRepositoryPort;
    private final HabitRepositoryPort habitRepositoryPort;

    public StatsService(CompletionRepositoryPort completionRepositoryPort, HabitRepositoryPort habitRepositoryPort) {
        this.completionRepositoryPort = completionRepositoryPort;
        this.habitRepositoryPort = habitRepositoryPort;
    }

    @Override
    public HabitStats getStats(Long habitId) {
        // Valida se o hábito existe
        Habit habit = habitRepositoryPort.findById(habitId)
                .orElseThrow(() -> new IllegalArgumentException("Hábito não encontrado."));

        List<Completion> completions = completionRepositoryPort.findByHabitIdOrderByCompletionDateDesc(habitId);
        int currentStreak = calculateCurrentStreak(completions);
        int longestStreak = calculateLongestStreak(completions);
        double successRate = calculateSuccessRate(habit, completions);

        return new HabitStats(currentStreak, longestStreak, successRate);
    }

    private int calculateCurrentStreak(List<Completion> completions) {
        if (completions.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate today = LocalDate.now();
        LocalDate expectedDate = today;

        // Verifica se a última conclusão foi hoje ou ontem. Se não, a sequência é 0.
        LocalDate lastCompletionDate = completions.get(0).getCompletionDate();
        if (!lastCompletionDate.equals(today) && !lastCompletionDate.equals(today.minusDays(1))) {
            return 0;
        }

        // Se a última conclusão não foi hoje, começamos a contar a partir de ontem.
        if (!lastCompletionDate.equals(today)) {
            expectedDate = today.minusDays(1);
        }

        // Itera sobre as conclusões para contar os dias consecutivos
        for (Completion completion : completions) {
            if (completion.getCompletionDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                // A sequência foi quebrada antes desta conclusão
                break;
            }
        }

        return streak;
    }

    private int calculateLongestStreak(List<Completion> completionsDesc) {
        if (completionsDesc.isEmpty()) {
            return 0;
        }

        // Invertemos a lista para iterar do mais antigo para o mais recente
        List<Completion> completionsAsc = new ArrayList<>(completionsDesc);
        Collections.reverse(completionsAsc);

        int longestStreak = 0;
        int currentStreak = 0;
        LocalDate lastDate = null;

        for (Completion completion : completionsAsc) {
            LocalDate currentDate = completion.getCompletionDate();
            if (lastDate != null && currentDate.equals(lastDate.plusDays(1))) {
                // Dia consecutivo, incrementa a sequência atual
                currentStreak++;
            } else {
                // Dia não consecutivo (ou o primeiro da lista), reinicia a sequência
                currentStreak = 1;
            }

            if (currentStreak > longestStreak) {
                longestStreak = currentStreak;
            }

            lastDate = currentDate;
        }
        return longestStreak;
    }

    private double calculateSuccessRate(Habit habit, List<Completion> completions) {
        if (completions.isEmpty()) {
            return 0.0;
        }

        long daysSinceCreation = ChronoUnit.DAYS.between(habit.getCreationDate(), LocalDate.now()) + 1;

        // Caso de borda: se o hábito foi criado no futuro (não deveria acontecer)
        if (daysSinceCreation <= 0) {
            return 0.0;
        }

        return ((double) completions.size() / daysSinceCreation) * 100.0;
    }
}
