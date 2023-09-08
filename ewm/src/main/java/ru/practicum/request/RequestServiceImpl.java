package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.exceptionhandler.NotFoundException;
import ru.practicum.exceptionhandler.RequestException;
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

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Integer userId, Integer eventId) {
        log.info("[Log][Info] Create request for event with id {} by user with id {}", eventId, userId);
        if (userId == null || eventId == null) {
            throw new IllegalStateException("BAD REQUEST");
        }
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        Event eventById = eventService.findEventById(userId, eventId);
        request.setEvent(eventById);
        User userById = userService.findUserById(userId);
        request.setRequester(userById);
        if (eventById.getRequestModeration() == false) {
            //TODO: find and check status to approve
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }

        List<Request> existedRequests = requestRepository.findAllByRequesterIdAndEventId(userId, eventId);
        if (existedRequests != null && !existedRequests.isEmpty()) {
            throw new RequestException("Request already exists");
        }

        if (eventById.getInitiator().getId() == userId) {
            throw new RequestException("Requester is initiator of event");
        }

        if (eventById.getPublishedOn() == null) {
            throw new RequestException("Event is not published");
        }

        if (eventById.getParticipantLimit() == eventById.getConfirmedRequests()) {
            throw new RequestException("Participant Limit for event is equal Confirmed Requests");
        }

        Request savedRequest = requestRepository.save(request);
        return RequestMapper.mapToParticipationRequestDto(savedRequest);
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
            throw new RequestException("User is not requester");
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

        List<Request> requests = requestRepository.findAllByRequesterIdAndEventId(userId, eventId);
        return RequestMapper.mapToParticipationRequestDto(requests);
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequest(EventRequestStatusUpdateRequest updateRequest,
                                                             Integer userId, Integer eventId) {
        log.info("[Log][Info] Update request to status {} by user with id {} for event with id", updateRequest.getStatus(), userId, eventId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        Status status = Status.valueOf(updateRequest.getStatus());

        User userById = userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);

        List<Integer> requestIds = updateRequest.getRequestIds();
//        BooleanExpression byId = QRequest.request.id.in(request.getRequestIds());
        List<Request> requests = requestRepository.findAllById(requestIds);

        for (Request request : requests) {
            if (request.getRequester().getId() != userId) {
                throw new RequestException("User is not requester");
            }
            if (request.getEvent().getId() != eventId) {
                throw new RequestException("Event is not event requested");
            }
            if ((request.getEvent().getParticipantLimit()==0 || request.getEvent().getRequestModeration()==false)
                    && status == Status.CONFIRMED) {
                throw new RequestException("Confirmation is not required");
            }
            if (request.getEvent().getParticipantLimit() == request.getEvent().getConfirmedRequests()
                    && status == Status.CONFIRMED) {
                throw new RequestException("Participant Limit is equal Confirmed Requests for event ");
            }
            if (request.getStatus() != Status.PENDING) {
                throw new RequestException("Request is not in status PENDING");
            }

            request.setStatus(status);
            Request savedRequest = requestRepository.save(request);
            ParticipationRequestDto dto = RequestMapper.mapToParticipationRequestDto(savedRequest);

            if (status == Status.CONFIRMED) {
                result.getConfirmedRequests().add(dto);
                //TODO:если при подтверждении данной заявки, лимит заявок для события исчерпан,
                // то все неподтверждённые заявки необходимо отклонить
                List<Request> unconfirmedRequests = requestRepository.findAllByStatus(Status.PENDING);
                for (Request unconfirmedRequest : unconfirmedRequests) {
                    unconfirmedRequest.setStatus(Status.REJECTED);
                    requestRepository.save(request);
                }
            }
            if (status == Status.REJECTED) {
                result.getRejectedRequests().add(dto);
            }
        }

        return result;
    }
}
