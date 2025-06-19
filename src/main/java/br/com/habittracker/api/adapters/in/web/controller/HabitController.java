package br.com.habittracker.api.adapters.in.web.controller;

import br.com.habittracker.api.adapters.in.web.dto.HabitRequestDTO;
import br.com.habittracker.api.adapters.in.web.dto.HabitResponseDTO;
import br.com.habittracker.api.adapters.in.web.mapper.HabitDtoMapper;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.in.CreateHabitUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/habits")
public class HabitController {
    private final CreateHabitUseCase createHabitUseCase;
    private final HabitDtoMapper mapper;

    public HabitController(CreateHabitUseCase createHabitUseCase, HabitDtoMapper mapper) {
        this.createHabitUseCase = createHabitUseCase;
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
}
