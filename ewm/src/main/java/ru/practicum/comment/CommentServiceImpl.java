package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventService;
import ru.practicum.exceptionhandler.CommentConflictException;
import ru.practicum.exceptionhandler.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserService;
import ru.practicum.util.ServiceImplUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    @Transactional
    public CommentResponseDto createCommentByUserId(Integer userId, Integer eventId, CommentRequestDto commentRequestDto) {
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

    @Override
    public List<CommentResponseDto> findCommentResponseDtos(Integer eventId, Integer from, Integer size) {
        log.info("Search comments by event with id {}", eventId);


        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);

        List<CommentResponseDto> commentDtos = new ArrayList<>();
        List<Comment> foundComments = new ArrayList<>();
        if (eventId != null) {
            eventService.findEventById(eventId);
            foundComments = commentRepository.findAllByEventId(eventId, page);

        } else {
            Page<Comment> comments = commentRepository.findAll(page);
            foundComments = ServiceImplUtils.mapToList(comments);
        }

        commentDtos = CommentMapper.mapToCommentResponseDto(foundComments);
        return commentDtos;
    }

    @Override
    public CommentResponseDto findCommentResponseDtoById(Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        CommentResponseDto commentResponseDto = CommentMapper.mapToCommentResponseDto(comment);
        return commentResponseDto;
    }

    @Override
    @Transactional
    public CommentResponseDto updateCommentByUserId(Integer userId, Integer commentId, CommentRequestDto commentRequestDto) {
        User user = userService.findUserById(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new CommentConflictException("User is not comment author");
        }

        if (!commentRequestDto.getText().isBlank()) {
            comment.setText(commentRequestDto.getText());
        }

        Comment savedComment = commentRepository.save(comment);
        CommentResponseDto updatedDto = CommentMapper.mapToCommentResponseDto(savedComment);

        return updatedDto;
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Integer commentId) {
        log.info("Delete comment by id {}", commentId);
        commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        commentRepository.deleteById(commentId);
    }
}
