package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.CategoryDto;
import ru.practicum.user.UserShortDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static ru.practicum.util.Constants.TIME_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Integer confirmedRequests;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private String eventDate;

    @NotNull
    private Integer id;

    @NotNull
    private UserShortDto initiator;

    private Boolean paid = false;

    @NotBlank
    @Length(max = 120, min = 3)
    private String title;

    private Integer views;
}
