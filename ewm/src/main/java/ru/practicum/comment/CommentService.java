package ru.practicum.comment;

import java.util.List;

public interface CommentService {
    CommentResponseDto createCommentByUserId(Integer userId, Integer eventId, CommentRequestDto commentRequestDto);

    List<CommentResponseDto> findCommentResponseDtos(Integer eventId, Integer from, Integer size);

    CommentResponseDto findCommentResponseDtoById(Integer commentId);

    CommentResponseDto updateCommentByUserId(Integer userId, Integer commentId, CommentRequestDto request);

    void deleteCommentByAdmin(Integer commentId);

}
