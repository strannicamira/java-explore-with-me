package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEventsByAdmin(
            @RequestParam(name = "users") Integer[] userIds, //список id пользователей, чьи события нужно найти
            @RequestParam(name = "states") String[] stateNames, //список состояний в которых находятся искомые события
            @RequestParam(name = "categories") Integer[] categoryIds, //список id категорий в которых будет вестись поиск
            @RequestParam(name = "rangeStart") String rangeStart, //дата и время не раньше которых должно произойти событие
            @RequestParam(name = "rangeEnd") String rangeEnd, //дата и время не позже которых должно произойти событие
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from, //количество событий, которые нужно пропустить для формирования текущего набора
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size //количество событий в наборе
    ) {
        return eventService.findEventsByAdmin(userIds, stateNames, categoryIds, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(value = "/{eventId}")
    public EventFullDto updateEventByAdmin(@Valid @RequestBody UpdateEventAdminRequest request,
                                          @PathVariable(name = "eventId") Integer eventId) {
        return eventService.updateEventByAdmin(request, eventId);
    }
}
