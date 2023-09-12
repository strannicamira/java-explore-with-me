package ru.practicum.compilation;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto dto);

    @Transactional
    CompilationDto updateCompilation(UpdateCompilationRequest dto, Integer compId);

    @Transactional
    void deleteCompilationById(Integer compId);

    List<CompilationDto> findCompilationDtos(Boolean pinned, Integer from, Integer size);

    CompilationDto findCompilationDtoById(Integer compId);
}
