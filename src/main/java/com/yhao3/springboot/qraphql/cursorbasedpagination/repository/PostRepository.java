package com.yhao3.springboot.qraphql.cursorbasedpagination.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.Post;
import com.yhao3.springboot.qraphql.cursorbasedpagination.repository.customized.CustomizedPostRepository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomizedPostRepository {

    @EntityGraph(attributePaths = {"author"})
    Page<Post> findAll(Pageable pageable);
}
