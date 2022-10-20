package com.yhao3.springboot.qraphql.cursorbasedpagination.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.PostDTO;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.Post;
import com.yhao3.springboot.qraphql.cursorbasedpagination.utils.CursorUtil;

import graphql.relay.Edge;
import graphql.relay.PageInfo;

@SpringBootTest
public class PostRepositoryTest {

    private static final int FIVE = 5;
    private static final Long LONG_4 = 4L;
    private static final Long LONG_5 = 5L;
    private static final Long LONG_59996 = 59996L;
    private static final Long LONG_59998 = 59998L;
    private static final String ID = "id";
    private static final String CREATED_AT = "createdAt";
    // private static final

    @Autowired
    private PostRepository postRepository;

    @Test
    void testFindAll() {
        List<Post> list = postRepository.findAll();
        assertEquals(60000, list.size());
        assertNotNull(list.get(0).getCreatedAt());
    }

    /* next page - order by id */
    @Test
    void test_nextConnection_page1_size5_orderById_asc() {

        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE, null, ID, "asc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(FIVE, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getId(), CursorUtil.decodeToLong(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(false, pageInfo.isHasPreviousPage());
        assertEquals(true, pageInfo.isHasNextPage());
        assertEquals(1L, CursorUtil.decodeToLong(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(LONG_5, CursorUtil.decodeToLong(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_page1_size5_orderById_desc() {
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE, null, ID, "desc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(FIVE, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getId(), CursorUtil.decodeToLong(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(false, pageInfo.isHasPreviousPage());
        assertEquals(true, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getId(),
                CursorUtil.decodeToLong(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getId(),
                CursorUtil.decodeToLong(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_page2_size5_orderById_asc() {
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString("5".getBytes(StandardCharsets.UTF_8)), // ASC 第 5 筆之後
                ID,
                "asc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(FIVE, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getId(), CursorUtil.decodeToLong(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(true, pageInfo.isHasPreviousPage());
        assertEquals(true, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getId(),
                CursorUtil.decodeToLong(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getId(),
                CursorUtil.decodeToLong(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_page2_size5_orderById_desc() {
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString("16".getBytes(StandardCharsets.UTF_8)), // DESC 第 16 筆之後
                ID,
                "desc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(FIVE, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getId(), CursorUtil.decodeToLong(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(true, pageInfo.isHasPreviousPage());
        assertEquals(true, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getId(),
                CursorUtil.decodeToLong(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getId(),
                CursorUtil.decodeToLong(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_noNextPage_size5_orderById_asc() {
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString(LONG_59998.toString().getBytes(StandardCharsets.UTF_8)), // ASC 第
                                                                                                            // 5998
                                                                                                            // 筆之後 -> 只剩
                                                                                                            // 2 筆
                ID,
                "asc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(connection.getTotalCount() - LONG_59998, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getId(), CursorUtil.decodeToLong(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(true, pageInfo.isHasPreviousPage());
        assertEquals(false, pageInfo.isHasNextPage());
        assertEquals(59999L, CursorUtil.decodeToLong(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(60000L, CursorUtil.decodeToLong(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_noNextPage_size5_orderById_desc() {
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString(LONG_4.toString().getBytes(StandardCharsets.UTF_8)), // DESC 第 4 筆之後
                                                                                                        // -> 剩下 3 筆
                ID,
                "desc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(LONG_4 - 1, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getId(), CursorUtil.decodeToLong(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(true, pageInfo.isHasPreviousPage());
        assertEquals(false, pageInfo.isHasNextPage());
        assertEquals(3L, CursorUtil.decodeToLong(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(1L, CursorUtil.decodeToLong(connection.getPageInfo().getEndCursor().getValue()));
    }

    /*
     * next page - order by createdAt
     */
    @Test
    void test_nextConnection_page1_size5_orderByCreatedAt_asc() {
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE, null, CREATED_AT, "asc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(FIVE, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getCreatedAt(), CursorUtil.decodeToInstant(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(false, pageInfo.isHasPreviousPage());
        assertEquals(true, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_page1_size5_orderByCreatedAt_desc() {

        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE, null, CREATED_AT, "desc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(FIVE, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getCreatedAt(), CursorUtil.decodeToInstant(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(false, pageInfo.isHasPreviousPage());
        assertEquals(true, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_page2_size5_orderByCreatedAt_asc() {
        Post post5 = postRepository.findById(LONG_5).orElse(null);
        assertNotNull(post5);
        assertEquals(LONG_5, post5.getId());

        Instant post5CreatedAt = post5.getCreatedAt();
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString(post5CreatedAt.toString().getBytes(StandardCharsets.UTF_8)), // ASC 第
                                                                                                                // 5 筆之後
                CREATED_AT,
                "asc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(LONG_5, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getCreatedAt(), CursorUtil.decodeToInstant(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(true, pageInfo.isHasPreviousPage());
        assertEquals(true, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_page2_size5_orderByCreatedAt_desc() {
        Post post5996 = postRepository.findById(LONG_59996).orElse(null);
        assertNotNull(post5996);
        assertEquals(LONG_59996, post5996.getId());

        Instant post5996CreatedAt = post5996.getCreatedAt();
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString(post5996CreatedAt.toString().getBytes(StandardCharsets.UTF_8)), // DESC
                                                                                                                   // 第
                                                                                                                   // 5996
                                                                                                                   // 筆之後
                                                                                                                   // (5995
                                                                                                                   // ~
                                                                                                                   // 5991)
                CREATED_AT,
                "desc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(FIVE, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getCreatedAt(), CursorUtil.decodeToInstant(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(true, pageInfo.isHasPreviousPage());
        assertEquals(true, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_noNextPage_size5_orderByCreatedAt_asc() {
        Post post5998 = postRepository.findById(LONG_59998).orElse(null);
        assertNotNull(post5998);
        assertEquals(LONG_59998, post5998.getId());

        Instant post5998CreatedAt = post5998.getCreatedAt();
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString(post5998CreatedAt.toString().getBytes(StandardCharsets.UTF_8)), // ASC
                                                                                                                   // 第
                                                                                                                   // 18
                                                                                                                   // 筆之後
                                                                                                                   // ->
                                                                                                                   // 只剩
                                                                                                                   // 2
                                                                                                                   // 筆
                CREATED_AT,
                "asc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(connection.getTotalCount() - LONG_59998, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getCreatedAt(), CursorUtil.decodeToInstant(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(true, pageInfo.isHasPreviousPage());
        assertEquals(false, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getEndCursor().getValue()));
    }

    @Test
    void test_nextConnection_noNextPage_size5_orderByCreatedAt_desc() {
        Post post4 = postRepository.findById(LONG_4).orElse(null);
        assertNotNull(post4);
        assertEquals(LONG_4, post4.getId());

        Instant post4CreatedAt = post4.getCreatedAt();
        CustomizedConnection<PostDTO> connection = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString(post4CreatedAt.toString().getBytes(StandardCharsets.UTF_8)), // DESC
                                                                                                                // 第 4
                                                                                                                // 筆之後
                                                                                                                // -> 剩下
                                                                                                                // 3 筆
                CREATED_AT,
                "desc");
        assertNotNull(connection);

        /* test edges */
        List<Edge<PostDTO>> edges = connection.getEdges();
        assertEquals(LONG_4 - 1, edges.size());
        for (Edge<PostDTO> edge : edges) {
            assertEquals(edge.getNode().getCreatedAt(), CursorUtil.decodeToInstant(edge.getCursor().getValue()));
        }

        /* test pageInfo */
        PageInfo pageInfo = connection.getPageInfo();
        assertEquals(true, pageInfo.isHasPreviousPage());
        assertEquals(false, pageInfo.isHasNextPage());
        assertEquals(edges.get(0).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getStartCursor().getValue()));
        assertEquals(edges.get(edges.size() - 1).getNode().getCreatedAt(),
                CursorUtil.decodeToInstant(connection.getPageInfo().getEndCursor().getValue()));
    }

    /*
     * next page - Cursor-based vs Offset pagination
     */
    @Test
    void testCursorVsOffset() {
        /* test page 1000 */
        PageRequest pageRequest = PageRequest.of(11999, FIVE, Sort.by("createdAt").ascending());

        long time1 = System.currentTimeMillis();
        Page<Post> offsetPage = postRepository.findAll(pageRequest);
        long time2 = System.currentTimeMillis();
        Instant firstPostCreatedAt = offsetPage.getContent().get(0).getCreatedAt();
        long time3 = System.currentTimeMillis();
        CustomizedConnection<PostDTO> cursorBasedPage = postRepository.findNextConnection(FIVE,
                Base64.getEncoder().encodeToString(firstPostCreatedAt.toString().getBytes(StandardCharsets.UTF_8)), // ASC
                                                                                                                    // 第
                                                                                                                    // 5
                                                                                                                    // 筆之後
                CREATED_AT,
                "asc");
        long time4 = System.currentTimeMillis();
        // assertEquals(5, offsetPage.getContent().size());
        // assertEquals(5, cursorBasedPage.getEdges().size());

        long offsetCostTime = time2 - time1; // 166 182 181 156 191
        long cursorCostTime = time4 - time3; // 78 85 73 76 74
        assertFalse(offsetCostTime < cursorCostTime); // 測試時記得把中斷點拿掉
    }

}
