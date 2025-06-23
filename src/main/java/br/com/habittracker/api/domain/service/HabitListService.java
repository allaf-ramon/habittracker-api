package br.com.habittracker.api.domain.service;

import br.com.habittracker.api.domain.model.Completion;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.in.FindAllHabitsWithTodayStatusUseCase;
import br.com.habittracker.api.domain.port.out.CompletionRepositoryPort;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Novo serviço dedicado à lógica de listagem.
 */
public class HabitListService implements FindAllHabitsWithTodayStatusUseCase {
    private final HabitRepositoryPort habitRepositoryPort;
    private final CompletionRepositoryPort completionRepositoryPort;

    public HabitListService(HabitRepositoryPort habitRepositoryPort,
                            CompletionRepositoryPort completionRepositoryPort) {
        this.habitRepositoryPort = habitRepositoryPort;
        this.completionRepositoryPort = completionRepositoryPort;
    }

    @Override
    public Map<Habit, Boolean> findAllWithTodayStatus() {
        // 1. Busca todos os hábitos
        List<Habit> allHabits = habitRepositoryPort.findAll();

        // 2. Busca os IDs dos hábitos concluídos hoje
        Set<Long> completedHabitIdsToday = completionRepositoryPort.findByCompletionDate(LocalDate.now())
                .stream()
                .map(Completion::getHabitId)
                .collect(Collectors.toSet());

        // 3. Retorna um mapa do Hábito para seu status booleano
        return allHabits.stream()
                .collect(Collectors.toMap(
                        Function.identity(), // A chave é o próprio objeto Habit
                        habit -> completedHabitIdsToday.contains(habit.getId()) // O valor é o status
                ));
    }
}
