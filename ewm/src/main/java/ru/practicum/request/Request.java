package ru.practicum.request;

import lombok.*;
import ru.practicum.event.Event;
import ru.practicum.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class Request {
    //Идентификатор заявки
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Дата и время создания заявки
    //example: 2022-09-06T21:10:05.432
    @NotNull
    private LocalDateTime created;

    //Идентификатор события
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_ID")
    private Event event;

    //Идентификатор пользователя, отправившего заявку
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUESTER_ID")
    private User requester;

    //Статус заявки
    //example: PENDING
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
