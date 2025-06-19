package br.com.habittracker.api.adapters.in.web.mapper;

import br.com.habittracker.api.adapters.in.web.dto.HabitRequestDTO;
import br.com.habittracker.api.adapters.in.web.dto.HabitResponseDTO;
import br.com.habittracker.api.domain.model.Habit;
import org.springframework.stereotype.Component;

@Component
public class HabitDtoMapper {
    public Habit toDomain(HabitRequestDTO dto) {
        if (dto == null) return null;
        Habit habit = new Habit();
        habit.setName(dto.getName());
        habit.setDescription(dto.getDescription());
        return habit;
    }

    public HabitResponseDTO toResponse(Habit habit) {
        if (habit == null) return null;
        HabitResponseDTO dto = new HabitResponseDTO();
        dto.setId(habit.getId());
        dto.setName(habit.getName());
        dto.setDescription(habit.getDescription());
        dto.setCreationDate(habit.getCreationDate());
        return dto;
    }
}
