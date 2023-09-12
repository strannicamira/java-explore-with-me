package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit post(@Valid @RequestBody NewEndpointHitRequest newEndpointHitRequest) {
        return statService.post(newEndpointHitRequest);
    }

    @GetMapping(path = "/stats")
    public List<ViewStats> getByUriArray(@RequestParam(name = "start") String start,
                                         @RequestParam(name = "end") String end,
                                         @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique,
                                         @RequestParam(name = "uris", required = false) String... uris
    ) {
        return statService.get(start, end, unique, uris);
    }

}
