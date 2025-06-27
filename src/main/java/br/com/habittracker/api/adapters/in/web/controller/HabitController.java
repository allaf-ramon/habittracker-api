package br.com.habittracker.api.adapters.in.web.controller;

import br.com.habittracker.api.adapters.in.web.dto.HabitRequestDTO;
import br.com.habittracker.api.adapters.in.web.dto.HabitResponseDTO;
import br.com.habittracker.api.adapters.in.web.dto.HabitStatsResponseDTO;
import br.com.habittracker.api.adapters.in.web.mapper.HabitDtoMapper;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.model.HabitStats;
import br.com.habittracker.api.domain.port.in.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/habits")
public class HabitController {
    private final CreateHabitUseCase createHabitUseCase;
    private final FindHabitByIdUseCase findHabitByIdUseCase;
    private final FindAllHabitsUseCase findAllHabitsUseCase;
    private final UpdateHabitUseCase updateHabitUseCase;
    private final DeleteHabitUseCase deleteHabitUseCase;
    private final GetHabitStatsUseCase getHabitStatsUseCase;
    private final FindAllHabitsWithTodayStatusUseCase findAllHabitsWithTodayStatusUseCase;
    private final HabitDtoMapper mapper;

    public HabitController(CreateHabitUseCase createHabitUseCase,
                            FindAllHabitsUseCase findAllHabitsUseCase,
                            FindHabitByIdUseCase findHabitByIdUseCase,
                            UpdateHabitUseCase updateHabitUseCase,
                            DeleteHabitUseCase deleteHabitUseCase,
                            GetHabitStatsUseCase getHabitStatsUseCase,
                            FindAllHabitsWithTodayStatusUseCase findAllHabitsWithTodayStatusUseCase,
                           HabitDtoMapper mapper) {
        this.createHabitUseCase = createHabitUseCase;
        this.findHabitByIdUseCase = findHabitByIdUseCase;
        this.findAllHabitsUseCase = findAllHabitsUseCase;
        this.updateHabitUseCase = updateHabitUseCase;
        this.deleteHabitUseCase = deleteHabitUseCase;
        this.getHabitStatsUseCase = getHabitStatsUseCase;
        this.findAllHabitsWithTodayStatusUseCase = findAllHabitsWithTodayStatusUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<HabitResponseDTO> createHabit(@Valid @RequestBody HabitRequestDTO request) {
        Habit habitDomain = mapper.toDomain(request);
        Habit createdHabit = createHabitUseCase.createHabit(habitDomain);
        HabitResponseDTO response = mapper.toResponse(createdHabit);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitResponseDTO> findHabitById(@PathVariable Long id) {
        return findHabitByIdUseCase.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<HabitResponseDTO>> findAllHabits() {
        // 1. Chama o novo caso de uso que já retorna a lógica pronta
        Map<Habit, Boolean> habitsWithStatus = findAllHabitsWithTodayStatusUseCase.findAllWithTodayStatus();

        // 2. Mapeia o resultado para o DTO de resposta
        List<HabitResponseDTO> response = habitsWithStatus.entrySet().stream()
                .map(entry -> {
                    Habit habit = entry.getKey();
                    boolean isCompleted = entry.getValue();

                    HabitResponseDTO dto = mapper.toResponse(habit);
                    dto.setCompletedToday(isCompleted);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitResponseDTO> updateHabit(
            @PathVariable Long id, @Valid @RequestBody HabitRequestDTO request) {
        Habit habitDomain = mapper.toDomain(request);

        return updateHabitUseCase.updateHabit(id, habitDomain)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        boolean deleted = deleteHabitUseCase.deleteHabit(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<HabitStatsResponseDTO> getHabitStats(@PathVariable Long id) {
        HabitStats stats = getHabitStatsUseCase.getStats(id);
        
        HabitStatsResponseDTO response = new HabitStatsResponseDTO();
        response.setCurrentStreak(stats.getCurrentStreak());
        response.setLongestStreak(stats.getLongestStreak());
        response.setSuccessRate(stats.getSuccessRate());

        return ResponseEntity.ok(response);
    }
}
