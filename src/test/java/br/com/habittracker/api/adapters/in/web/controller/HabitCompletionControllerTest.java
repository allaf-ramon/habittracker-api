package br.com.habittracker.api.adapters.in.web.controller;

import br.com.habittracker.api.domain.port.in.MarkCompletionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HabitCompletionController.class)
class HabitCompletionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarkCompletionUseCase markCompletionUseCase;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Test
    void whenPostCompletion_thenReturns200Ok() throws Exception {
        // Arrange
        Long habitId = 1L;
        LocalDate date = LocalDate.now();
        String dateString = date.format(formatter);

        // Act & Assert
        mockMvc.perform(post("/v1/habits/{habitId}/completions/{date}", habitId, dateString))
                .andExpect(status().isOk());

        verify(markCompletionUseCase).markAsCompleted(habitId, date);
    }

    @Test
    void whenPostCompletionForNonExistentHabit_thenReturns400BadRequest() throws Exception {
        // Arrange
        Long habitId = 99L;
        LocalDate date = LocalDate.now();
        String dateString = date.format(formatter);

        // Simula o serviço lançando a exceção que definimos
        doThrow(new IllegalArgumentException("Hábito não encontrado."))
                .when(markCompletionUseCase).markAsCompleted(habitId, date);

        // Act & Assert
        mockMvc.perform(post("/v1/habits/{habitId}/completions/{date}", habitId, dateString))
                .andExpect(status().isBadRequest()); // O Spring trata IllegalArgumentException como 400 por padrão
    }

    @Test
    void whenDeleteCompletion_thenReturns204NoContent() throws Exception {
        // Arrange
        Long habitId = 1L;
        LocalDate date = LocalDate.now();
        String dateString = date.format(formatter);

        // Act & Assert
        mockMvc.perform(delete("/v1/habits/{habitId}/completions/{date}", habitId, dateString))
                .andExpect(status().isNoContent());

        verify(markCompletionUseCase).markAsNotCompleted(habitId, date);
    }
}
