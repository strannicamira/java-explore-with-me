package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping(path = "/hit")
    public EndpointHit create(@Valid @RequestBody EndpointHit endpointHit) {
        return statService.create(endpointHit);
    }

}
