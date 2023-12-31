package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatClientController {
    private final StatClient statClient;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> post(
            @Valid @RequestBody NewEndpointHitRequest newEndpointHitRequest) {
        return statClient.post(newEndpointHitRequest);
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<Object> getByUriArray(
            @RequestParam(name = "start") String start,
            @RequestParam(name = "end") String end,
            @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique,
            @RequestParam(name = "uris", required = false) String... uris
    ) {
        return statClient.get(start, end, unique, uris);
    }
}
