package br.com.habittracker.api.domain.service;

import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {
    @Mock
    private HabitRepositoryPort habitRepositoryPort;

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
}
