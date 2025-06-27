package br.com.habittracker.api.domain.port.out;

import br.com.habittracker.api.domain.model.Completion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompletionRepositoryPort {
    void save(Completion completion);
    void delete(Long habitId, LocalDate date);
    Optional<Completion> findByHabitIdAndDate(Long habitId, LocalDate date);
    List<Completion> findByCompletionDate(LocalDate date);
    List<Completion> findByHabitIdOrderByCompletionDateDesc(Long habitId);
    void deleteAllByHabitId(Long habitId);
}
