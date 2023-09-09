package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {

    private final EventService eventService;

    //GET
    ///events
    //Получение событий с возможностью фильтрации

    //это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
    //текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
    //если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
    //информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
    //информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
    @GetMapping
    public List<EventShortDto> findEventsByPublic(
            @RequestParam(name = "text", required = false) String text, //текст для поиска в содержимом аннотации и подробном описании события
            @RequestParam(name = "categories", required = false) Integer[] categoryIds, //список id категорий в которых будет вестись поиск
            @RequestParam(name = "paid", required = false) Boolean paid, //поиск только платных/бесплатных событий
            @RequestParam(name = "rangeStart", required = false) String rangeStart, //дата и время не раньше которых должно произойти событие
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd, //дата и время не позже которых должно произойти событие
            @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable, //только события у которых не исчерпан лимит запросов на участие
            //Default value : false
            @RequestParam(name = "sort", required = false) String sort, //Вариант сортировки: по дате события или по количеству просмотров
            //Available values : EVENT_DATE, VIEWS
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from, //количество событий, которые нужно пропустить для формирования текущего набора
            //Default value : 0
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size //количество событий в наборе
            //Default value : 10
    ) {
        return eventService.findEventsByPublic(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    //GET
    ///events/{id}
    //Получение подробной информации об опубликованном событии по его идентификатору
    //
    //Jump to path
    //Обратите внимание:
    //
    //событие должно быть опубликовано
    //информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
    //информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
    //В случае, если события с заданным id не найдено, возвращает статус код 404
    @GetMapping(value = "/{id}")
    public EventFullDto findEventByIdByPublic(HttpServletRequest request,
                                              @PathVariable(name = "id") Integer eventId) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.findEventByIdByPublic(request, eventId);
    }
}
