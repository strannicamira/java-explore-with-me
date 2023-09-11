package ru.practicum.comment;

public interface CommentService {
    CommentResponseDto createComment(Integer userId, Integer eventId, CommentRequestDto commentRequestDto);
}
