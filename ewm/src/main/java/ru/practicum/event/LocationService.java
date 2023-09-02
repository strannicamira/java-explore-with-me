package ru.practicum.event;

import org.springframework.transaction.annotation.Transactional;

public interface LocationService {
    @Transactional
    Location createLocation(LocationDto locationDto);
}
