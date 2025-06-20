package br.com.habittracker.api.domain.port.in;

@FunctionalInterface
public interface DeleteHabitUseCase {
    /**
     * Exclui um hábito pelo seu ID.
     * @param id O ID do hábito a ser excluído.
     * @return true se o hábito foi encontrado e excluído, false caso contrário.
     */
    boolean deleteHabit(Long id);
}
