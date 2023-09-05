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
        log.info("Create location");
        Location savedLocation = locationRepository.save(LocationMapper.mapToLocation(locationDto));
        return savedLocation;
    }

    @Override
    @Transactional
    public Location updateLocation(LocationDto locationDto) {
        log.info("Update location");
        Location foundLocation = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
        foundLocation.setLat(locationDto.getLat() != null ? locationDto.getLat() : foundLocation.getLat());
        foundLocation.setLon(locationDto.getLon() != null ? locationDto.getLon() : foundLocation.getLon());
        Location savedLocation = locationRepository.save(LocationMapper.mapToLocation(locationDto));
        return savedLocation;
    }

    @Override
    @Transactional
    public void deleteLocation(Integer id) {
        log.info("Create location");
        locationRepository.deleteById(id);
    }
}
