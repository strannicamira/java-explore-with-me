package ru.practicum.event;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(NewEventRequest newEventRequest, Integer userId);

    List<EventShortDto> findEventShortDtos(Integer from, Integer size, Integer userId);
}
