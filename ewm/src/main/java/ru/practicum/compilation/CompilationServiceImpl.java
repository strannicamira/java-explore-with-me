package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.EventService;
import ru.practicum.event.EventShortDto;
import ru.practicum.exceptionhandler.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventCompilationRepository eventCompilationRepository;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Create compilation");
        Compilation compilation = compilationRepository.save(CompilationMapper.mapToCompilation(newCompilationDto));
        List<EventShortDto> events = new ArrayList<>();

        Integer compilationId = compilation.getId();
        List<Integer> eventIds = newCompilationDto.getEvents();

        if (eventIds != null) {


            for (Integer eventId : eventIds) {
                EventCompilation eventCompilation = new EventCompilation();
                eventCompilation.setCompilationId(compilationId);
                eventCompilation.setEventId(eventId);
                eventCompilationRepository.save(eventCompilation);
            }
            //eventCompilationService.create(Integer compilationId, List<Event> events);
            //List<Event> events = eventCompilationService.findByCompilationId(compilationId);


            events = eventService.findEventShortDtosByAdmin(eventIds);
        }
        return CompilationMapper.mapToCompilationDto(compilation, events);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationRequest request, Integer compId) {
        log.info("Update compilation by id {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));

        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }

        Compilation savedCompilation = compilationRepository.save(compilation);

        Integer compilationId = compilation.getId();
        List<Integer> eventIds = eventCompilationRepository.findByCompilationId(compilationId);
        if (request.getEvents() != null) {// && !request.getEvents().isEmpty()

            for (Integer eventId : eventIds) {
                EventCompilation eventCompilation = new EventCompilation();
                eventCompilation.setCompilationId(compilationId);
                eventCompilation.setEventId(eventId);
                eventCompilationRepository.delete(eventCompilation);
            }

            eventIds = request.getEvents();

            for (Integer eventId : eventIds) {
                EventCompilation eventCompilation = new EventCompilation();
                eventCompilation.setCompilationId(compilationId);
                eventCompilation.setEventId(eventId);
                eventCompilationRepository.save(eventCompilation);
            }

        }
        List<EventShortDto> eventShortDtoss = eventService.findEventShortDtosByAdmin(eventIds);
        return CompilationMapper.mapToCompilationDto(savedCompilation, eventShortDtoss);
    }

    @Override
    @Transactional
    public void deleteCompilationById(Integer compId) {
        log.info("Delete compilation by id {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        compilationRepository.deleteById(compId);
    }
}
