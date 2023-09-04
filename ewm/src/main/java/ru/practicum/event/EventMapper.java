package ru.practicum.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.util.Constants.TIME_PATTERN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static Event mapToEvent(NewEventRequest newEventRequest,
                                   Category category,
                                   LocalDateTime createdOn,
                                   User initiator,
                                   Location location) {
        Event event = new Event();
        event.setAnnotation(newEventRequest.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(createdOn);
        event.setDescription(newEventRequest.getDescription());
        LocalDateTime eventDate = LocalDateTime.parse(URLDecoder.decode(newEventRequest.getEventDate()), DateTimeFormatter.ofPattern(TIME_PATTERN));
        event.setInitiator(initiator);
        event.setEventDate(eventDate);
        event.setLocation(location);
        event.setPaid(newEventRequest.getPaid());
        event.setParticipantLimit(newEventRequest.getParticipantLimit());
        event.setRequestModeration(newEventRequest.getRequestModeration());
        event.setState(State.PENDING);
        event.setTitle(newEventRequest.getTitle());
        return event;
    }

    public static EventFullDto mapToEventFullDto(Event event) {
        EventFullDto dto = new EventFullDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.mapToCategoryDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setCreatedOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        dto.setId(event.getId());
        dto.setInitiator(UserMapper.mapToUserShortDto(event.getInitiator()));
        dto.setLocation(LocationMapper.mapToLocationDto(event.getLocation()));
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        dto.setRequestModeration(event.getRequestModeration());
        dto.setState(event.getState());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        return dto;
    }
}
