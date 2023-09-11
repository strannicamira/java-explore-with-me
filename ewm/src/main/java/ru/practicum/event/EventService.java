package ru.practicum.event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    EventFullDto createEventByPrivate(NewEventRequest newEventRequest, Integer userId);

    List<EventShortDto> findEventsByPrivate(Integer from, Integer size, Integer userId);

    EventFullDto findEventByIdByPrivate(Integer userId, Integer eventId);

    Event findEventById(Integer eventId);

    Event findEventById(Integer userId, Integer eventId);

    EventFullDto updateEventByPrivate(UpdateEventUserRequest event, Integer userId, Integer eventId);

    List<Event> findEventsByAdmin(Integer[] userIds, String[] stateNames, Integer[] categoryIds, String rangeStart, String rangeEnd, Integer from, Integer size);

    List<EventFullDto> findEventFullDtosByAdmin(Integer[] userIds, String[] stateNames, Integer[] categoryIds,
                                                String rangeStart, String rangeEnd, Integer from, Integer size);

    List<EventShortDto> findEventShortDtosByAdmin(List<Integer> eventIds);

    EventFullDto updateEventByAdmin(UpdateEventAdminRequest request, Integer eventId);

    List<EventShortDto> findEventsByPublic(HttpServletRequest request, String text, Integer[] categoryIds, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto findEventByIdByPublic(HttpServletRequest request, Integer eventId);
}
