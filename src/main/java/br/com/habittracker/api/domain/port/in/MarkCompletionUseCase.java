package br.com.habittracker.api.domain.port.in;

import java.time.LocalDate;

public interface MarkCompletionUseCase {
    void markAsCompleted(Long habitId, LocalDate date);
    void markAsNotCompleted(Long habitId, LocalDate date);
}
