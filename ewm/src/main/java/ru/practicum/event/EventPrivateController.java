package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class EventPrivateController {

    private final EventService eventService;

    @PostMapping(value = "/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Valid @RequestBody NewEventRequest event,
                                    @PathVariable(name = "userId") Integer userId) {
        return eventService.createEvent(event, userId);
    }

}
