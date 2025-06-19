package br.com.habittracker.api.domain.port.out;

import br.com.habittracker.api.domain.model.Habit;

/**
 * Porta de Saída (Output Port) que define o contrato de persistência
 * para a entidade Hábito. O domínio depende desta interface, e a camada
 * de persistência (adapter) a implementará.
 */
public interface HabitRepositoryPort {
    /**
     * Salva um novo Hábito ou atualiza um existente.
     *
     * @param habit O hábito a ser salvo.
     * @return O hábito salvo.
     */
    Habit save(Habit habit);

    // Futuramente adicionaremos outros métodos aqui, como:
    // Optional<Habit> findById(Long id);
    // List<Habit> findAll();
    // void deleteById(Long id);
}
