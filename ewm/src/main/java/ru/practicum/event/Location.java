package ru.practicum.event;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LOCATIONS", schema = "public")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(name = "LOCATION_LAT")
    private Float lat;
    @NotNull
    @Column(name = "LOCATION_LON")
    private Float lon;
}
