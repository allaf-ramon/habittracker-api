package br.com.habittracker.api.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa a entidade de negócio Hábito.
 * Esta classe é parte do core do domínio e não deve ter dependências
 * com frameworks externos (como Spring ou JPA).
 */
public class Habit {
    private Long id;
    private String name;
    private String description;
    private LocalDate creationDate;

    // Construtores, Getters e Setters são necessários para o funcionamento.
    // A boa prática é garantir que o objeto esteja sempre em um estado válido.

    public Habit() {
    }

    public Habit(Long id, String name, String description, LocalDate creationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return Objects.equals(id, habit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
