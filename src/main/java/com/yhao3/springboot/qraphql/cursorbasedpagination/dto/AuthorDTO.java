package com.yhao3.springboot.qraphql.cursorbasedpagination.dto;

import lombok.Data;

@Data
public class AuthorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long postId;
    private String title;
}
