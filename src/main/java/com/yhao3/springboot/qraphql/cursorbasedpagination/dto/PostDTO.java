package com.yhao3.springboot.qraphql.cursorbasedpagination.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private Instant createdAt;
    private String email;
}
