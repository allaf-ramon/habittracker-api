package br.com.habittracker.api.adapters.out.persistence.repository;

import br.com.habittracker.api.adapters.out.persistence.entity.CompletionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompletionJpaRepository extends JpaRepository<CompletionEntity, Long> {
    Optional<CompletionEntity> findByHabitIdAndCompletionDate(Long habitId, LocalDate completionDate);

    @Transactional // Necessário para operações de delete customizadas
    void deleteByHabitIdAndCompletionDate(Long habitId, LocalDate date);

    List<CompletionEntity> findByHabitIdOrderByCompletionDateDesc(Long habitId);
}
