package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

        eventService.findEventById(eventId);

        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);

        List<CommentResponseDto> commentDtos = new ArrayList<>();
        List<Comment> foundComments = new ArrayList<>();
        foundComments = commentRepository.findAllByEventId(eventId, page);
        commentDtos = CommentMapper.mapToCommentResponseDto(foundComments);
        return commentDtos;
    }

    @Override
    public CommentResponseDto findCommentResponseDtoById(Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        CommentResponseDto commentResponseDto = CommentMapper.mapToCommentResponseDto(comment);
        return commentResponseDto;
    }

//    @Override
//    public List<CommentResponseDto> findCommentByUserId(Integer userId) {
//        userService.findUserById(userId);
//        List<Comment> comments = commentRepository.findAllByAuthorId(userId);
//        List<CommentResponseDto>  dtos = CommentMapper.mapToCommentResponseDto(comments);
//        return dtos;
//    }

    @Override
    public CommentResponseDto updateCommentByUserId(Integer userId, Integer commentId, CommentRequestDto request) {
        User user = userService.findUserById(userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new CommentConflictException("User is not comment author");
        }

        if (!request.getText().isBlank()) {
            comment.setText(request.getText());
        }

        Comment savedComment = commentRepository.save(comment);
        CommentResponseDto updatedDto = CommentMapper.mapToCommentResponseDto(savedComment);

        return updatedDto;
    }

    @Override
    public void deleteCommentByAdmin(Integer commentId) {
        //TODO: or replace by findCommentResponseDtoById
        log.info("Delete comment by id {}", commentId);
        commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        commentRepository.deleteById(commentId);
    }
}
