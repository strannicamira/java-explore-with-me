package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> findEventsByPublic(HttpServletRequest request,
                                                  @RequestParam(name = "text", required = false) String text,
                                                  @RequestParam(name = "categories", required = false) Integer[] categoryIds,
                                                  @RequestParam(name = "paid", required = false) Boolean paid,
                                                  @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                  @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                  @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(name = "sort", required = false) String sort,
                                                  @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(name = "size", required = false, defaultValue = "10") Integer size
    ) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.findEventsByPublic(request, text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(value = "/{id}")
    public EventWithCommentsFullDto findEventByIdByPublic(HttpServletRequest request,
                                                          @PathVariable(name = "id") Integer eventId) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.findEventByIdByPublic(request, eventId);
    }
}
