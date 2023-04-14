package com.myblog.payload;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentDto {
    private long id;

    @NotEmpty
    @Size(min = 4, message = "Name should be at list 4 characters")
    private String name;

    @NotEmpty
    @Size(message = "email format should be given")
    private String email;

    @NotEmpty
    @Size(min = 5, message = "message should be at list 5 characters")
    private String body;


}
