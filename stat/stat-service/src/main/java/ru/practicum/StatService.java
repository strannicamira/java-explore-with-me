package ru.practicum;

import java.util.List;

public interface StatService {
    EndpointHitDto post(EndpointHit endpointHit);

    List<ViewStats> get(String start, String end, Boolean unique, String[] uris);
}
