package ru.practicum;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface StatService {
    EndpointHitDto post(EndpointHitDto endpointHitDto);

    Iterable<ViewStats> get(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, Boolean unique);

    EndpointHitDto post(EndpointHit endpointHit);

    Iterable<ViewStats> get(String start, String end, ArrayList<String> uris, Boolean unique);
}
