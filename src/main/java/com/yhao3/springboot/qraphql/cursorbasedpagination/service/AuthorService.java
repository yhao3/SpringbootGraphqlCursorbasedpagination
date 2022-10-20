package com.yhao3.springboot.qraphql.cursorbasedpagination.service;

import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.AuthorDTO;

public interface AuthorService {

    CustomizedConnection<AuthorDTO> getAuthorsConnection(Integer first, String after, Integer last, String before, String orderBy,
            String direction);
    
}
