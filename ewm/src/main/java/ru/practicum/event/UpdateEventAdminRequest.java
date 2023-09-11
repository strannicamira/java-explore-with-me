package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.LocationDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    @Length(max = 2000, min = 20)
    private String annotation;

    private Integer category;

    @Length(max = 7000, min = 20)
    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    StateAction stateAction;

    @Length(max = 120, min = 3)
    private String title;
}
