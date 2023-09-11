package ru.practicum.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.NewEndpointHitRequest;
import ru.practicum.StatClient;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.exceptionhandler.EventBadRequestException;
import ru.practicum.exceptionhandler.EventConflictException;
import ru.practicum.exceptionhandler.EventNotPublishedException;
import ru.practicum.exceptionhandler.NotFoundException;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationService;
import ru.practicum.user.User;
import ru.practicum.user.UserService;
import ru.practicum.util.ServiceImplUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.util.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationService locationService;
    private final StatClient statClient;

    @Override
    @Transactional
    public EventFullDto createEventByPrivate(NewEventRequest newEventRequest, Integer userId) {
        log.info("[Log][Info] Create event");

        Event event = new Event();

        String annotation = newEventRequest.getAnnotation();
        event.setAnnotation(annotation);

        //category
        Integer categoryId = newEventRequest.getCategory();
        Category category = categoryService.findCategoryById(categoryId);
        event.setCategory(category);

        //createdOn
        LocalDateTime createdOn = LocalDateTime.now();
        event.setCreatedOn(createdOn);

        String description = newEventRequest.getDescription();
        event.setDescription(description);

        LocalDateTime eventDate = LocalDateTime.parse(URLDecoder.decode(newEventRequest.getEventDate()), DateTimeFormatter.ofPattern(TIME_PATTERN));
        event.setEventDate(eventDate);

        //initiator
        List<User> initiatorUsers = userService.findUsersByIds(null, null, new Integer[]{userId});
        User initiator = initiatorUsers != null && !initiatorUsers.isEmpty() && initiatorUsers.size() >= 1 ? initiatorUsers.get(0) : null;
        event.setInitiator(initiator);

        //location
        LocationDto locationDto = newEventRequest.getLocation();
        Location location = locationService.createLocation(locationDto);
        event.setLocation(location);

        Boolean paid = newEventRequest.getPaid();
        if (paid == null) {
            paid = false;
        }
        event.setPaid(paid);

        Integer participantLimit = newEventRequest.getParticipantLimit();
        if (participantLimit == null) {
            participantLimit = 0;
        }
        event.setParticipantLimit(participantLimit);

        Boolean requestModeration = newEventRequest.getRequestModeration();
        if (requestModeration == null) {
            requestModeration = true;
        }
        event.setRequestModeration(requestModeration);

        //initial state
        State state = State.PENDING;
        event.setState(state);

        String title = newEventRequest.getTitle();
        event.setTitle(title);

        //save to DB as Event
        Event savedEvent = eventRepository.save(event);

        //return for controller as EventFullDto
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(savedEvent);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> findEventsByPrivate(Integer from, Integer size, Integer userId) {
        log.info("[Log][Info] Search events by user with id {}", userId);
        userService.findUserById(userId);
        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);
        Page<Event> foundEvents = eventRepository.findAll(page);
        List<Event> eventsList = ServiceImplUtils.mapToList(foundEvents);
        List<EventShortDto> eventsDtoList = EventMapper.mapToEventShortDto(eventsList);
        return eventsDtoList;
    }

    @Override
    public EventFullDto findEventByIdByPrivate(Integer userId, Integer eventId) {
        log.info("[Log][Info] Search events by user with id {}", userId);
        Event event = findEventById(userId, eventId);
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(event);
        return eventFullDto;
    }

    @Override
    public Event findEventById(Integer eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        return event;
    }

    @Override
    public Event findEventById(Integer userId, Integer eventId) {
        userService.findUserById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        return event;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByPrivate(UpdateEventUserRequest request, Integer userId, Integer eventId) {
        log.info("[Log][Info] Update event with id {} by user with id {}", eventId, userId);

        //Event data to convert
        //---------------------------------------------
        //category
        Integer categoryId = request.getCategory();
        Category category = null;
        if (categoryId != null) {
            category = categoryService.findCategoryById(categoryId);
        }

        //eventDate
        String sEventDate = request.getEventDate();
        LocalDateTime eventDate = null;
        if (sEventDate != null) {
            eventDate = LocalDateTime.parse(URLDecoder.decode(sEventDate), DateTimeFormatter.ofPattern(TIME_PATTERN));
        }

        //location
        LocationDto locationDto = request.getLocation();

        StateAction stateAction = request.getStateAction();
        State state = null;
        if (stateAction != null) {
            state = EventMapper.getStateByStateAction(stateAction);
        }

        LocalDateTime publishedOn = null;
        if (state == State.PUBLISHED) {
            publishedOn = LocalDateTime.now();
        }

        //---------------------------------------------

        Event event = findEventById(userId, eventId);
        Event updatedEvent;
        Event savedEvent = null;
        if (event.getState() == State.PUBLISHED) {
            throw new EventConflictException("Event already published");
        }

        //Стоимость отменённого события должна соответствовать стоимости события до отмены
        if (stateAction == StateAction.CANCEL_REVIEW || stateAction == StateAction.SEND_TO_REVIEW) {
            updatedEvent = event;
            updatedEvent.setState(state);
            savedEvent = eventRepository.save(updatedEvent);
        } else {
            updatedEvent = EventMapper.mapToEvent(event, request, category, eventDate, locationDto, state, publishedOn);
            savedEvent = eventRepository.save(updatedEvent);
        }

        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(savedEvent);
        return eventFullDto;
    }

    @Override
    public List<Event> findEventsByAdmin(Integer[] userIds, String[] stateNames, Integer[] categoryIds,
                                         String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("[Log][Info] Find events by Admin");
        BooleanExpression byUserIds = null;
        if (userIds != null) {
            byUserIds = QEvent.event.initiator.id.in(userIds);
        }

        BooleanExpression byStates = null;
        if (stateNames != null) {
            List<State> states = Arrays.stream(stateNames).map(State::forValues).collect(Collectors.toList());
            byStates = QEvent.event.state.in(states);
        }

        BooleanExpression byCategory = null;
        if (categoryIds != null) {
            byCategory = QEvent.event.category.id.in(categoryIds);

        }

        BooleanExpression byStart = null;
        if (rangeStart != null) {
            LocalDateTime startLDT = LocalDateTime.parse(URLDecoder.decode(rangeStart), DateTimeFormatter.ofPattern(TIME_PATTERN));
            byStart = QEvent.event.eventDate.after(startLDT);
        }

        BooleanExpression byEnd = null;
        if (rangeEnd != null) {
            LocalDateTime endtLDT = LocalDateTime.parse(URLDecoder.decode(rangeEnd), DateTimeFormatter.ofPattern(TIME_PATTERN));
            byEnd = QEvent.event.eventDate.before(endtLDT);
        }

        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);

        Iterable<Event> foundEvents = null;
        if (byUserIds != null || byStates != null || byCategory != null || byStart != null || byEnd != null) {
            foundEvents = eventRepository.findAll(byUserIds.and(byStates).and(byCategory).and(byStart).and(byEnd), page);
        } else {
            foundEvents = eventRepository.findAll(page);
        }

        List<Event> eventsList = ServiceImplUtils.mapToList(foundEvents);
        return eventsList;
    }

    @Override
    public List<EventFullDto> findEventFullDtosByAdmin(Integer[] userIds, String[] stateNames, Integer[] categoryIds,
                                                       String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("[Log][Info] Find event full dtos by Admin");
        List<Event> eventsList = findEventsByAdmin(userIds, stateNames, categoryIds, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtos = EventMapper.mapToEventFullDto(eventsList);
        return eventFullDtos;
    }

    @Override
    public List<EventShortDto> findEventShortDtosByAdmin(List<Integer> eventIds) {
        log.info("[Log][Info] Find event short dtos by Admin");
        List<Event> eventsList = eventRepository.findAllById(eventIds);
        List<EventShortDto> eventShortDtos = EventMapper.mapToEventShortDto(eventsList);
        return eventShortDtos;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest request, Integer eventId) {
        log.info("[Log][Info] Update event by Admin");

        //Event data to convert
        //---------------------------------------------
        //category
        Integer categoryId = request.getCategory();
        Category category = null;
        if (categoryId != null) {
            category = categoryService.findCategoryById(categoryId);
        }

        //eventDate
        String sEventDate = request.getEventDate();
        LocalDateTime eventDate = null;
        if (sEventDate != null) {
            eventDate = LocalDateTime.parse(URLDecoder.decode(sEventDate), DateTimeFormatter.ofPattern(TIME_PATTERN));
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new EventBadRequestException("Event Date is in the past");
            }
        }

        //location
        LocationDto locationDto = request.getLocation();

        StateAction stateAction = request.getStateAction();
        State newEventState = null;
        if (stateAction != null) {
            newEventState = EventMapper.getStateByStateAction(stateAction);
        }

        LocalDateTime publishedOn = null;
        if (newEventState == State.PUBLISHED) {
            publishedOn = LocalDateTime.now().plusDays(1);
        }
        //---------------------------------------------


        Event event = findEventById(eventId);
        Event updatedEvent = null;
        Event savedEvent = null;

        if (stateAction == StateAction.REJECT_EVENT && event.getState() == State.PUBLISHED) {
            throw new EventConflictException("Event already published. Cannot be canceled.");
        } else if (stateAction == StateAction.REJECT_EVENT) {
            updatedEvent = event;
            updatedEvent.setState(newEventState);
            savedEvent = eventRepository.save(updatedEvent);
        } else if (stateAction == StateAction.PUBLISH_EVENT && event.getState() == State.CANCELED) {
            throw new EventConflictException("Event already canceled. Cannot be published.");
        } else {
            if (event.getState() == State.PUBLISHED) {
                throw new EventConflictException("Event already published. Cannot be double published.");
            }
            updatedEvent = EventMapper.mapToEvent(event, request, category, eventDate, locationDto, newEventState, publishedOn);
            savedEvent = eventRepository.save(updatedEvent);
        }

        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(savedEvent);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> findEventsByPublic(HttpServletRequest request, String text, Integer[] categoryIds, Boolean paid,
                                                  String rangeStart, String rangeEnd,
                                                  Boolean onlyAvailable,
                                                  String sort, Integer from, Integer size) {
        log.info("[Log][Info] Find events by public");

        BooleanExpression byPublished = QEvent.event.publishedOn.isNotNull();
        BooleanExpression byState = QEvent.event.state.eq(State.PUBLISHED);

        BooleanExpression byText = null;
        if (text != null) {
            String decodedText = URLDecoder.decode(text);
            BooleanExpression byAnnotation = QEvent.event.annotation.containsIgnoreCase(decodedText);
            BooleanExpression byDescription = QEvent.event.description.containsIgnoreCase(decodedText);
            byText = byAnnotation.or(byDescription);
        }

        BooleanExpression byCategory = null;
        if (categoryIds != null) {
            byCategory = QEvent.event.category.id.in(categoryIds);
        }

        BooleanExpression byPaid = null;
        if (paid != null) {
            byPaid = QEvent.event.paid.eq(paid);
        }

        BooleanExpression byRange = null;
        LocalDateTime now = LocalDateTime.now();
        if (rangeStart == null && rangeEnd == null) {
            BooleanExpression byNow = QEvent.event.eventDate.after(now);
            byRange = byNow;
        } else {
            LocalDateTime startLDT = LocalDateTime.parse(URLDecoder.decode(rangeStart), DateTimeFormatter.ofPattern(TIME_PATTERN));
            LocalDateTime endtLDT = LocalDateTime.parse(URLDecoder.decode(rangeEnd), DateTimeFormatter.ofPattern(TIME_PATTERN));
            if (startLDT.isAfter(endtLDT)) {
                throw new EventBadRequestException("Start is before End for event");
            }
            BooleanExpression byStart = QEvent.event.eventDate.after(startLDT);
            BooleanExpression byEnd = QEvent.event.eventDate.before(endtLDT);
            byRange = byStart.and(byEnd);
        }

        BooleanExpression byLimit = null;
        if (onlyAvailable != null && onlyAvailable == true) {
            byLimit = QEvent.event.participantLimit.lt(QEvent.event.confirmedRequests);
        }

        EventSort eventSort = EventSort.forValues(sort);
        Sort pageSort = SORT_BY_EVENT_DATE_ASC;
        if (eventSort == EventSort.EVENT_DATE) {
            pageSort = SORT_BY_EVENT_DATE_ASC;
        } else if (eventSort == EventSort.VIEWS) {
            pageSort = SORT_BY_VIEWS_ASC;

        }

        Pageable page = ServiceImplUtils.getPage(from, size, pageSort);

        Iterable<Event> foundEvents = null;
        if (byPublished != null || byState != null || byText != null || byCategory != null || byRange != null || byLimit != null) {
            foundEvents = eventRepository.findAll(byPublished.and(byState).and(byText).and(byCategory).and(byPaid).and(byRange).and(byLimit), page);
        } else {
            foundEvents = eventRepository.findAll(page);
        }

        //--------------------------------------
        // Create NewEndpointHitRequest to post to statistics

        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());

        NewEndpointHitRequest newEndpointHitRequest = new NewEndpointHitRequest();
        newEndpointHitRequest.setApp("ewm-main-service");
        newEndpointHitRequest.setIp(request.getRemoteAddr());
        String requestURI = request.getRequestURI();
        newEndpointHitRequest.setUri(requestURI);

        String requestedOn = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN));
        newEndpointHitRequest.setTimestamp(requestedOn);

        statClient.post(newEndpointHitRequest);

        //------------------------------------------------------------

        List<Event> eventsList = ServiceImplUtils.mapToList(foundEvents);
        List<EventShortDto> eventFullDtos = EventMapper.mapToEventShortDto(eventsList);
        return eventFullDtos;
    }

    @Override
    public EventFullDto findEventByIdByPublic(HttpServletRequest request, Integer eventId) {
        log.info("[Log][Info] Search events by  id {}", eventId);

        Event event = findEventById(eventId);

        if (event.getState() != State.PUBLISHED) {
            throw new EventNotPublishedException("Event Not Published");
        }

        //--------------------------------------
        // Create NewEndpointHitRequest to post to statistics

        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());

        NewEndpointHitRequest newEndpointHitRequest = new NewEndpointHitRequest();
        newEndpointHitRequest.setApp("ewm-main-service");
        newEndpointHitRequest.setIp(request.getRemoteAddr());
        String requestURI = request.getRequestURI();
        newEndpointHitRequest.setUri(requestURI);

        String requestedOn = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN));
        newEndpointHitRequest.setTimestamp(requestedOn);

        statClient.post(newEndpointHitRequest);

        //------------------------------------------------------------

        String start = event.getCreatedOn().minusDays(1).format(DateTimeFormatter.ofPattern(TIME_PATTERN));
        String end = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern(TIME_PATTERN));

        ResponseEntity<Object> response = statClient.get(start, end, true, new String[]{requestURI});
        ArrayList<Object> objectsList = (ArrayList<Object>) response.getBody();
        Map<Object, Object> objectsMap = (Map<Object, Object>) objectsList.get(0);
        Integer hits = (Integer) objectsMap.get("hits");
        event.setViews(hits);
        eventRepository.save(event);

        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(event);
        return eventFullDto;
    }
}



