package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.CategoryDto;
import ru.practicum.comment.CommentResponseDto;
import ru.practicum.location.LocationDto;
import ru.practicum.user.UserShortDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static ru.practicum.util.Constants.TIME_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventWithCommentsFullDto {
    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Integer confirmedRequests;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private String createdOn;

    @NotBlank
    @Length(max = 7000, min = 20)
    private String description;

    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private String eventDate;

    @NotNull
    private Integer id;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private LocationDto location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private String publishedOn;

    private Boolean requestModeration = true;

    @NotNull
    private State state;

    @NotBlank
    @Length(max = 120, min = 3)
    private String title;

    private Integer views;

    private List<CommentResponseDto> comments;
}
