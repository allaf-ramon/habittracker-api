package br.com.habittracker.api.adapters.in.web.controller;

import br.com.habittracker.api.domain.port.in.MarkCompletionUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/habits/{habitId}/completions")
public class HabitCompletionController {
    private final MarkCompletionUseCase markCompletionUseCase;

    public HabitCompletionController(MarkCompletionUseCase markCompletionUseCase) {
        this.markCompletionUseCase = markCompletionUseCase;
    }

    @PostMapping("/{date}")
    public ResponseEntity<Void> markAsCompleted(@PathVariable Long habitId, @PathVariable LocalDate date) {
        try {
            markCompletionUseCase.markAsCompleted(habitId, date);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<Void> markAsNotCompleted(@PathVariable Long habitId, @PathVariable LocalDate date) {
        markCompletionUseCase.markAsNotCompleted(habitId, date);
        return ResponseEntity.noContent().build();
    }
}
