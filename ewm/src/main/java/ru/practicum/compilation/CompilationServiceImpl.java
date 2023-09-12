package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.EventService;
import ru.practicum.event.EventShortDto;
import ru.practicum.exceptionhandler.NotFoundException;
import ru.practicum.util.ServiceImplUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

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
        List<EventCompilation> eventCompilationIds = eventCompilationRepository.findAllByCompilationId(compilationId);
        List<Integer> eventIds = eventCompilationIds.stream().map(EventCompilation::getEventId).collect(Collectors.toList());
        if (request.getEvents() != null) {

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

    @Override
    public List<CompilationDto> findCompilationDtos(Boolean pinned, Integer from, Integer size) {
        log.info("Search categories by pinned {}", pinned);
        List<CompilationDto> compilationDtos = new ArrayList<>();
        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);
        List<Compilation> foundCompilations = new ArrayList<>();
        if (pinned != null) {
            foundCompilations = compilationRepository.findAllByPinned(pinned, page);
        } else {
            Page<Compilation> compilationPage = compilationRepository.findAll(page);
            foundCompilations = ServiceImplUtils.mapToList(compilationPage);
        }
        if (foundCompilations != null) {

            for (Compilation compilation : foundCompilations) {
                List<EventCompilation> eventCompilationIds = eventCompilationRepository.findAllByCompilationId(compilation.getId());
                List<Integer> eventIds = eventCompilationIds.stream().map(EventCompilation::getEventId).collect(Collectors.toList());
                List<EventShortDto> eventShortDtos = eventService.findEventShortDtosByAdmin(eventIds);
                compilationDtos.add(CompilationMapper.mapToCompilationDto(compilation, eventShortDtos));
            }

        }
        return compilationDtos;
    }

    @Override
    public CompilationDto findCompilationDtoById(Integer compId) {
        log.info("Search compilation dto by id {}", compId);
        Compilation compilationById = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        List<EventCompilation> eventCompilationIds = eventCompilationRepository.findAllByCompilationId(compilationById.getId());
        List<Integer> eventIds = eventCompilationIds.stream().map(EventCompilation::getEventId).collect(Collectors.toList());
        if (eventIds != null) {
            eventShortDtos = eventService.findEventShortDtosByAdmin(eventIds);
        }
        CompilationDto compilationDto = CompilationMapper.mapToCompilationDto(compilationById, eventShortDtos);
        return compilationDto;
    }
}
