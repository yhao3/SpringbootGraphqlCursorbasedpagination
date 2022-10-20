package com.yhao3.springboot.qraphql.cursorbasedpagination.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.AuthorDTO;
import com.yhao3.springboot.qraphql.cursorbasedpagination.service.AuthorService;

@Controller // AnnotatedControllerConfigurer 檢測 @Controller bean 並通過 RuntimeWiring.Builder 將它們的註釋處理程序方法註冊為 DataFetchers。
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;

    /*
        test query: 
         query {
            authors(first:3, after: "MQ==") {
                edges {
                node {
                    id
                    lastName
                    email
                    posts {
                        id
                        title
                    }
                }
                cursor
                }
                pageInfo {
                    hasNextPage
                    hasPreviousPage
                }
            }
        }
     */
    @QueryMapping // 將此方法綁定到 query，即 Query type 下的欄位。
    public CustomizedConnection<AuthorDTO> authors( // 如果未在 annotation 上聲明 name 屬性，則默認根據方法名稱 authors 查詢。
    /* Spring for GraphQL 使用 RuntimeWiring.Builder 將該 authors() 方法註冊為名稱為 "authors" 的 graphql.schema.DataFetcher 查詢。 */
        @Argument Integer first,
        @Argument String after,
        @Argument Integer last,
        @Argument String before,
        @Argument String orderBy,
        @Argument String direction
    ) {
        if (first != null && last != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (first == null && last == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (after != null && before != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // CursorBasedPageable cursorBasedPageable = new CursorBasedPageable(first, after, last, before, orderBy, direction);
        CustomizedConnection<AuthorDTO> authorsConnection = authorService.getAuthorsConnection(first, after, last, before, orderBy, direction);

        return authorsConnection;
    }
}
