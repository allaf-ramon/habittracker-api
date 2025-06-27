package br.com.habittracker.api.adapters.in.web.controller;

import br.com.habittracker.api.adapters.in.web.mapper.HabitDtoMapper;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.model.HabitStats;
import br.com.habittracker.api.domain.port.in.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;
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

    @MockBean
    private UpdateHabitUseCase updateHabitUseCase;

    @MockBean
    private DeleteHabitUseCase deleteHabitUseCase;

    @MockBean
    private GetHabitStatsUseCase getHabitStatsUseCase;

    @MockBean
    private FindAllHabitsWithTodayStatusUseCase findAllHabitsWithTodayStatusUseCase;

    @Test
    void whenPostValidHabit_thenReturns201Created() throws Exception {
        String requestJson = "{\"name\":\"Ler um livro\",\"description\":\"Um capítulo por dia\"}";
        Habit createdHabit = new Habit(1L, "Ler um livro", "Um capítulo por dia", LocalDate.now());
        when(createHabitUseCase.createHabit(any(Habit.class))).thenReturn(createdHabit);

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
        String requestJson = "{\"name\":\"Li\",\"description\":\"Inválido\"}";

        mockMvc.perform(post("/v1/habits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetExistingHabit_thenReturns200Ok() throws Exception {
        Habit habit = new Habit(1L, "Ler um livro", "Um capítulo por dia", LocalDate.now());
        when(findHabitByIdUseCase.findById(1L)).thenReturn(Optional.of(habit));

        mockMvc.perform(get("/v1/habits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ler um livro"));
    }

    @Test
    void whenGetNonExistingHabit_thenReturns404NotFound() throws Exception {
        when(findHabitByIdUseCase.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/habits/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAllHabits_thenReturns200OkWithList() throws Exception {
        Habit habit = new Habit(1L, "Hábito 1", "d1", LocalDate.now());
        Map<Habit, Boolean> habitsWithStatus = Map.of(habit, true);
        when(findAllHabitsWithTodayStatusUseCase.findAllWithTodayStatus()).thenReturn(habitsWithStatus);

        mockMvc.perform(get("/v1/habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hábito 1"))
                .andExpect(jsonPath("$[0].completedToday").value(true));
    }

    @Test
    void whenPutValidHabit_thenReturns200Ok() throws Exception {
        String requestJson = "{\"name\":\"Nome Atualizado\",\"description\":\"Desc Atualizada\"}";
        Habit updatedHabit = new Habit(1L, "Nome Atualizado", "Desc Atualizada", LocalDate.now());

        when(updateHabitUseCase.updateHabit(any(Long.class), any(Habit.class)))
                .thenReturn(Optional.of(updatedHabit));

        mockMvc.perform(put("/v1/habits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nome Atualizado"));
    }

    @Test
    void whenPutNonExistingHabit_thenReturns404NotFound() throws Exception {
        String requestJson = "{\"name\":\"Nome Atualizado\",\"description\":\"Desc Atualizada\"}";
        when(updateHabitUseCase.updateHabit(any(Long.class), any(Habit.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/v1/habits/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteExistingHabit_thenReturns204NoContent() throws Exception {
        when(deleteHabitUseCase.deleteHabit(1L)).thenReturn(true);

        mockMvc.perform(delete("/v1/habits/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteNonExistingHabit_thenReturns404NotFound() throws Exception {
        when(deleteHabitUseCase.deleteHabit(99L)).thenReturn(false);

        mockMvc.perform(delete("/v1/habits/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetStatsForExistingHabit_thenReturns200Ok() throws Exception {
        when(getHabitStatsUseCase.getStats(1L))
                .thenReturn(new HabitStats(5, 5, 0.0));

        mockMvc.perform(get("/v1/habits/1/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStreak").value(5))
                .andExpect(jsonPath("$.longestStreak").value(5));
    }
}
