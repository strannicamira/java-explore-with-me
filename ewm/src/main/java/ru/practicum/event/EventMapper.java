package ru.practicum.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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


    public static Event mapToEvent(Event event, UpdateEventUserRequest request, Category category, LocalDateTime eventDate, Location location) {
        Event newEvent = event;
        newEvent.setAnnotation(getNewValue(event.getAnnotation(), request.getAnnotation()));
        newEvent.setCategory(getNewValue(event.getCategory(), category));
        newEvent.setDescription(getNewValue(event.getDescription(), request.getDescription()));
        newEvent.setEventDate(getNewValue(event.getEventDate(), eventDate));
        newEvent.setLocation(getNewValue(event.getLocation(), location));
        newEvent.setPaid(getNewValue(event.getPaid(), request.getPaid()));
        newEvent.setParticipantLimit(getNewValue(event.getParticipantLimit(), request.getParticipantLimit()));
        newEvent.setRequestModeration(getNewValue(event.getRequestModeration(), request.getRequestModeration()));
        newEvent.setState(getNewValue(event.getState(), request.getStateAction()));
        newEvent.setTitle(getNewValue(event.getTitle(), request.getTitle()));
        return newEvent;
    }


    public static Event mapToEvent(Event event, UpdateEventAdminRequest request, Category category, LocalDateTime eventDate, LocationDto locationDto) {
        Event newEvent = event;
        newEvent.setAnnotation(getNewValue(event.getAnnotation(), request.getAnnotation()));
        newEvent.setCategory(getNewValue(event.getCategory(), category));
        newEvent.setDescription(getNewValue(event.getDescription(), request.getDescription()));
        newEvent.setEventDate(getNewValue(event.getEventDate(), eventDate));
//        newEvent.setLocation(getNewValue(event.getLocation(), location));
//        if(event.getLocation()!=null && locationDto!= null){
//            newEvent.getLocation().setLat(getNewValue(event.getLocation().getLat(), locationDto.getLat()));
//            newEvent.getLocation().setLon(getNewValue(event.getLocation().getLon(), locationDto.getLon()));
//        } else if(event.getLocation()==null ){
//            newEvent.getLocation().setLat(locationDto.getLat());
//            newEvent.getLocation().setLon( locationDto.getLon());
//        }

        if(locationDto!= null){
            newEvent.getLocation().setLat(locationDto.getLat());
            newEvent.getLocation().setLon( locationDto.getLon());
        }

        newEvent.setPaid(getNewValue(event.getPaid(), request.getPaid()));
        newEvent.setParticipantLimit(getNewValue(event.getParticipantLimit(), request.getParticipantLimit()));
        newEvent.setRequestModeration(getNewValue(event.getRequestModeration(), request.getRequestModeration()));
        newEvent.setState(getNewValue(event.getState(), request.getStateAction()));
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


}
