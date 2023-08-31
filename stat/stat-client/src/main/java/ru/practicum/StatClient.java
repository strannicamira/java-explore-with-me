package ru.practicum;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class StatClient extends BaseClient {

    public StatClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> post(Integer userId, NewEndpointHitRequest newEndpointHitRequest) {
        return post("/hit", userId, newEndpointHitRequest);

    }

    public ResponseEntity<Object> get(Integer userId, String start, String end, Boolean unique, String[] uris) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique,
                "uris", uris
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", Long.valueOf(userId), parameters);


    }
}
