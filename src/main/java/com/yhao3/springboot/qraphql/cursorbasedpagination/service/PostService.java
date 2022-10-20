package com.yhao3.springboot.qraphql.cursorbasedpagination.service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.PostDTO;

public interface PostService {

    CustomizedConnection<PostDTO> getPostsNextConnection(
        @Min(0)   Integer first, 
                  String  after, 
        @NotBlank String  orderBy,
        @NotBlank String  direction);

    CustomizedConnection<PostDTO> getPostsPrevConnection(
        @Min(0)   Integer last, 
                  String  before, 
        @NotBlank String  orderBy,
        @NotBlank String  direction);
}
