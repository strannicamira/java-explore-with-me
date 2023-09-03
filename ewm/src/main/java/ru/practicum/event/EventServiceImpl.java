package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryDto;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.CategoryService;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationService;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserService;
import ru.practicum.user.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

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
        Integer category = newEventRequest.getCategory();
//        CategoryDto categoryById = categoryService.findCategoryById(category);
        Category categoryById = categoryService.findCategoryById(category);
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(categoryById);


        LocationDto locationDto = newEventRequest.getLocation();
        Location location = locationService.createLocation(locationDto);

        Event event = eventRepository.save(EventMapper.mapToEvent(newEventRequest, categoryById, location));
        LocalDateTime createdOn = LocalDateTime.now();
        List<User> initiatorUsers = userService.findUsersByIds(null, null, new Integer[]{userId});
        List<UserShortDto> initiators = UserMapper.mapToUserShortDto(initiatorUsers);
        UserShortDto initiator = initiators != null && !initiators.isEmpty() && initiators.size() >= 1 ? initiators.get(0) : null;


        EventFullDto eventFullDto = EventMapper.mapToEventFullDto(event, categoryDto,
                null, createdOn, initiator, null, State.PENDING, null);
        return eventFullDto;
    }

}
