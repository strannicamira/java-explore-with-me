package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    public EventRequestStatusUpdateResult() {
        this.confirmedRequests = new ArrayList<>();
        this.rejectedRequests = new ArrayList<>();
    }
}
