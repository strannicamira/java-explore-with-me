package ru.practicum;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class StatClient extends BaseClient {

    public StatClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> post(NewEndpointHitRequest newEndpointHitRequest) {
        return post("/hit", newEndpointHitRequest);

    }

    public ResponseEntity<Object> get(String start, String end, Boolean unique, String[] uris) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "unique", unique,
                "uris", uris
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);


    }
}
