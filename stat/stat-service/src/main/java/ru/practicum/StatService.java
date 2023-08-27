package ru.practicum;

public interface StatService {
    EndpointHitDto post(EndpointHit endpointHit);

    Iterable<ViewStats> get(String start, String end, Boolean unique, String[] uris);
}
