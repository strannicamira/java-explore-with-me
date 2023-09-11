package ru.practicum.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.util.Constants.TIME_PATTERN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

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


    public static EventShortDto mapToEventShortDto(Event event) {
        EventShortDto dto = new EventShortDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.mapToCategoryDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setEventDate(event.getEventDate().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        dto.setId(event.getId());
        dto.setInitiator(UserMapper.mapToUserShortDto(event.getInitiator()));
        dto.setPaid(event.getPaid());
        dto.setTitle(event.getTitle());
        dto.setViews(event.getViews());
        return dto;
    }

    public static List<EventShortDto> mapToEventShortDto(List<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(mapToEventShortDto(event));
        }
        return dtos;
    }


    public static Event mapToEvent(Event event,
                                   UpdateEventUserRequest request,
                                   Category category,
                                   LocalDateTime eventDate,
                                   LocationDto locationDto,
                                   State state,
                                   LocalDateTime publishedOn) {
        Event newEvent = event;
        newEvent.setAnnotation(getNewValue(event.getAnnotation(), request.getAnnotation()));
        newEvent.setCategory(getNewValue(event.getCategory(), category));
        newEvent.setDescription(getNewValue(event.getDescription(), request.getDescription()));
        newEvent.setEventDate(getNewValue(event.getEventDate(), eventDate));
        if (locationDto != null) {
            newEvent.getLocation().setLat(locationDto.getLat());
            newEvent.getLocation().setLon(locationDto.getLon());
        }
        newEvent.setPaid(getNewValue(event.getPaid(), request.getPaid()));
        newEvent.setParticipantLimit(getNewValue(event.getParticipantLimit(), request.getParticipantLimit()));
        newEvent.setRequestModeration(getNewValue(event.getRequestModeration(), request.getRequestModeration()));
        newEvent.setPublishedOn(getNewValue(event.getPublishedOn(), publishedOn));
        newEvent.setState(getNewValue(event.getState(), state));
        newEvent.setTitle(getNewValue(event.getTitle(), request.getTitle()));
        return newEvent;
    }

    public static Event mapToEvent(Event event,
                                   UpdateEventAdminRequest request,
                                   Category category,
                                   LocalDateTime eventDate,
                                   LocationDto locationDto,
                                   State state,
                                   LocalDateTime publishedOn) {
        Event newEvent = event;
        newEvent.setAnnotation(getNewValue(event.getAnnotation(), request.getAnnotation()));
        newEvent.setCategory(getNewValue(event.getCategory(), category));
        newEvent.setDescription(getNewValue(event.getDescription(), request.getDescription()));
        newEvent.setEventDate(getNewValue(event.getEventDate(), eventDate));
        if (locationDto != null) {
            newEvent.getLocation().setLat(locationDto.getLat());
            newEvent.getLocation().setLon(locationDto.getLon());
        }
        newEvent.setPaid(getNewValue(event.getPaid(), request.getPaid()));
        newEvent.setParticipantLimit(getNewValue(event.getParticipantLimit(), request.getParticipantLimit()));
        newEvent.setRequestModeration(getNewValue(event.getRequestModeration(), request.getRequestModeration()));
        newEvent.setPublishedOn(getNewValue(event.getPublishedOn(), publishedOn));
        newEvent.setState(getNewValue(event.getState(), state));
        newEvent.setTitle(getNewValue(event.getTitle(), request.getTitle()));
        return newEvent;
    }

    private static <T> T getNewValue(T oldValue, T newValue) {
        return newValue != null ? newValue : oldValue;
    }

    public static List<EventFullDto> mapToEventFullDto(List<Event> events) {
        List<EventFullDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(mapToEventFullDto(event));
        }
        return dtos;
    }

    public static State getStateByStateAction(StateAction stateAction) {
        State state = null;
        switch (stateAction) {
            case REJECT_EVENT:
                state = State.CANCELED;
                break;
            case SEND_TO_REVIEW:
                state = State.PENDING;
                break;
            case PUBLISH_EVENT:
                state = State.PUBLISHED;
                break;
            case CANCEL_REVIEW:
                state = State.CANCELED;
        }
        return state;
    }

}
