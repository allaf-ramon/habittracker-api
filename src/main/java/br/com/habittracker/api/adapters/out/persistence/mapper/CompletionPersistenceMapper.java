package br.com.habittracker.api.adapters.out.persistence.mapper;

import br.com.habittracker.api.adapters.out.persistence.entity.CompletionEntity;
import br.com.habittracker.api.adapters.out.persistence.entity.HabitEntity;
import br.com.habittracker.api.domain.model.Completion;
import org.springframework.stereotype.Component;

@Component
public class CompletionPersistenceMapper {
    public CompletionEntity toEntity(Completion completion) {
        if (completion == null) {
            return null;
        }

        CompletionEntity entity = new CompletionEntity();
        entity.setId(completion.getId());
        entity.setCompletionDate(completion.getCompletionDate());

        // Apenas associamos a entidade Habit pelo ID. O JPA cuidará do resto.
        if (completion.getHabitId() != null) {
            HabitEntity habitEntity = new HabitEntity();
            habitEntity.setId(completion.getHabitId());
            entity.setHabit(habitEntity);
        }

        return entity;
    }

    public Completion toDomain(CompletionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Completion(
                entity.getId(),
                entity.getHabit().getId(),
                entity.getCompletionDate()
        );
    }
}
