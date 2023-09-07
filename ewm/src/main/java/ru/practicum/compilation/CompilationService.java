package ru.practicum.compilation;

import org.springframework.transaction.annotation.Transactional;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto dto);

    @Transactional
    CompilationDto updateCompilation(UpdateCompilationRequest dto, Integer compId);

    @Transactional
    void deleteCompilationById(Integer compId);
}
