package ru.practicum.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.exceptionhandler.EventBadRequestException;
import ru.practicum.exceptionhandler.EventNotPublishedException;
import ru.practicum.exceptionhandler.EventPublishedException;
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

import static ru.practicum.util.Constants.*;

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
    public EventFullDto createEventByPrivate(NewEventRequest newEventRequest, Integer userId) {
        log.info("[Log][Info] Create event");

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
    public  Event findEventById(Integer eventId) {
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
        //        Category category = categoryId != null ? categoryService.findCategoryById(categoryId) : null;
        Category category = null;
        if (categoryId != null) {
            category = categoryService.findCategoryById(categoryId);
        }

        //eventDate
        String sEventDate = request.getEventDate();
//        LocalDateTime eventDate = LocalDateTime.parse(URLDecoder.decode(sEventDate), DateTimeFormatter.ofPattern(TIME_PATTERN));
        LocalDateTime eventDate = null;
        if (sEventDate != null) {
            eventDate = LocalDateTime.parse(URLDecoder.decode(sEventDate), DateTimeFormatter.ofPattern(TIME_PATTERN));
        }

        //location
        LocationDto locationDto = request.getLocation();
        //---------------------------------------------

        Event event = findEventById(userId, eventId);
        Event updatedEvent = EventMapper.mapToEvent(event, request, category, eventDate, locationDto);
        Event savedEvent = eventRepository.save(updatedEvent);
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(savedEvent);
        return eventFullDto;
    }

    @Override
    public List<Event> findEventsByAdmin(Integer[] userIds, String[] stateNames, Integer[] categoryIds,
                                         String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("[Log][Info] Find events by Admin");

        BooleanExpression byUserIds = QEvent.event.initiator.id.in(userIds);

        List<State> states = Arrays.stream(stateNames).map(State::forValues).collect(Collectors.toList());
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

//    @Override
//    public List<EventShortDto> findEventShortDtosByAdmin(Integer[] userIds, String[] stateNames, Integer[] categoryIds,
//                                                         String rangeStart, String rangeEnd, Integer from, Integer size) {
//        log.info("[Log][Info] Find event short dtos by Admin");
//        List<Event> eventsList = findEventsByAdmin(userIds, stateNames, categoryIds, rangeStart, rangeEnd, from, size);
//        List<EventShortDto> eventShortDtos = EventMapper.mapToEventShortDto(eventsList);
//        return eventShortDtos;
//    }

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

        Event event = findEventById(eventId);

        if (event.getState() == State.PUBLISHED && request.getStateAction() == StateAction.PUBLISH_EVENT) {
            throw new EventPublishedException("Event already published");
        }

        //Event data to convert
        //---------------------------------------------
        //category
        Integer categoryId = request.getCategory();
        //        Category category = categoryId != null ? categoryService.findCategoryById(categoryId) : null;
        Category category = null;
        if (categoryId != null) {
            category = categoryService.findCategoryById(categoryId);
        }

        //eventDate
        String sEventDate = request.getEventDate();
//        LocalDateTime eventDate = LocalDateTime.parse(URLDecoder.decode(sEventDate), DateTimeFormatter.ofPattern(TIME_PATTERN));
        LocalDateTime eventDate = null;
        if (sEventDate != null) {
            eventDate = LocalDateTime.parse(URLDecoder.decode(sEventDate), DateTimeFormatter.ofPattern(TIME_PATTERN));
        }

        //location
        LocationDto locationDto = request.getLocation();
        //---------------------------------------------

        Event updatedEvent = EventMapper.mapToEvent(event, request, category, eventDate, locationDto);
        Event savedEvent = eventRepository.save(updatedEvent);
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(savedEvent);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> findEventsByPublic(String text, Integer[] categoryIds, Boolean paid,
                                                  String rangeStart, String rangeEnd,
                                                  Boolean onlyAvailable,
                                                  String sort, Integer from, Integer size) {
        log.info("[Log][Info] Find events by public");

        BooleanExpression published = QEvent.event.publishedOn.isNotNull();

        String decodedText = URLDecoder.decode(text);
        BooleanExpression byAnnotation = QEvent.event.annotation.containsIgnoreCase(decodedText);
        BooleanExpression byDescription = QEvent.event.description.containsIgnoreCase(decodedText);
        BooleanExpression byText = byAnnotation.or(byDescription);

        BooleanExpression byCategory = QEvent.event.category.id.in(categoryIds);

        BooleanExpression byPaid = QEvent.event.paid.eq(paid);

        LocalDateTime now = LocalDateTime.now();
        BooleanExpression byRange;

        if (rangeStart == null && rangeEnd == null) {
            BooleanExpression byNow = QEvent.event.eventDate.after(now);
            byRange = byNow;
        } else {

            LocalDateTime startLDT = LocalDateTime.parse(URLDecoder.decode(rangeStart), DateTimeFormatter.ofPattern(TIME_PATTERN));
            LocalDateTime endtLDT = LocalDateTime.parse(URLDecoder.decode(rangeEnd), DateTimeFormatter.ofPattern(TIME_PATTERN));

            if (startLDT.isAfter(endtLDT)){
                throw  new EventBadRequestException("Start is before End for event");
            }

            BooleanExpression byStart = QEvent.event.eventDate.after(startLDT);
            BooleanExpression byEnd = QEvent.event.eventDate.before(endtLDT);
            byRange = byStart.and(byEnd);
        }
        BooleanExpression byLimit = null;
        if (onlyAvailable) {
            byLimit = QEvent.event.participantLimit.lt(QEvent.event.confirmedRequests);
        }


        EventSort eventSort = EventSort.forValues(sort);
        //TODO: Default value???
        Sort pageSort = SORT_BY_ID_ASC;
        if (eventSort == EventSort.EVENT_DATE) {
            pageSort = SORT_BY_EVENT_DATE_ASC;
        } else if (eventSort == EventSort.VIEWS) {
            pageSort = SORT_BY_VIEWS_ASC;

        }
        Pageable page = ServiceImplUtils.getPage(from, size, pageSort);

        BooleanExpression findExpression = published.and(byText).and(byCategory).and(byPaid).and(byRange).and(byLimit);
        Iterable<Event> foundEvents = eventRepository.findAll(findExpression, page);
        //TODO: add statistic
        List<Event> eventsList = ServiceImplUtils.mapToList(foundEvents);
        List<EventShortDto> eventFullDtos = EventMapper.mapToEventShortDto(eventsList);
        return eventFullDtos;
    }

    @Override
    public EventFullDto findEventByIdByPublic(Integer eventId) {
        log.info("[Log][Info] Search events by  id {}", eventId);
        //TODO: add statistic
        Event event = findEventById(eventId);
        if (event.getState()!=State.PUBLISHED){
            throw new EventNotPublishedException("Event Not Published");
        }
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(event);
        return eventFullDto;
    }
}
