package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventByPrivate(@Valid @RequestBody NewEventRequest event,
                                             @PathVariable(name = "userId") Integer userId) {
        return eventService.createEventByPrivate(event, userId);
    }

    @GetMapping
    public List<EventShortDto> findEventsByPrivate(
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @PathVariable(name = "userId") Integer userId) {
        return eventService.findEventsByPrivate(from, size, userId);
    }

    @GetMapping(value = "/{eventId}")
    public EventFullDto findEventByIdByPrivate(
            @PathVariable(name = "userId") Integer userId,
            @PathVariable(name = "eventId") Integer eventId) {
        return eventService.findEventByIdByPrivate(userId, eventId);
    }

    @PatchMapping(value = "/{eventId}")
    public EventFullDto updateEventByPrivate(@Valid @RequestBody UpdateEventUserRequest event,
                                             @PathVariable(name = "userId") Integer userId,
                                             @PathVariable(name = "eventId") Integer eventId) {
        return eventService.updateEventByPrivate(event, userId, eventId);
    }
}
