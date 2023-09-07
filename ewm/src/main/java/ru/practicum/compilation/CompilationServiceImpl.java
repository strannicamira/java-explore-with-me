package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.exceptionhandler.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Create compilation");
        Compilation compilation = compilationRepository.save(CompilationMapper.mapToCompilation(newCompilationDto));
        Integer compilationId = compilation.getId();

        List<Event> events = newCompilationDto.getEvents();

        for (Event event : events){
            EventCompilation eventCompilation = new EventCompilation();
            eventCompilation.setCompilationId(compilationId);
            eventCompilation.setEventId(event.getId());
            eventCompilationRepository.save(eventCompilation);
        }
        //eventCompilationService.create(Integer compilationId, List<Event> events);
        //List<Event> events = eventCompilationService.findByCompilationId(compilationId);

        return CompilationMapper.mapToCompilationDto(compilation, events);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationRequest request, Integer compId) {
        log.info("Update compilation by id {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));

        if(request.getPinned()!=null){
            compilation.setPinned(request.getPinned());
        }
        if(request.getTitle()!=null){
            compilation.setTitle(request.getTitle());
        }

        Compilation savedCompilation = compilationRepository.save(compilation);

        Integer compilationId = compilation.getId();
        List<Event> events = eventCompilationRepository.findByCompilationId(compilationId);
        if(request.getEvents()!=null) {// && !request.getEvents().isEmpty()

            for (Event event : events){
                EventCompilation eventCompilation = new EventCompilation();
                eventCompilation.setCompilationId(compilationId);
                eventCompilation.setEventId(event.getId());
                eventCompilationRepository.delete(eventCompilation);
            }

            events=request.getEvents();

            for (Event event : events){
                EventCompilation eventCompilation = new EventCompilation();
                eventCompilation.setCompilationId(compilationId);
                eventCompilation.setEventId(event.getId());
                eventCompilationRepository.save(eventCompilation);
            }

        }
        return CompilationMapper.mapToCompilationDto(savedCompilation, events);
    }

    @Override
    @Transactional
    public void deleteCompilationById(Integer compId) {
        log.info("Delete compilation by id {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        compilationRepository.deleteById(compId);
    }
}
