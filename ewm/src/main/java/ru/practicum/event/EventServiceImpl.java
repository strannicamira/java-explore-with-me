package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryService;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationService;
import ru.practicum.user.User;
import ru.practicum.user.UserService;
import ru.practicum.util.ServiceImplUtils;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

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
        Event event = eventRepository.save(entity);

        //return for controller as EventFullDto
        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(event);
        return eventFullDto;
    }

    @Override
    public List<EventShortDto> findEventShortDtos(Integer from, Integer size, Integer userId) {
        log.info("Search events by user with id {}", userId);
        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);
        Page<Event> foundEvents = eventRepository.findAll(page);
        List<Event> eventsList = ServiceImplUtils.mapToList(foundEvents);
        List<EventShortDto> eventsDtoList = EventMapper.mapToEventShortDto(eventsList);
        return eventsDtoList;
    }

}
