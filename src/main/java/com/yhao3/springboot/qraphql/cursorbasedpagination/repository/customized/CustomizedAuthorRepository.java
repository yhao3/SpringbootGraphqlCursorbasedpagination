package com.yhao3.springboot.qraphql.cursorbasedpagination.repository.customized;

import java.util.List;

import com.querydsl.core.types.Predicate;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.AuthorDTO;

public interface CustomizedAuthorRepository {
    List<AuthorDTO> findAllForEdges(Predicate predicate, Integer limit, String orderBy, String direction);
}
