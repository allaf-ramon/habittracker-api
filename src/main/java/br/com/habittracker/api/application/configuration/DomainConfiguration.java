package br.com.habittracker.api.application.configuration;

import br.com.habittracker.api.domain.port.out.CompletionRepositoryPort;
import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;
import br.com.habittracker.api.domain.service.CompletionService;
import br.com.habittracker.api.domain.service.HabitListService;
import br.com.habittracker.api.domain.service.HabitService;
import br.com.habittracker.api.domain.service.StatsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    public HabitService habitService(HabitRepositoryPort habitRepositoryPort) {
        return new HabitService(habitRepositoryPort);
    }
    @Bean
    public CompletionService completionService(CompletionRepositoryPort completionRepositoryPort,
                                               HabitRepositoryPort habitRepositoryPort) {
        return new CompletionService(completionRepositoryPort, habitRepositoryPort);
    }

    @Bean
    public StatsService statsService(CompletionRepositoryPort completionRepositoryPort, HabitRepositoryPort habitRepositoryPort) {
        return new StatsService(completionRepositoryPort, habitRepositoryPort);
    }

    @Bean
    public HabitListService habitListService(HabitRepositoryPort habitRepositoryPort, CompletionRepositoryPort completionRepositoryPort) {
        return new HabitListService(habitRepositoryPort, completionRepositoryPort);
    }
}
