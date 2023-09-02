package ru.practicum.event;

public interface EventService {

    EventFullDto createEvent(NewEventRequest newEventRequest, Integer userId);
}
