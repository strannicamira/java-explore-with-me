package ru.practicum.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.exceptionhandler.NotFoundException;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationService;
import ru.practicum.user.User;
import ru.practicum.user.UserService;
import ru.practicum.util.ServiceImplUtils;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;
import static ru.practicum.util.Constants.TIME_PATTERN;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationService locationService;

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventRequest newEventRequest, Integer userId) {
        log.info("Create event");

        //category
        Integer categoryId = newEventRequest.getCategory();
        Category category = categoryService.findCategoryById(categoryId);

        //createdOn
        LocalDateTime createdOn = LocalDateTime.now();

        //initiator
        List<User> initiatorUsers = userService.findUsersByIds(null, null, new Integer[]{userId});
        User initiator = initiatorUsers != null && !initiatorUsers.isEmpty() && initiatorUsers.size() >= 1 ? initiatorUsers.get(0) : null;

        //location
        LocationDto locationDto = newEventRequest.getLocation();
        Location location = locationService.createLocation(locationDto);

        //save to DB as Event
        Event entity = EventMapper.mapToEvent(newEventRequest, category, createdOn, initiator, location);
        Event savedEvent = eventRepository.save(entity);

        //return for controller as EventFullDto
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(savedEvent);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> findEventShortDtos(Integer from, Integer size, Integer userId) {
        log.info("Search events by user with id {}", userId);
        userService.findUserById(userId);
        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);
        Page<Event> foundEvents = eventRepository.findAll(page);
        List<Event> eventsList = ServiceImplUtils.mapToList(foundEvents);
        List<EventShortDto> eventsDtoList = EventMapper.mapToEventShortDto(eventsList);
        return eventsDtoList;
    }

    @Override
    public EventFullDto findEventFullDtoById(Integer userId, Integer eventId) {
        Event event = findEventById(userId, eventId);
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(event);
        return eventFullDto;
    }

    private Event findEventById(Integer userId, Integer eventId) {
        userService.findUserById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        return event;
    }

    @Override
    public EventFullDto updateEvent(UpdateEventUserRequest request, Integer userId, Integer eventId) {
        Event event = findEventById(userId, eventId);
        Event updatedEvent = EventMapper.mapToEvent(event, request);
        Event savedEvent = eventRepository.save(updatedEvent);
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(savedEvent);
        return eventFullDto;
    }

    @Override
    public List<EventFullDto> findEventFullDtos(Integer[] userIds, String[] stateNames, Integer[] categoryIds, String rangeStart, String rangeEnd, Integer from, Integer size) {

        BooleanExpression byUserIds = QEvent.event.initiator.id.in(userIds);

        List<State> states = Arrays.stream(stateNames).map(State::forValues).collect(Collectors.toList());
        //    List<State> states = State.forValues(stateNames);
        BooleanExpression byStates = QEvent.event.state.in(states);

        BooleanExpression byCategory = QEvent.event.category.id.in(categoryIds);

        LocalDateTime startLDT = LocalDateTime.parse(URLDecoder.decode(rangeStart), DateTimeFormatter.ofPattern(TIME_PATTERN));
        LocalDateTime endtLDT = LocalDateTime.parse(URLDecoder.decode(rangeEnd), DateTimeFormatter.ofPattern(TIME_PATTERN));
        BooleanExpression byStart = QEvent.event.eventDate.after(startLDT);
        BooleanExpression byEnd = QEvent.event.eventDate.before(endtLDT);

        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);

        Iterable<Event> foundEvents = eventRepository.findAll(byUserIds.and(byStates).and(byCategory).and(byStart).and(byEnd), page);
        List<Event> eventsList = ServiceImplUtils.mapToList(foundEvents);
        List<EventFullDto> eventFullDtos = EventMapper.mapToEventFullDto(eventsList);
        return eventFullDtos;
    }
}
