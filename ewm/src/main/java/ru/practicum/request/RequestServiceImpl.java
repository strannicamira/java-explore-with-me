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
        if(userId==null || eventId == null){
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
            request.setStatus(Status.APPROVED);
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
        log.info("Search requests");
        User userById = userService.findUserById(userId);
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.mapToParticipationRequestDto(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto updateRequest(Integer userId, Integer requestId) {
        log.info("Cancel request by user with id {} for request with id", userId, requestId);
        User userById = userService.findUserById(userId);

        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        if (request.getRequester().getId() != userId) {
            throw new RequestException("User is not requester");
        }
        request.setStatus(Status.CANCELED);
        Request savedRequest = requestRepository.save(request);
        return RequestMapper.mapToParticipationRequestDto(savedRequest);
    }
}
