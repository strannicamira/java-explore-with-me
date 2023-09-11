package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventRequest {
    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    @NotNull
    private Integer category;

    @NotBlank
    @Length(max = 7000, min = 20)
    private String description;

    @NotNull
    private String eventDate;

    @NotNull
    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank
    @Length(max = 120, min = 3)
    private String title;
}
