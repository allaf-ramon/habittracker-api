package br.com.habittracker.api.adapters.out.persistence.repository;

import br.com.habittracker.api.adapters.out.persistence.entity.HabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabitJpaRepository extends JpaRepository<HabitEntity, Long> {
}
