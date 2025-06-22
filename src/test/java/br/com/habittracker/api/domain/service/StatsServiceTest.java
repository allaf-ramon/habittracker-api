package br.com.habittracker.api.domain.service;

import br.com.habittracker.api.domain.model.Completion;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.model.HabitStats;
import br.com.habittracker.api.domain.port.out.CompletionRepositoryPort;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private CompletionRepositoryPort completionRepositoryPort;

    @Mock
    private HabitRepositoryPort habitRepositoryPort;

    @InjectMocks
    private StatsService statsService;

    private final Long habitId = 1L;
    private final Habit habit = new Habit(habitId, "Test Habit", "", LocalDate.now().minusDays(10));

    @BeforeEach
    void setUp() {
        when(habitRepositoryPort.findById(habitId)).thenReturn(Optional.of(habit));
    }

    @Test
    void givenHabitWithNoCompletions_whenGetStats_thenReturnsStreakZero() {
        // Arrange
        when(completionRepositoryPort.findByHabitIdOrderByCompletionDateDesc(habitId)).thenReturn(Collections.emptyList());

        // Act
        HabitStats stats = statsService.getStats(habitId);

        // Assert
        assertEquals(0, stats.getCurrentStreak());
    }

    @Test
    void givenHabitCompletedToday_whenGetStats_thenReturnsStreakOne() {
        // Arrange
        LocalDate today = LocalDate.now();
        List<Completion> completions = List.of(new Completion(1L, habitId, today));
        when(completionRepositoryPort.findByHabitIdOrderByCompletionDateDesc(habitId)).thenReturn(completions);

        // Act
        HabitStats stats = statsService.getStats(habitId);

        // Assert
        assertEquals(1, stats.getCurrentStreak());
    }

    @Test
    void givenHabitCompletedTodayAndYesterday_whenGetStats_thenReturnsStreakTwo() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        List<Completion> completions = List.of(
                new Completion(2L, habitId, today),
                new Completion(1L, habitId, yesterday)
        );
        when(completionRepositoryPort.findByHabitIdOrderByCompletionDateDesc(habitId)).thenReturn(completions);

        // Act
        HabitStats stats = statsService.getStats(habitId);

        // Assert
        assertEquals(2, stats.getCurrentStreak());
    }

    @Test
    void givenHabitCompletedOnlyYesterday_whenGetStats_thenReturnsStreakOne() {
        // Arrange
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Completion> completions = List.of(new Completion(1L, habitId, yesterday));
        when(completionRepositoryPort.findByHabitIdOrderByCompletionDateDesc(habitId)).thenReturn(completions);

        // Act
        HabitStats stats = statsService.getStats(habitId);

        // Assert
        assertEquals(1, stats.getCurrentStreak());
    }

    @Test
    void givenHabitCompletedDayBeforeYesterdayButNotYesterday_whenGetStats_thenReturnsStreakZero() {
        // Arrange
        LocalDate dayBeforeYesterday = LocalDate.now().minusDays(2);
        List<Completion> completions = List.of(new Completion(1L, habitId, dayBeforeYesterday));
        when(completionRepositoryPort.findByHabitIdOrderByCompletionDateDesc(habitId)).thenReturn(completions);

        // Act
        HabitStats stats = statsService.getStats(habitId);

        // Assert
        assertEquals(0, stats.getCurrentStreak());
    }

    @Test
    void givenHabitWithLongStreak_whenGetStats_thenReturnsCorrectStreak() {
        // Arrange
        LocalDate today = LocalDate.now();
        List<Completion> completions = List.of(
                new Completion(5L, habitId, today),
                new Completion(4L, habitId, today.minusDays(1)),
                new Completion(3L, habitId, today.minusDays(2)),
                new Completion(2L, habitId, today.minusDays(3)),
                new Completion(1L, habitId, today.minusDays(4))
        );
        when(completionRepositoryPort.findByHabitIdOrderByCompletionDateDesc(habitId)).thenReturn(completions);

        // Act
        HabitStats stats = statsService.getStats(habitId);

        // Assert
        assertEquals(5, stats.getCurrentStreak());
    }

    @Test
    void givenHabitWithBrokenStreak_whenGetStats_thenReturnsCorrectStreak() {
        // Arrange
        LocalDate today = LocalDate.now();
        List<Completion> completions = List.of(
                new Completion(5L, habitId, today),
                new Completion(4L, habitId, today.minusDays(1)),
                // Gap of one day
                new Completion(3L, habitId, today.minusDays(3)),
                new Completion(2L, habitId, today.minusDays(4)),
                new Completion(1L, habitId, today.minusDays(5))
        );
        when(completionRepositoryPort.findByHabitIdOrderByCompletionDateDesc(habitId)).thenReturn(completions);

        // Act
        HabitStats stats = statsService.getStats(habitId);

        // Assert
        assertEquals(2, stats.getCurrentStreak());
    }
}
