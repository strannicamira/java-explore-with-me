package ru.practicum;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {

    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
