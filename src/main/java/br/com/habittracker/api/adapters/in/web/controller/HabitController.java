package br.com.habittracker.api.adapters.in.web.controller;

import br.com.habittracker.api.adapters.in.web.dto.HabitRequestDTO;
import br.com.habittracker.api.adapters.in.web.dto.HabitResponseDTO;
import br.com.habittracker.api.adapters.in.web.mapper.HabitDtoMapper;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.in.CreateHabitUseCase;
import br.com.habittracker.api.domain.port.in.FindHabitByIdUseCase;
import br.com.habittracker.api.domain.port.in.FindAllHabitsUseCase;
import br.com.habittracker.api.domain.port.in.UpdateHabitUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/habits")
public class HabitController {
    private final CreateHabitUseCase createHabitUseCase;
    private final FindHabitByIdUseCase findHabitByIdUseCase;
    private final FindAllHabitsUseCase findAllHabitsUseCase;
    private final UpdateHabitUseCase updateHabitUseCase;
    private final HabitDtoMapper mapper;

    public HabitController(CreateHabitUseCase createHabitUseCase,
                            FindAllHabitsUseCase findAllHabitsUseCase,
                            FindHabitByIdUseCase findHabitByIdUseCase,
                            UpdateHabitUseCase updateHabitUseCase,
                           HabitDtoMapper mapper) {
        this.createHabitUseCase = createHabitUseCase;
        this.findHabitByIdUseCase = findHabitByIdUseCase;
        this.findAllHabitsUseCase = findAllHabitsUseCase;
        this.updateHabitUseCase = updateHabitUseCase;
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
        List<HabitResponseDTO> response = findAllHabitsUseCase.findAll().stream()
                .map(mapper::toResponse)
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
}
