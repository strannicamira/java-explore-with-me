package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestPrivateController {

    private final RequestService requestService;

    //POST
    ///users/{userId}/requests
    //Добавление запроса от текущего пользователя на участие в событии

    //нельзя добавить повторный запрос (Ожидается код ошибки 409)
    //инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
    //нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
    //если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
    //если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable(name = "userId") Integer userId, @RequestParam(name = "eventId") Integer eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> findRequestsById(@PathVariable(name = "userId") Integer userId) {
        return requestService.findRequestById(userId);
    }

    @PatchMapping(value = "/{requestId}/cancel")
    public ParticipationRequestDto updateRequest(@Valid @NotNull @PathVariable(name = "userId") Integer userId,
                                                 @Valid @NotNull @PathVariable(name = "requestId") Integer requestId) {
        return requestService.updateRequest(userId, requestId);
    }
}
