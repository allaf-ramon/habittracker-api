package br.com.habittracker.api.adapters.out.persistence;

import br.com.habittracker.api.adapters.out.persistence.entity.CompletionEntity;
import br.com.habittracker.api.adapters.out.persistence.mapper.CompletionPersistenceMapper;
import br.com.habittracker.api.adapters.out.persistence.repository.CompletionJpaRepository;
import br.com.habittracker.api.domain.model.Completion;
import br.com.habittracker.api.domain.port.out.CompletionRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CompletionPersistenceAdapter implements CompletionRepositoryPort {
    private final CompletionJpaRepository completionJpaRepository;
    private final CompletionPersistenceMapper mapper;

    public CompletionPersistenceAdapter(CompletionJpaRepository completionJpaRepository,
                                        CompletionPersistenceMapper mapper) {
    this.completionJpaRepository = completionJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Completion completion) {
        CompletionEntity entity = mapper.toEntity(completion);
        completionJpaRepository.save(entity);
    }

    @Override
    public void delete(Long habitId, LocalDate date) {
        completionJpaRepository.deleteByHabitIdAndCompletionDate(habitId, date);
    }

    @Override
    public Optional<Completion> findByHabitIdAndDate(Long habitId, LocalDate date) {
        return completionJpaRepository.findByHabitIdAndCompletionDate(habitId, date)
                .map(mapper::toDomain);
    }

    @Override
    public List<Completion> findByHabitIdOrderByCompletionDateDesc(Long habitId) {
        return completionJpaRepository.findByHabitIdOrderByCompletionDateDesc(habitId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Completion> findByCompletionDate(LocalDate date) {
        return completionJpaRepository.findByCompletionDate(date)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
