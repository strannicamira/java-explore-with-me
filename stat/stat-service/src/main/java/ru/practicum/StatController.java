package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping(path = "/hit")
    public EndpointHitDto post(@Valid @RequestBody EndpointHit endpointHit) {
        return statService.post(endpointHit);
    }

    @GetMapping(path = "/stats")
    public Iterable<ViewStats> getByUriArray(@RequestParam(name = "start", required = true) String start,
                                             @RequestParam(name = "end", required = true) String end,
                                             @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique,
                                             @RequestParam(name = "uris", required = false) String... uris
    ) {
        return statService.get(start, end, unique, uris);
    }

}
