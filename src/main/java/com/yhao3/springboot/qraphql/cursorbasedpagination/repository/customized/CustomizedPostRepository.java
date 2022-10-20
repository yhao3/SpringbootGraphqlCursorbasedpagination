package com.yhao3.springboot.qraphql.cursorbasedpagination.repository.customized;

import java.util.List;

import com.querydsl.core.types.Predicate;
import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.PostDTO;

public interface CustomizedPostRepository {
    List<PostDTO> findNextForEdges(Predicate predicate, Integer limit, String orderBy, String direction);
    List<PostDTO> findPrevForEdges(Predicate predicate, Integer limit, String orderBy, String direction);
    CustomizedConnection<PostDTO> findNextConnection(Integer first, String after, String orderBy, String direction);
    CustomizedConnection<PostDTO> findPrevConnection(Predicate predicate, Integer limit, String orderBy, String direction);
}
