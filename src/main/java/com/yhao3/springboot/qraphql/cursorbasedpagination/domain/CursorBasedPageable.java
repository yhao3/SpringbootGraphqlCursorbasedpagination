package com.yhao3.springboot.qraphql.cursorbasedpagination.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CursorBasedPageable {
    private Integer first;
    private String after;
    private Integer last; 
    private String before; 
    private String orderBy;
    private String direction; 
}
