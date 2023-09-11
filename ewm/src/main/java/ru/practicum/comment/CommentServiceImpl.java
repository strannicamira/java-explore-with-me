package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public CommentResponseDto createComment(Integer userId, Integer eventId, CommentRequestDto commentRequestDto) {
        log.info("Create comment by user id {} for event id {}", userId, eventId);
        User userById = userService.findUserById(userId);
        Event eventById = eventService.findEventById(eventId);
        LocalDateTime created = LocalDateTime.now();
        String text = commentRequestDto.getText();

        Comment comment = new Comment();
        comment.setAuthor(userById);
        comment.setEvent(eventById);
        comment.setCreated(created);
        comment.setText(text);

        Comment savedComment = commentRepository.save(comment);

        CommentResponseDto commentResponseDto = CommentMapper.mapToCommentResponseDto(savedComment);

        return commentResponseDto;
    }
}
