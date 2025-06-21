package br.com.habittracker.api.domain.service;

import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.out.CompletionRepositoryPort;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompletionServiceTest {
    @Mock
    private CompletionRepositoryPort completionRepositoryPort;

    @Mock
    private HabitRepositoryPort habitRepositoryPort;

    @InjectMocks
    private CompletionService completionService;

    @Test
    void givenValidHabit_whenMarkAsCompleted_thenSavesCompletion() {
        // Arrange
        Long habitId = 1L;
        LocalDate date = LocalDate.now();
        Habit habit = new Habit(habitId, "Test Habit", "", date.minusDays(1));

        when(habitRepositoryPort.findById(habitId)).thenReturn(Optional.of(habit));
        when(completionRepositoryPort.findByHabitIdAndDate(habitId, date)).thenReturn(Optional.empty());

        // Act
        completionService.markAsCompleted(habitId, date);

        // Assert
        verify(completionRepositoryPort).save(any());
    }

    @Test
    void givenNonExistentHabit_whenMarkAsCompleted_thenThrowsException() {
        // Arrange
        Long habitId = 99L;
        LocalDate date = LocalDate.now();

        when(habitRepositoryPort.findById(habitId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            completionService.markAsCompleted(habitId, date);
        });

        verify(completionRepositoryPort, never()).save(any());
    }

    @Test
    void givenAlreadyCompletedHabit_whenMarkAsCompleted_thenDoesNothing() {
        // Arrange
        Long habitId = 1L;
        LocalDate date = LocalDate.now();
        Habit habit = new Habit(habitId, "Test Habit", "", date.minusDays(1));

        when(habitRepositoryPort.findById(habitId)).thenReturn(Optional.of(habit));
        when(completionRepositoryPort.findByHabitIdAndDate(habitId, date))
                .thenReturn(Optional.of(new br.com.habittracker.api.domain.model.Completion(1L, habitId, date)));

        // Act
        completionService.markAsCompleted(habitId, date);

        // Assert
        verify(completionRepositoryPort, never()).save(any());
    }

    @Test
    void whenMarkAsNotCompleted_thenDeletesCompletion() {
        // Arrange
        Long habitId = 1L;
        LocalDate date = LocalDate.now();

        // Act
        completionService.markAsNotCompleted(habitId, date);

        // Assert
        verify(completionRepositoryPort).delete(habitId, date);
    }
}
