package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilationByAdmin(@Valid @RequestBody NewCompilationDto dto) {
        return compilationService.createCompilation(dto);
    }

    @PatchMapping(value = "/{compId}")
    public CompilationDto updateCompilationByAdmin(@Valid @RequestBody UpdateCompilationRequest dto,
                                                   @PathVariable(name = "compId") Integer compId) {
        return compilationService.updateCompilation(dto, compId);
    }

    @DeleteMapping(value = "/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationByIdByAdmin(@PathVariable(name = "compId") Integer compId) {
        compilationService.deleteCompilationById(compId);
    }

}
