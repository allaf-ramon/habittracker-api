package br.com.habittracker.api.adapters.out.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "completions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"habit_id", "completion_date"}))
public class CompletionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private HabitEntity habit;

    @Column(name = "completion_date", nullable = false)
    private LocalDate completionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HabitEntity getHabit() {
        return habit;
    }

    public void setHabit(HabitEntity habit) {
        this.habit = habit;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CompletionEntity that = (CompletionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
