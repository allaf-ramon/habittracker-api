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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {
    @Mock
    private HabitRepositoryPort habitRepositoryPort;

    @Mock
    private CompletionRepositoryPort completionRepositoryPort;

    @InjectMocks
    private HabitService habitService;

    @Test
    void givenValidHabit_whenCreateHabit_thenSetsCreationDateAndSaves() {
        // Arrange
        Habit habitToCreate = new Habit();
        habitToCreate.setName("Test Habit");

        when(habitRepositoryPort.save(any(Habit.class))).thenAnswer(invocation -> {
            Habit savedHabit = invocation.getArgument(0);
            savedHabit.setId(1L); // Simula o ID sendo gerado no banco
            return savedHabit;
        });

        // Act
        Habit createdHabit = habitService.createHabit(habitToCreate);

        // Assert
        assertNotNull(createdHabit);
        assertEquals(1L, createdHabit.getId());
        assertEquals("Test Habit", createdHabit.getName());
        assertEquals(LocalDate.now(), createdHabit.getCreationDate());
        verify(habitRepositoryPort).save(habitToCreate); // Verifica se o método save foi chamado
    }

    @Test
    void givenHabitWithBlankName_whenCreateHabit_thenThrowsException() {
        // Arrange
        Habit habitWithBlankName = new Habit();
        habitWithBlankName.setName(" ");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> habitService.createHabit(habitWithBlankName)
        );

        assertEquals("O nome do hábito não pode ser vazio.", exception.getMessage());
    }

    @Test
    void whenFindById_thenReturnsHabitOptional() {
        // Arrange
        Habit habit = new Habit(1L, "Read book", "description", LocalDate.now());
        when(habitRepositoryPort.findById(1L)).thenReturn(Optional.of(habit));

        // Act
        Optional<Habit> foundHabit = habitService.findById(1L);

        // Assert
        assertTrue(foundHabit.isPresent());
        assertEquals(1L, foundHabit.get().getId());
    }

    @Test
    void whenFindAll_thenReturnsHabitList() {
        // Arrange
        List<Habit> habits = List.of(new Habit(1L, "Habit 1", "d1", LocalDate.now()));
        when(habitRepositoryPort.findAll()).thenReturn(habits);

        // Act
        List<Habit> foundHabits = habitService.findAll();

        // Assert
        assertFalse(foundHabits.isEmpty());
        assertEquals(1, foundHabits.size());
    }

    @Test
    void givenExistingHabit_whenUpdateHabit_thenSavesAndReturnsUpdatedHabit() {
        // Arrange
        Habit existingHabit = new Habit(1L, "Old Name", "Old Desc", LocalDate.now());
        Habit habitWithNewData = new Habit();
        habitWithNewData.setName("New Name");
        habitWithNewData.setDescription("New Desc");

        when(habitRepositoryPort.findById(1L)).thenReturn(Optional.of(existingHabit));
        when(habitRepositoryPort.save(any(Habit.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Optional<Habit> updatedHabitOpt = habitService.updateHabit(1L, habitWithNewData);

        // Assert
        assertTrue(updatedHabitOpt.isPresent());
        Habit updatedHabit = updatedHabitOpt.get();
        assertEquals("New Name", updatedHabit.getName());
        assertEquals("New Desc", updatedHabit.getDescription());
        verify(habitRepositoryPort).save(existingHabit);
    }

    @Test
    void givenNonExistingHabit_whenUpdateHabit_thenReturnsEmptyOptional() {
        // Arrange
        Habit habitWithNewData = new Habit();
        habitWithNewData.setName("New Name");
        when(habitRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Habit> updatedHabitOpt = habitService.updateHabit(99L, habitWithNewData);

        // Assert
        assertTrue(updatedHabitOpt.isEmpty());
    }

    @Test
    void givenExistingHabit_whenDeleteHabit_thenReturnsTrue() {
        // Arrange
        Habit habit = new Habit(1L, "Habit to delete", "d", LocalDate.now());
        when(habitRepositoryPort.findById(1L)).thenReturn(Optional.of(habit));

        // Act
        boolean result = habitService.deleteHabit(1L);

        // Assert
        assertTrue(result);
        verify(habitRepositoryPort).deleteById(1L); // Verifica se a exclusão foi chamada
    }

    @Test
    void givenNonExistingHabit_whenDeleteHabit_thenReturnsFalse() {
        // Arrange
        when(habitRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        // Act
        boolean result = habitService.deleteHabit(99L);

        // Assert
        assertFalse(result);
        verify(habitRepositoryPort, never()).deleteById(99L); // Verifica que a exclusão NÃO foi chamada
    }
}
