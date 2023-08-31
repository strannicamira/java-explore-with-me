package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatClient statClient;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> post(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                       @Valid @RequestBody NewEndpointHitRequest newEndpointHitRequest) {
        return statClient.post(userId, newEndpointHitRequest);
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<Object> getByUriArray(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @RequestParam(name = "start", required = true) String start,
                                                @RequestParam(name = "end", required = true) String end,
                                                @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique,
                                                @RequestParam(name = "uris", required = false) String... uris
    ) {
        return statClient.get(userId, start, end, unique, uris);
    }

}
