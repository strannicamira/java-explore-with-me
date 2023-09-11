package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.event.EventService;
import ru.practicum.event.State;
import ru.practicum.exceptionhandler.NotFoundException;
import ru.practicum.exceptionhandler.RequestConflictException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Integer userId, Integer eventId) {
        log.info("[Log][Info] Create request for event with id {} by user with id {}", eventId, userId);

        Request request = new Request();
        //TODO: @FutureOrPresent  - plusDays(1) just for test
        request.setCreated(LocalDateTime.now().plusDays(1));

        User user = userService.findUserById(userId);
        request.setRequester(user);

        Event event = eventService.findEventById(userId, eventId);
        request.setEvent(event);

        Boolean requestModeration = event.getRequestModeration();
        Integer participantLimit = event.getParticipantLimit();

        if (requestModeration == false || participantLimit == 0) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }

        List<Request> existedRequests = requestRepository.findAllByRequesterIdAndEventId(userId, eventId);
        if (existedRequests != null && !existedRequests.isEmpty()) {
            throw new RequestConflictException("Request already exists");
        }

        if (event.getInitiator().getId() == userId) {
            throw new RequestConflictException("Requester is initiator of event");
        }

        if (event.getPublishedOn() == null && event.getState() != State.PUBLISHED) {
            throw new RequestConflictException("Event is not published");
        }

        if (participantLimit != 0 && participantLimit == event.getConfirmedRequests()) {
            throw new RequestConflictException("Participant limit for event is equal confirmed requests");
        }

        Request savedRequest = requestRepository.save(request);
        ParticipationRequestDto dto = RequestMapper.mapToParticipationRequestDto(savedRequest);

        if (requestModeration == false || participantLimit == 0) {
            Integer confirmedRequests = event.getConfirmedRequests();
            if (confirmedRequests == null) {
                event.setConfirmedRequests(1);
            } else {
                event.setConfirmedRequests(confirmedRequests + 1);
            }
            eventRepository.save(event);
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findRequestById(Integer userId) {
        log.info("[Log][Info] Search requests");
        User userById = userService.findUserById(userId);
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.mapToParticipationRequestDto(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto updateRequest(Integer userId, Integer requestId) {
        log.info("[Log][Info] Cancel request by user with id {} for request with id", userId, requestId);
        User userById = userService.findUserById(userId);

        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        if (request.getRequester().getId() != userId) {
            throw new RequestConflictException("User is not requester");
        }
        request.setStatus(Status.CANCELED);
        Request savedRequest = requestRepository.save(request);
        return RequestMapper.mapToParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> findEventRequests(Integer userId, Integer eventId) {
        log.info("[Log][Info] Search requests for event with id {} by user with id {}", eventId, userId);
        User user = userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);

        if (event.getInitiator().getId() != userId) {
            //TODO: no need?
            throw new RequestConflictException("User is not initiator of event gets requests");
        }
        List<Request> requests = requestRepository.findAllByEventId(eventId);

        return RequestMapper.mapToParticipationRequestDto(requests);
    }

    //Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя

    //если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
    //нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
    //статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
    //если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
    @Override
    public EventRequestStatusUpdateResult updateEventRequest(EventRequestStatusUpdateRequest updateRequest,
                                                             Integer userId, Integer eventId) {
        log.info("[Log][Info] Update request to status {} by user with id {} for event with id {}", updateRequest.getStatus(), userId, eventId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        Status status = Status.valueOf(updateRequest.getStatus());

        User user = userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);

        List<Integer> requestIds = updateRequest.getRequestIds();
        List<Request> requests = requestRepository.findAllById(requestIds);

        for (Request request : requests) {
            if (request.getEvent().getInitiator().getId() != userId) {
                throw new RequestConflictException("User is not initiator");
            }

            if (request.getEvent().getId() != eventId) {
                throw new RequestConflictException("Event is not event requested");
            }

            if (request.getEvent().getParticipantLimit() == request.getEvent().getConfirmedRequests()
                    && status == Status.CONFIRMED) {
                throw new RequestConflictException("Participant Limit is equal Confirmed Requests for event ");
            }

            if (request.getStatus() != Status.PENDING) {
                throw new RequestConflictException("Request is not in status PENDING");
            }

            request.setStatus(status);
            Request savedRequest = requestRepository.save(request);
            //TODO:
            Integer confirmedRequests = event.getConfirmedRequests();
            if (confirmedRequests == null) {
                event.setConfirmedRequests(1);
            } else {
                event.setConfirmedRequests(confirmedRequests + 1);
            }
            //TODO: create methods in event service
            eventRepository.save(event);
            ParticipationRequestDto dto = RequestMapper.mapToParticipationRequestDto(savedRequest);

            if (status == Status.CONFIRMED) {
                List<ParticipationRequestDto> result1 = result.getConfirmedRequests();
                result1.add(dto);
                //если при подтверждении данной заявки, лимит заявок для события исчерпан,
                // то все неподтверждённые заявки необходимо отклонить
                Integer newConfirmedRequests = event.getConfirmedRequests();
                Integer participantLimit = event.getParticipantLimit();
                if (newConfirmedRequests == participantLimit) {
                    List<Request> unconfirmedRequests = requestRepository.findAllByStatus(Status.PENDING);
                    for (Request unconfirmedRequest : unconfirmedRequests) {
                        unconfirmedRequest.setStatus(Status.REJECTED);
                        requestRepository.save(request);
                    }
                }
            }
            if (status == Status.REJECTED) {
                result.getRejectedRequests().add(dto);
            }
        }
        return result;
    }
}
