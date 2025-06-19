package br.com.habittracker.api.adapters.in.web.controller;

import br.com.habittracker.api.adapters.in.web.mapper.HabitDtoMapper;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.in.CreateHabitUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Mappers são componentes, então precisam estar disponíveis para o controller
@WebMvcTest(controllers = {HabitController.class, HabitDtoMapper.class})
class HabitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateHabitUseCase createHabitUseCase;

    @Test
    void whenPostValidHabit_thenReturns201Created() throws Exception {
        // Arrange
        String requestJson = "{\"name\":\"Ler um livro\",\"description\":\"Um capítulo por dia\"}";

        Habit createdHabit = new Habit(1L, "Ler um livro", "Um capítulo por dia", LocalDate.now());
        when(createHabitUseCase.createHabit(any(Habit.class))).thenReturn(createdHabit);

        // Act & Assert
        mockMvc.perform(post("/v1/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ler um livro"))
                .andExpect(header().string("Location", "http://localhost/v1/habits/1"));
    }

    @Test
    void whenPostInvalidHabit_thenReturns400BadRequest() throws Exception {
        // Arrange: Nome com menos de 3 caracteres
        String requestJson = "{\"name\":\"Li\",\"description\":\"Inválido\"}";

        // Act & Assert
        mockMvc.perform(post("/v1/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
