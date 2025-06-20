package br.com.habittracker.api.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa o registro de que um Hábito foi concluído em uma data específica.
 */
public class Completion {
    private Long id;
    private Long habitId;
    private LocalDate completionDate;

    public Completion() {
    }

    public Completion(Long id, Long habitId, LocalDate completionDate) {
        this.id = id;
        this.habitId = habitId;
        this.completionDate = completionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
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
        Completion that = (Completion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
