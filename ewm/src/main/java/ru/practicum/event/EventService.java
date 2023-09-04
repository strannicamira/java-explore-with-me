package ru.practicum.event;

import ru.practicum.category.CategoryDto;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(NewEventRequest newEventRequest, Integer userId);

    List<EventShortDto> findEventShortDtos(Integer from, Integer size, Integer userId);

    EventFullDto findEventFullDtoById(Integer userId, Integer eventId);

    EventFullDto updateEvent(UpdateEventUserRequest event, Integer userId, Integer eventId);
}
