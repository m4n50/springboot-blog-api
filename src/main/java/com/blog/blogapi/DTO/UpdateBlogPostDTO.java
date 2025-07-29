package com.blog.blogapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateBlogPostDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    private List<Long> categoryIds;

    @NotNull(message = "Date is required")
    private LocalDate date;
}
