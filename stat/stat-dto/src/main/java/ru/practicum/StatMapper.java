package ru.practicum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.Constants.TIME_PATTERN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatMapper {
    @Valid
    public static EndpointHitDto mapToEndpointHit(EndpointHit endpointHit) {
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

    public static List<ViewStats> mapToViewStats(Iterable<EndpointHitDto> endpoints, Boolean unique) {
        Map<ViewStats, Map<String, Integer>> viewStatsMap = new HashMap<>();

        for (EndpointHitDto endpoint : endpoints) {

            ViewStats view = new ViewStats();
            view.setApp(endpoint.getApp());
            view.setUri(endpoint.getUri());

            Map<String, Integer> hitsByIp = viewStatsMap.get(view);

            if (hitsByIp == null) {
                hitsByIp = new HashMap<>();
                hitsByIp.put(endpoint.getIp(), 1);
            } else {
                Integer hit = hitsByIp.get(endpoint.getIp());
                if (hit == null) {
                    hitsByIp.put(endpoint.getIp(), 1);
                } else {
                    hitsByIp.put(endpoint.getIp(), hit + 1);
                }
            }
            viewStatsMap.put(view, hitsByIp);
        }

        //TODO: use iterator
        for (Map.Entry<ViewStats, Map<String, Integer>> entry : viewStatsMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            Integer sumHitsByIp = 0;
            if (unique) {
                sumHitsByIp = entry.getValue().size();
            } else {
                for (Map.Entry<String, Integer> hitsByIp : entry.getValue().entrySet()) {
                    sumHitsByIp += hitsByIp.getValue();
                }
            }

            entry.getKey().setHits(sumHitsByIp);
        }

        return new ArrayList<>(viewStatsMap.keySet());
    }
}
