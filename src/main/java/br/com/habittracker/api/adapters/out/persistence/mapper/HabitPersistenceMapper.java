package br.com.habittracker.api.adapters.out.persistence.mapper;

import br.com.habittracker.api.adapters.out.persistence.entity.HabitEntity;
import br.com.habittracker.api.domain.model.Habit;
import org.springframework.stereotype.Component;

@Component // Marcamos como um componente Spring para poder injetá-lo
public class HabitPersistenceMapper {
    public HabitEntity toEntity(Habit habit) {
        if (habit == null) return null;

        HabitEntity entity = new HabitEntity();
        entity.setId(habit.getId());
        entity.setName(habit.getName());
        entity.setDescription(habit.getDescription());
        entity.setCreationDate(habit.getCreationDate());
        return entity;
    }

    public Habit toDomain(HabitEntity entity) {
        if (entity == null) return null;

        Habit habit = new Habit();
        habit.setId(entity.getId());
        habit.setName(entity.getName());
        habit.setDescription(entity.getDescription());
        habit.setCreationDate(entity.getCreationDate());
        return habit;
    }
}
