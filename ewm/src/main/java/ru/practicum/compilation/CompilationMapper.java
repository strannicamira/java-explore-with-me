package ru.practicum.compilation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.event.EventShortDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static Compilation mapToCompilation(NewCompilationDto dto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(dto.getTitle());
        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }
        return compilation;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation, List<EventShortDto> events) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setPinned(compilation.getPinned());
        dto.setTitle(compilation.getTitle());
        dto.setEvents(events);
        return dto;
    }

//    public static List<CompilationDto> mapToCompilationDto(List<Compilation> compilations,
//                                                           Map<Integer, List<EventShortDto>> eventDtos) {
//        List<CompilationDto> compilationDtos = new ArrayList<>();
//        for (Compilation compilation : compilations) {
//            Integer compilationId = compilation.getId();
//            compilationDtos.add(mapToCompilationDto(compilation, eventDtos.get(compilationId)));
//        }
//        return compilationDtos;
//    }

}
