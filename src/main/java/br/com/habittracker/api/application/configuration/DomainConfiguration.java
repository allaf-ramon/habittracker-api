package br.com.habittracker.api.application.configuration;

import br.com.habittracker.api.domain.port.out.HabitRepositoryPort;
import br.com.habittracker.api.domain.service.HabitService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    public HabitService habitService(HabitRepositoryPort habitRepositoryPort) {
        return new HabitService(habitRepositoryPort);
    }
}
