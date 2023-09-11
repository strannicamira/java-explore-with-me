package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@Valid @NotNull @PathVariable(name = "userId") Integer userId,
                                            @Valid @NotNull @RequestParam(name = "eventId") Integer eventId,
                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.createCommentByUserId(userId, eventId, commentRequestDto);
    }

//    @GetMapping
//    public List<CommentResponseDto> findComments(@Valid @NotNull @PathVariable(name = "userId") Integer userId) {
//        return commentService.findCommentByUserId(userId);
//    }

    @PatchMapping(value = "/{commentId}")
    public CommentResponseDto updateComment(
            @Valid @NotNull @PathVariable(name = "userId") Integer userId,
            @Valid @NotNull @PathVariable(name = "commentId") Integer commentId,
            @Valid @RequestBody CommentRequestDto request) {
        return commentService.updateCommentByUserId(userId, commentId, request);
    }
}
