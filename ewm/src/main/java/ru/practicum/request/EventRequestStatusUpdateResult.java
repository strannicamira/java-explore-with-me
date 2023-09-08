package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

////Результат подтверждения/отклонения заявок на участие в событии
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
//    @NotNull
    private List<ParticipationRequestDto> confirmedRequests;
//    @NotNull
    private List<ParticipationRequestDto> rejectedRequests;
}