package ru.practicum;

import java.util.List;

public interface StatService {
    EndpointHit post(NewEndpointHitRequest newEndpointHitRequest);

    List<ViewStats> get(String start, String end, Boolean unique, String[] uris);
}
