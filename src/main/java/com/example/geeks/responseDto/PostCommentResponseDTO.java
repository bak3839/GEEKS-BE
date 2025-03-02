package com.example.geeks.responseDto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostCommentResponseDTO {

    private Long commentId;

    private String writer;

    private String content;

    private boolean isDeleted;

    private boolean commentWriterState;

    private LocalDateTime createdDate;

    private List<PostCommentResponseDTO> children = new ArrayList<>();

    public PostCommentResponseDTO(Long commentId, String writer, String content, boolean isDeleted, boolean commentWriterState, LocalDateTime createdDate) {
        this.commentId = commentId;
        this.writer = writer;
        this.content = content;
        this.isDeleted = isDeleted;
        this.commentWriterState = commentWriterState;
        this.createdDate = createdDate;
    }
}
