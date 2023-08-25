package ru.practicum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.practicum.Constants.TIME_PATTERN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatMapper {
    @Valid
    public static EndpointHitDto mapToEndpointHit(EndpointHit endpointHit){
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(endpointHit.getApp());
        endpointHitDto.setUri(endpointHit.getUri());
        endpointHitDto.setIp(endpointHit.getIp());

        String timestampString = URLDecoder.decode(endpointHit.getTimestamp());
        LocalDateTime timestamp = LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern(TIME_PATTERN));
        endpointHitDto.setTimestamp(timestamp);

        return endpointHitDto;
    }

    @Valid
    public static ViewStats mapToViewStats(EndpointHitDto endpointHitDto, Integer hits) {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(endpointHitDto.getApp());
        viewStats.setUri(endpointHitDto.getUri());
        viewStats.setHits(hits);
        return viewStats;
    }

    public static Iterable<ViewStats> mapToViewStats(Iterable<EndpointHitDto> endpointHits) {
        Iterable<ViewStats> viewStats;
        Map<ViewStats, Integer> viewStatsMap = new HashMap<>();
        Map<String, Integer> hitsByIp = new HashMap<>();

        for (EndpointHitDto endpointHitDto : endpointHits) {

            ViewStats view = new ViewStats();
            view.setApp(endpointHitDto.getApp());
            view.setUri(endpointHitDto.getUri());

            Integer hits = viewStatsMap.get(view);

            if (hits == null) {
                viewStatsMap.put(view, 1);
            } else {
                viewStatsMap.put(view, hits + 1);
            }
        }

        //TODO: use iterator
        for (Map.Entry<ViewStats, Integer> entry : viewStatsMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            entry.getKey().setHits(entry.getValue());
        }

        viewStats = viewStatsMap.keySet();

        return viewStats;
    }
}
