package ru.practicum.location;

import org.springframework.transaction.annotation.Transactional;

public interface LocationService {
    @Transactional
    Location createLocation(LocationDto locationDto);
}
