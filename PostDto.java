package com.myblog.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PostDto {

        private long id;

        @NotEmpty(message = "Is Mandatory")
        @Size(min = 2, message = "Post title should be at list 2 characters")
        private String title;

        @NotEmpty
        @Size(min =4, message = "Post description should be at list 4 characters")
        private String description;

        @NotEmpty
        @Size(min = 8, message = "Post content should be at list 8 characters")
        private String content;
}
