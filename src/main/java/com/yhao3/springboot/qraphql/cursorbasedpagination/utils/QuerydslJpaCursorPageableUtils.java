package com.yhao3.springboot.qraphql.cursorbasedpagination.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;

import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuerydslJpaCursorPageableUtils {
    public static <T> CustomizedConnection<T> getConnection(JPAQuery<T> jpaQuery, 
                                                  Integer     first, 
                                                  String      after, 
                                                  String      orderBy,
                                                  String      direction, 
                                                  Path<?>...  supportedSortPaths) {

        List<T> list = new LinkedList<>();
        boolean hasNextPage = false;
        boolean hasPreviousPage = false;

        // 3. totalCount: 
        long totalCount = jpaQuery.fetchCount();

        OrderSpecifier<?> orderSpec = toOrderSpecByAlias(orderBy, direction, supportedSortPaths);
        // ComparableExpression<?> orderExpression = orderSpec.getTarget();
        BooleanBuilder whereCondition = new BooleanBuilder();

        if (after == null) {
            /* ASC + 第一頁: 第一頁 after 為 0 或 empty */
            // SELECT * FROM posts ORDER BY xxx ASC LIMIT ($first + 1)
            list = jpaQuery
                    .orderBy(orderSpec)
                    .limit(first + 1)
                    .fetch();

            hasPreviousPage = false;
            hasNextPage = list.size() > first;

            // 如果有下一頁，就刪除最後那筆冗余的資料
            if (hasNextPage) {
                list.remove(list.size() - 1);
            }
            
        } else {
            /* ASC + 下一頁: 非第一頁 after 不為 0 */
            // Long cursor = CursorUtil.decode(after);
            whereCondition.and(toBooleanExpression(after, orderBy, direction, supportedSortPaths));
            list = jpaQuery
                    .where(whereCondition)
                    .orderBy(orderSpec)
                    .limit(first + 1)
                    .fetch();

            hasPreviousPage = true; // 有這一頁就有上一頁
            hasNextPage = list.size() > first;

            // 如果有下一頁，就刪除最後那筆冗余的資料
            if (hasNextPage) {
                list.remove(list.size() - 1);
            }
        }

        // 1. edges: 將 List 塞進 Edges 中
        List<Edge<T>> edges = list
                .stream()
                .map(item -> new DefaultEdge<>(item, new DefaultConnectionCursor(getCursor(item, orderBy)))) // edge 的 cursor 是每筆 orderBy 欄位的 base64 編碼
                .collect(Collectors.toList());

        log.debug("edges: {}", edges);

        // 2. PageInfo: 
        PageInfo pageInfo = new DefaultPageInfo(
                CursorUtil.getStartCursor(edges),
                CursorUtil.getEndCursor(edges),
                hasPreviousPage,
                hasNextPage);

        

        // return Connection
        return new CustomizedConnection<>(edges, pageInfo, totalCount);
    }

    /** 透過 reflection 呼叫 getXxx() method 取得 orderBy 欄位之屬性值，並 encode 為 Base64 格式 */
    private static String getCursor(Object item, String orderBy) {
        Method getMethod;
        try {
            getMethod = item.getClass().getDeclaredMethod("get" + orderBy.substring(0, 1).toUpperCase() + orderBy.substring(1));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String returnValue;
        try {
            returnValue = getMethod.invoke(item).toString();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return Base64.getEncoder().encodeToString(returnValue.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings({ "unchecked" })
    private static BooleanExpression toBooleanExpression(String after, String orderBy, String direction,
            Path<?>... paths) {
        BooleanExpression booleanExpression = null;

        String property = orderBy;
        Path<?> path = null;
        for (Path<?> p : paths) {
            if (property.equals(p.getMetadata().getName())) {
                path = p;
                break;
            }
        }
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (path.getClass().equals(StringPath.class)) {
            StringPath stringPath = (StringPath) path;
            if ("asc".equalsIgnoreCase(direction))
                booleanExpression = stringPath.gt(CursorUtil.decodeToString(after));
            else
                booleanExpression = stringPath.lt(CursorUtil.decodeToString(after));
        } else if (path.getClass().equals(NumberPath.class)) {
            NumberPath<?> numberPath = (NumberPath<?>) path;
            if ("asc".equalsIgnoreCase(direction))
                booleanExpression = numberPath.gt(CursorUtil.decodeToLong(after));
            else
                booleanExpression = numberPath.lt(CursorUtil.decodeToLong(after));
        } else if (path.getClass().equals(DateTimePath.class)) {
            DateTimePath<Instant> dateTimePath = (DateTimePath<Instant>) path; // 因為比較欄位要是「唯一的」，故只支援 Instant
            if ("asc".equalsIgnoreCase(direction))
                booleanExpression = dateTimePath.gt(CursorUtil.decodeToInstant(after));
            else
                booleanExpression = dateTimePath.lt(CursorUtil.decodeToInstant(after));
        }
        return booleanExpression;
    }

    private static OrderSpecifier<?> toOrderSpecByAlias(String orderBy, String direction, Path<?>... paths) {
        OrderSpecifier<?> orderSpec = null;

        String property = orderBy;
        Path<?> path = null;
        for (Path<?> p : paths) {
            if (property.equals(p.getMetadata().getName())) {
                path = p;
                break;
            }
        }
        if (path == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (path.getClass().equals(StringPath.class)) {
            StringPath stringPath = (StringPath) path;
            if ("asc".equalsIgnoreCase(direction))
                orderSpec = stringPath.asc();
            else
                orderSpec = stringPath.desc();
        } else if (path.getClass().equals(NumberPath.class)) {
            NumberPath<?> numberPath = (NumberPath<?>) path;
            if ("asc".equalsIgnoreCase(direction))
                orderSpec = numberPath.asc();
            else
                orderSpec = numberPath.desc();
        } else if (path.getClass().equals(DateTimePath.class)) {
            DateTimePath<?> dateTimePath = (DateTimePath<?>) path;
            if ("asc".equalsIgnoreCase(direction))
                orderSpec = dateTimePath.asc();
            else
                orderSpec = dateTimePath.desc();
        }
        return orderSpec;
    }
}
