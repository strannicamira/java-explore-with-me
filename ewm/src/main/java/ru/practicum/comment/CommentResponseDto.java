package ru.practicum.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static ru.practicum.util.Constants.TIME_PATTERN;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    @NotNull
    private Integer id;
    @NotBlank
    @Length(max = 250, min = 1)
    private String text;
    @NotNull
    private String author;
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private String created;
}
