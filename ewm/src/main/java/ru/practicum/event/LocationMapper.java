package ru.practicum.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {
    public static Location mapToLocation(LocationDto dto) {
        Location location = new Location();
        location.setLat(dto.getLat());
        location.setLon(dto.getLon());
        return location;
    }

    public static LocationDto mapToLocationDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setLat(location.getLat());
        dto.setLon(location.getLon());
        return dto;
    }
}
