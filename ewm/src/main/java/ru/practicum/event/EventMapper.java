package ru.practicum.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryDto;
import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.UserShortDto;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.util.Constants.TIME_PATTERN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static Event mapToEvent(NewEventRequest newEventRequest, Category category, Location location) {
        Event event = new Event();
        event.setAnnotation(newEventRequest.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventRequest.getDescription());
        LocalDateTime eventDate = LocalDateTime.parse(URLDecoder.decode(newEventRequest.getEventDate()), DateTimeFormatter.ofPattern(TIME_PATTERN));
        event.setEventDate(eventDate);
        event.setLocation(location);
        event.setPaid(newEventRequest.getPaid());
        event.setParticipantLimit(newEventRequest.getParticipantLimit());
        event.setRequestModeration(newEventRequest.getRequestModeration());
        event.setTitle(newEventRequest.getTitle());
        return event;
    }

    public static EventFullDto mapToEventFullDto(Event event, CategoryDto categoryDto,
                                                 Integer confirmedRequests,
                                                 LocalDateTime createdOn,
                                                 UserShortDto initiator,
                                                 LocalDateTime publishedOn,
                                                 State state,
                                                 Integer views) {
        EventFullDto dto = new EventFullDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(categoryDto);
        dto.setConfirmedRequests(confirmedRequests);
        dto.setCreatedOn(createdOn.format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        dto.setId(event.getId());
        dto.setInitiator(initiator);
        dto.setLocation(LocationMapper.mapToLocationDto(event.getLocation()));
        dto.setPaid(event.getPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(publishedOn != null ? publishedOn.format(DateTimeFormatter.ofPattern(TIME_PATTERN)) : null);
        dto.setRequestModeration(event.getRequestModeration());
        dto.setState(state);
        dto.setTitle(event.getTitle());
        dto.setViews(views);
        return dto;
    }
}
