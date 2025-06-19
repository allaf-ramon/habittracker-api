package br.com.habittracker.api.adapters.out.persistence;

import br.com.habittracker.api.adapters.out.persistence.entity.HabitEntity;
import br.com.habittracker.api.adapters.out.persistence.mapper.HabitPersistenceMapper;
import br.com.habittracker.api.adapters.out.persistence.repository.HabitJpaRepository;
import br.com.habittracker.api.domain.model.Habit;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Optional<Habit> findById(Long id) {
        Optional<HabitEntity> entityOptional = jpaRepository.findById(id);
        return entityOptional.map(mapper::toDomain); // Converte se presente
    }

    @Override
    public List<Habit> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
