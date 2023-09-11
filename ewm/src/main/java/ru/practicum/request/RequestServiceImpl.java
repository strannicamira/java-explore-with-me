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
        log.info("Create request for event with id {} by user with id {}", eventId, userId);

        Request request = new Request();
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

        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestConflictException("Requester is initiator of event");
        }

        if (event.getPublishedOn() == null && event.getState() != State.PUBLISHED) {
            throw new RequestConflictException("Event is not published");
        }

        if (participantLimit != 0 && participantLimit.equals(event.getConfirmedRequests())) {
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
        log.info("Search request by id {}", userId);
        User userById = userService.findUserById(userId);
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.mapToParticipationRequestDto(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto updateRequest(Integer userId, Integer requestId) {
        log.info("Cancel request by user with id {} for request with id {}", userId, requestId);
        User userById = userService.findUserById(userId);

        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
        if (!request.getRequester().getId().equals(userId)) {
            throw new RequestConflictException("User is not requester");
        }
        request.setStatus(Status.CANCELED);
        Request savedRequest = requestRepository.save(request);
        return RequestMapper.mapToParticipationRequestDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> findEventRequests(Integer userId, Integer eventId) {
        log.info("Search requests for event with id {} by user with id {}", eventId, userId);
        User user = userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new RequestConflictException("User is not initiator of event gets requests");
        }
        List<Request> requests = requestRepository.findAllByEventId(eventId);

        return RequestMapper.mapToParticipationRequestDto(requests);
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequest(EventRequestStatusUpdateRequest updateRequest,
                                                             Integer userId, Integer eventId) {
        log.info("Update request to status {} by user with id {} for event with id {}", updateRequest.getStatus(), userId, eventId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        Status status = Status.valueOf(updateRequest.getStatus());

        User user = userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);

        List<Integer> requestIds = updateRequest.getRequestIds();
        List<Request> requests = requestRepository.findAllById(requestIds);

        for (Request request : requests) {
            if (!request.getEvent().getInitiator().getId().equals(userId)) {
                throw new RequestConflictException("User is not initiator");
            }

            if (!request.getEvent().getId().equals(eventId)) {
                throw new RequestConflictException("Event is not event requested");
            }

            if (request.getEvent().getParticipantLimit().equals(request.getEvent().getConfirmedRequests())
                    && status == Status.CONFIRMED) {
                throw new RequestConflictException("Participant Limit is equal Confirmed Requests for event ");
            }

            if (request.getStatus() != Status.PENDING) {
                throw new RequestConflictException("Request is not in status PENDING");
            }

            request.setStatus(status);
            Request savedRequest = requestRepository.save(request);
            Integer confirmedRequests = event.getConfirmedRequests();
            if (confirmedRequests == null) {
                event.setConfirmedRequests(1);
            } else {
                event.setConfirmedRequests(confirmedRequests + 1);
            }
            eventRepository.save(event);
            ParticipationRequestDto dto = RequestMapper.mapToParticipationRequestDto(savedRequest);

            if (status == Status.CONFIRMED) {
                List<ParticipationRequestDto> result1 = result.getConfirmedRequests();
                result1.add(dto);
                Integer newConfirmedRequests = event.getConfirmedRequests();
                Integer participantLimit = event.getParticipantLimit();
                if (newConfirmedRequests.equals(participantLimit)) {
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
