package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events/{eventId}/requests")
public class EventRequestPrivateController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> findEventRequests(@PathVariable(name = "userId") Integer userId,
                                                           @PathVariable(name = "eventId") Integer eventId) {
        return requestService.findEventRequests(userId, eventId);
    }

    @PatchMapping
    public EventRequestStatusUpdateResult updateEventRequest(@Valid @RequestBody EventRequestStatusUpdateRequest request,
                                                             @PathVariable(name = "userId") Integer userId,
                                                             @PathVariable(name = "eventId") Integer eventId) {
        return requestService.updateEventRequest(request, userId, eventId);
    }
}
