package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@PathVariable Integer userId,
                                            @PathVariable Integer eventId,
                                            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.createComment(userId, eventId, commentRequestDto);
    }

}
