package ru.practicum.compilation;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EVENT_COMPILATION", schema = "public")
public class EventCompilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
//    @Id
    @Column(name = "EVENT_ID")
    private Integer eventId;
    @NotNull
//    @Id
    @Column(name = "COMPILATION_ID")
    private Integer compilationId;
}
