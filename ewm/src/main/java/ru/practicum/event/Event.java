package ru.practicum.event;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.category.Category;
import ru.practicum.location.Location;
import ru.practicum.user.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name = "CONFIRMED_REQUESTS")
    private Integer confirmedRequests = 0;

    @NotNull
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @NotBlank
    @Length(max = 7000, min = 20)
    private String description;

    @NotNull
    @FutureOrPresent
    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INITIATOR_ID")
    private User initiator;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCATION_ID")
    private Location location;

    @NotNull
    private Boolean paid = false;

    @Column(name = "PARTICIPANT_LIMIT")
    private Integer participantLimit = 0;

    @FutureOrPresent
    @Column(name = "PUBLISHED_ON")
    private LocalDateTime publishedOn;

    @Column(name = "REQUEST_MODERATION")
    private Boolean requestModeration = true;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private State state;

    @NotBlank
    @Length(max = 120, min = 3)
    private String title;

    private Integer views;
}
