package com.yhao3.springboot.qraphql.cursorbasedpagination.repository.customized.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.AuthorDTO;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.Author;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.Post;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.QAuthor;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.QPost;
import com.yhao3.springboot.qraphql.cursorbasedpagination.repository.customized.CustomizedAuthorRepository;

public class CustomizedAuthorRepositoryImpl implements CustomizedAuthorRepository {

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    private static final QAuthor Q_AUTHOR = QAuthor.author;
    private static final QPost Q_POST = QPost.post;

    @Override
    public List<AuthorDTO> findAllForEdges(Predicate predicate, Integer limit, String orderBy, String direction) {
        StringPath orderByPath = Expressions.stringPath(orderBy);
        NumberPath<Long> id = Expressions.numberPath(Long.class, "id");
        StringPath firstName = Expressions.stringPath("firstName");
        StringPath lastName = Expressions.stringPath("lastName");
        StringPath email = Expressions.stringPath("email");
        Path<Post> post = new PathBuilder<Post>(Post.class, "post");

        return jpaQueryFactory
            .from(Q_AUTHOR)
            .leftJoin(Q_POST).on(Q_AUTHOR.id.eq(Q_POST.author.id))
            .where(predicate)
            .orderBy(orderByPath.asc())
            .limit(limit)
            .transform(
                GroupBy.groupBy(Q_AUTHOR.id).list(
                    Projections.fields(
                        AuthorDTO.class, 
                        Q_AUTHOR.id.as(id), 
                        Q_AUTHOR.firstName.as(firstName), 
                        Q_AUTHOR.lastName.as(lastName), 
                        Q_AUTHOR.email.as(email), 
                        GroupBy.list(
                            Projections.fields(
                                Post.class,
                                Q_POST.id,
                                Q_POST.title
                            )
                        ).as("posts")
                    )
                )
            );
    }
    
}
