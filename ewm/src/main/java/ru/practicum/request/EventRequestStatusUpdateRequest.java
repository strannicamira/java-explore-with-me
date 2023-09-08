package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EventRequestStatusUpdateRequest {
    //Идентификаторы запросов на участие в событии текущего пользователя
    //example: List [ 1, 2, 3 ]
//    @NotNull
    private List<Integer> requestIds;

    //Новый статус запроса на участие в событии текущего пользователя
    //Enum:
    //[ CONFIRMED, REJECTED ]
    //example: CONFIRMED
//    @NotNull
    private String status;
}
