package com.yhao3.springboot.qraphql.cursorbasedpagination.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.Author;
import com.yhao3.springboot.qraphql.cursorbasedpagination.repository.customized.CustomizedAuthorRepository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, CustomizedAuthorRepository {

    @EntityGraph(
        attributePaths = { "posts" }
    )
    List<Author> findAll();
}
