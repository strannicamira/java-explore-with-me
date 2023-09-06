package ru.practicum.event;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(NewEventRequest newEventRequest, Integer userId);

    List<EventShortDto> findEventShortDtos(Integer from, Integer size, Integer userId);

    EventFullDto findEventFullDtoById(Integer userId, Integer eventId);

    EventFullDto updateEvent(UpdateEventUserRequest event, Integer userId, Integer eventId);

    List<EventFullDto> findEventsByAdmin(Integer[] userIds, String[] stateNames, Integer[] categoryIds, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(UpdateEventAdminRequest request, Integer eventId);

    List<EventShortDto> findEventsByPublic(String text, Integer[] categoryIds, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);
}
