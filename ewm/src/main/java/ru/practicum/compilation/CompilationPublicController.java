package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> findCompilationByPublic(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        List<CompilationDto> compilationDto = compilationService.findCompilationDtos(pinned, from, size);
        return compilationDto;
    }

    @GetMapping(value = "/{compId}")
    public CompilationDto findCompilationByIdByPublic(@PathVariable(name = "compId") Integer compId) {
        CompilationDto compilationDto = compilationService.findCompilationDtoById(compId);
        return compilationDto;
    }

}
