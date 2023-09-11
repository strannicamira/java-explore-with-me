package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events/{eventId}/requests")
public class EventRequestPrivateController {

    private final RequestService requestService;

    //Получение информации о запросах на участие в событии текущего пользователя
    @GetMapping
    public List<ParticipationRequestDto> findEventRequests(@PathVariable(name = "userId") Integer userId,
                                                           @PathVariable(name = "eventId") Integer eventId) {
        return requestService.findEventRequests(userId, eventId);
    }

    //Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя

    //если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
    //нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
    //статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
    //если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
    @PatchMapping
    public EventRequestStatusUpdateResult updateEventRequest(@Valid @RequestBody EventRequestStatusUpdateRequest request,
                                                             @PathVariable(name = "userId") Integer userId,
                                                             @PathVariable(name = "eventId") Integer eventId) {
        return requestService.updateEventRequest(request, userId, eventId);
    }
}
