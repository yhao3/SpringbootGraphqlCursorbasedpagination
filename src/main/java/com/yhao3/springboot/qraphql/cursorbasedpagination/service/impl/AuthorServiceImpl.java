package com.yhao3.springboot.qraphql.cursorbasedpagination.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.AuthorDTO;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.QAuthor;
import com.yhao3.springboot.qraphql.cursorbasedpagination.repository.AuthorRepository;
import com.yhao3.springboot.qraphql.cursorbasedpagination.service.AuthorService;

import graphql.relay.DefaultConnection;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private static final QAuthor Q_AUTHOR = QAuthor.author;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public CustomizedConnection<AuthorDTO> getAuthorsConnection(Integer first, String after, Integer last, String before, String orderBy, String direction) {

        List<AuthorDTO> authors = new LinkedList<>();
        boolean hasNextPage = false;
        boolean hasPreviousPage = false;

        /* ASC  + 第一頁: 第一頁沒有 after 下一頁 before 上一頁問題 */
        if (first != null && after == null && before == null) {
            BooleanBuilder whereCondition = new BooleanBuilder();
            // whereCondition.and(Q_AUTHOR.id.gt(lastId));
            // SELECT * FROM author WHERE id > $lastId LIMIT ($first + 1)
            // authors = authorRepository.findAllForEdges(whereCondition, first);
        }
        /* ASC  + 下一頁 */
        /* ASC  + 上一頁 */

        /* DESC + 第一頁 */
        /* DESC + 下一頁 */
        /* DESC + 上一頁 */
        Long lastId = 0L;
        // if (after != null) {
        //     lastId = CursorUtil.decode(after); // 反編譯為明文當作 repository 方法之參數
        // }
        
        // hasNextPage = authors.size() > first;

        // // 如果有下一頁，就刪除最後那筆冗余的資料
        // if (hasNextPage) {
        //     authors.remove(authors.size() - 1);
        // }

        // // 將 List 塞進 Edges 中
        // List<Edge<Author>> edges = authors
        //     .stream()
        //     .map(artist -> new DefaultEdge<>(artist, CursorUtil.encode(artist.getId())))
        //     .collect(Collectors.toList());

        // // PageInfo
        // PageInfo pageInfo =  new DefaultPageInfo(
        //         CursorUtil.getStartCursorFrom(edges),
        //         CursorUtil.getEndCursorFrom(edges),
        //         false,
        //         hasNextPage
        // );

        // return Connection
        return new CustomizedConnection<>(null, null, 0L);
    }
    
}
