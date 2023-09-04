package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.User;

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
    public EventFullDto createEvent(@Valid @RequestBody NewEventRequest event,
                                    @PathVariable(name = "userId") Integer userId) {
        return eventService.createEvent(event, userId);
    }


    @GetMapping
    public List<EventShortDto> findEvents(
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @PathVariable(name = "userId") Integer userId) {
        return eventService.findEventShortDtos(from, size, userId);
    }

}
