package ru.practicum.event;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.util.Constants.TIME_PATTERN;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Краткое описание события
    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    //id категории к которой относится событие
    @NotNull
    @Column(name = "CATEGORY_ID")
    private Integer category;

    //Полное описание события
    @NotBlank
    @Length(max = 7000, min = 20)
    private String description;

    // Дата и время на которые намечено событие.
    // Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    // Обратите внимание: дата и время на которые намечено событие не может быть раньше,
    // чем через два часа от текущего момента
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;

    //Широта и долгота места проведения события
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCATION_ID")
    private Location location;

//    @NotNull
//    @Column(name = "LOCATION_LAT")
//    private Float lat;
//    @NotNull
//    @Column(name = "LOCATION_LON")
//    private Float lon;

    //    Нужно ли оплачивать участие в событии
    private Boolean paid = false;

    //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @Column(name = "PARTICIPANT_LIMIT")
    private Integer participantLimit = 0;

    //Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.
    @Column(name = "REQUEST_MODERATION")
    private Boolean requestModeration = true;

    //Заголовок события
    @NotBlank
    @Length(max = 120, min = 3)
    private String title;
}
