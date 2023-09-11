package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentResponseDto> findCommentsByEventIdByPublic(
            @RequestParam(name = "eventId") Integer eventId,
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        List<CommentResponseDto> commentDto = commentService.findCommentResponseDtos(eventId, from, size);
        return commentDto;
    }

    @GetMapping(value = "/{commentId}")
    public CommentResponseDto findCommentByIdByPublic(@PathVariable(name = "commentId") Integer commentId) {
        CommentResponseDto commentDto = commentService.findCommentResponseDtoById(commentId);
        return commentDto;
    }

}
