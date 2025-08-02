package com.blog.blogapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPostDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotNull(message = "Categories are required")
    @Size(min = 1, message = "At least one category ID must be provided")
    private List<@NotNull(message = "Each category must be provided") Long> categoryIds;
}
