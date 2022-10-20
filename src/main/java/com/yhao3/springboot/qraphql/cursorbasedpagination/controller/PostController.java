package com.yhao3.springboot.qraphql.cursorbasedpagination.controller;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.PostDTO;
import com.yhao3.springboot.qraphql.cursorbasedpagination.service.PostService;

@Controller // AnnotatedControllerConfigurer 檢測 @Controller bean 並通過 RuntimeWiring.Builder 將它們的註釋處理程序方法註冊為 DataFetchers。
public class PostController {

    @Autowired
    private PostService postService;
    
    /** 第一頁 + 下一頁 */
    @QueryMapping // 將此方法綁定到 query，即 Query type 下的欄位。
    public CustomizedConnection<PostDTO> postsNext( // 如果未在 annotation 上聲明 name 屬性，則默認根據方法名稱 authors 查詢。
    /* Spring for GraphQL 使用 RuntimeWiring.Builder 將該 authors() 方法註冊為名稱為 "authors" 的 graphql.schema.DataFetcher 查詢。 */
        @Argument @Min(0)   Integer first,
        @Argument           String  after,
        @Argument @NotBlank String  orderBy,
        @Argument @NotBlank String  direction
    ) {
        // CursorBasedPageable cursorBasedPageable = new CursorBasedPageable(first, after, last, before, orderBy, direction);
        CustomizedConnection<PostDTO> postsConnection = postService.getPostsNextConnection(first, after, orderBy, direction);

        return postsConnection;
    }

    /** 上一頁 */
    @QueryMapping // 將此方法綁定到 query，即 Query type 下的欄位。
    public CustomizedConnection<PostDTO> postsPrev( // 如果未在 annotation 上聲明 name 屬性，則默認根據方法名稱 authors 查詢。
    /* Spring for GraphQL 使用 RuntimeWiring.Builder 將該 authors() 方法註冊為名稱為 "authors" 的 graphql.schema.DataFetcher 查詢。 */
        @Argument @Min(0)   Integer last,
        @Argument           String  before,
        @Argument @NotBlank String  orderBy,
        @Argument @NotBlank String  direction
    ) {
        // CursorBasedPageable cursorBasedPageable = new CursorBasedPageable(first, after, last, before, orderBy, direction);
        CustomizedConnection<PostDTO> postsConnection = postService.getPostsPrevConnection(last, before, orderBy, direction);

        return postsConnection;
    }
}
