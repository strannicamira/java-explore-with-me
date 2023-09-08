package ru.practicum.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public Location createLocation(LocationDto locationDto) {
        log.info("[Log][Info] Create location");
        Location savedLocation = locationRepository.save(LocationMapper.mapToLocation(locationDto));
        return savedLocation;
    }
}
