package com.yhao3.springboot.qraphql.cursorbasedpagination.repository.customized.impl;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.PostDTO;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.QAuthor;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.QPost;
import com.yhao3.springboot.qraphql.cursorbasedpagination.repository.customized.CustomizedPostRepository;
import com.yhao3.springboot.qraphql.cursorbasedpagination.utils.QuerydslJpaCursorPageableUtils;

public class CustomizedPostRepositoryImpl implements CustomizedPostRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomizedAuthorRepositoryImpl.class);

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    private static final QAuthor Q_AUTHOR = QAuthor.author;
    private static final QPost Q_POST = QPost.post;

    @Override
    public List<PostDTO> findNextForEdges(Predicate predicate, Integer limit, String orderBy, String direction) {
        // NumberPath<Long> id = Expressions.numberPath(Long.class, "id");
        // StringPath title = Expressions.stringPath("title");
        // StringPath firstName = Expressions.stringPath("firstName");
        // StringPath lastName = Expressions.stringPath("lastName");
        // StringPath email = Expressions.stringPath("email");

        // OrderSpecifier<?> orderSpec = toOrderSpecByAlias(orderBy, direction, id, email);

        // JPAQuery<PostDTO> jpaQuery = jpaQueryFactory
        //         .select(
        //                 Projections.fields(
        //                         PostDTO.class,
        //                         Q_POST.id.as(id),
        //                         Q_POST.title.as(title),
        //                         Q_AUTHOR.firstName.as(firstName),
        //                         Q_AUTHOR.lastName.as(lastName),
        //                         Q_AUTHOR.email.as(email)))
        //         .from(Q_POST)
        //         .leftJoin(Q_AUTHOR).on(Q_AUTHOR.id.eq(Q_POST.author.id));
        // if (predicate != null) {
        //     jpaQuery.where(predicate);
        // }
        // return jpaQuery
        //         .orderBy(orderSpec)
        //         .limit(limit)
        //         .fetch();

        return null;
    }

    @Override
    public List<PostDTO> findPrevForEdges(Predicate predicate, Integer limit, String orderBy, String direction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CustomizedConnection<PostDTO> findNextConnection(Integer first, String after, String orderBy, String direction) {

        NumberPath<Long> id = Expressions.numberPath(Long.class, Q_POST, "id");
        DateTimePath<Instant> createdAt = Expressions.dateTimePath(Instant.class, Q_POST, "createdAt");

        JPAQuery<PostDTO> jpaQuery = jpaQueryFactory
                .select(
                        Projections.fields(
                                PostDTO.class,
                                Q_POST.id.as(id),
                                Q_POST.title.as("title"),
                                Q_POST.createdAt.as(createdAt),
                                Q_AUTHOR.firstName.as("firstName"),
                                Q_AUTHOR.lastName.as("lastName"),
                                Q_AUTHOR.email.as("email")))
                .from(Q_POST)
                .leftJoin(Q_AUTHOR).on(Q_AUTHOR.id.eq(Q_POST.author.id));

        return QuerydslJpaCursorPageableUtils.getConnection(jpaQuery, first, after, orderBy, direction, id, createdAt);
    }

    @Override
    public CustomizedConnection<PostDTO> findPrevConnection(Predicate predicate, Integer limit, String orderBy,
            String direction) {
        // TODO Auto-generated method stub
        return null;
    }

}
