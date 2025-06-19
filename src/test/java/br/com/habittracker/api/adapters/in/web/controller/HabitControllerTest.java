package br.com.habittracker.api.adapters.in.web.controller;

import br.com.habittracker.api.adapters.in.web.mapper.HabitDtoMapper;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.in.CreateHabitUseCase;
import br.com.habittracker.api.domain.port.in.FindAllHabitsUseCase;
import br.com.habittracker.api.domain.port.in.FindHabitByIdUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private FindHabitByIdUseCase findHabitByIdUseCase;

    @MockBean
    private FindAllHabitsUseCase findAllHabitsUseCase;


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

    @Test
    void whenGetExistingHabit_thenReturns200Ok() throws Exception {
        // Arrange
        Habit habit = new Habit(1L, "Ler um livro", "Um capítulo por dia", LocalDate.now());
        when(findHabitByIdUseCase.findById(1L)).thenReturn(Optional.of(habit));

        // Act & Assert
        mockMvc.perform(get("/v1/habits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ler um livro"));
    }

    @Test
    void whenGetNonExistingHabit_thenReturns404NotFound() throws Exception {
        // Arrange
        when(findHabitByIdUseCase.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/v1/habits/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAllHabits_thenReturns200OkWithList() throws Exception {
        // Arrange
        List<Habit> habits = List.of(new Habit(1L, "Hábito 1", "d1", LocalDate.now()));
        when(findAllHabitsUseCase.findAll()).thenReturn(habits);

        // Act & Assert
        mockMvc.perform(get("/v1/habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hábito 1"));
    }
}
