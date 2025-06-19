package br.com.habittracker.api.adapters.out.persistence;

import br.com.habittracker.api.adapters.out.persistence.entity.HabitEntity;
import br.com.habittracker.api.adapters.out.persistence.mapper.HabitPersistenceMapper;
import br.com.habittracker.api.adapters.out.persistence.repository.HabitJpaRepository;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;
import org.springframework.stereotype.Component;

@Component // Marcamos como um componente Spring
public class HabitPersistenceAdapter implements HabitRepositoryPort {
    private final HabitJpaRepository jpaRepository;
    private final HabitPersistenceMapper mapper;

    public HabitPersistenceAdapter(HabitJpaRepository jpaRepository, HabitPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Habit save(Habit habit) {
        HabitEntity habitEntity = mapper.toEntity(habit);
        HabitEntity savedEntity = jpaRepository.save(habitEntity);
        return mapper.toDomain(savedEntity);
    }
}
