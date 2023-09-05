package ru.practicum.location;

import org.springframework.transaction.annotation.Transactional;

public interface LocationService {
    Location createLocation(LocationDto locationDto);

    Location updateLocation(LocationDto locationDto);

    @Transactional
    void deleteLocation(Integer id);
}
