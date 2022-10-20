package com.yhao3.springboot.qraphql.cursorbasedpagination.service.impl;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yhao3.springboot.qraphql.cursorbasedpagination.connections.CustomizedConnection;
import com.yhao3.springboot.qraphql.cursorbasedpagination.dto.PostDTO;
import com.yhao3.springboot.qraphql.cursorbasedpagination.entity.QPost;
import com.yhao3.springboot.qraphql.cursorbasedpagination.repository.PostRepository;
import com.yhao3.springboot.qraphql.cursorbasedpagination.service.PostService;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private static final QPost Q_POST = QPost.post;

    @Autowired
    private PostRepository postRepository;

    @Override
    public CustomizedConnection<PostDTO> getPostsNextConnection(
        @Min(0)   Integer first,  
                  String  after,
        @NotBlank String  orderBy, 
        @NotBlank String  direction) {
        return postRepository.findNextConnection(first, after, orderBy, direction);
    }

    @Override
    public CustomizedConnection<PostDTO> getPostsPrevConnection(
        @Min(0)   Integer last, 
                  String  before,
        @NotBlank String  orderBy, 
        @NotBlank String  direction) {

        // List<PostDTO> posts = new LinkedList<>();
        // boolean hasNextPage = false;
        // boolean hasPreviousPage = false;
        // Long cursor = CursorUtil.decode(before);
        // if ("asc".equalsIgnoreCase(direction)) {
        //     /* ======================= 升序 ASC ======================= */
        //     /* ASC + 上一頁 */
        //     BooleanBuilder whereCondition = new BooleanBuilder().and(Q_POST.id.gt(cursor));

        //     // SELECT * FROM author WHERE id > $lastId ORDER BY xxx ASC LIMIT ($first + 1)
        //     posts = postRepository.findPrevForEdges(whereCondition, last + 1, orderBy, direction);
        //     hasPreviousPage = posts.size() > last; // 有這一頁就有上一頁
        //     hasNextPage = true;

        //     // 如果有下一頁，就刪除最後那筆冗余的資料
        //     if (hasNextPage) {
        //         posts.remove(posts.size() - 1);
        //     }
        // } else {
        //     /* ======================= 降序 DESC ======================= */
        //     /* DESC + 上一頁 */
        //     // if (after.isEmpty()) {
        //     //     /* DESC + 第一頁: 第一頁 after 為 0 或 empty */
        //     //     // SELECT * FROM author ORDER BY xxx DESC LIMIT ($first + 1)
        //     //     posts = postRepository.findNextForEdges(null, first + 1, orderBy, direction);

        //     //     hasPreviousPage = false;
        //     //     hasNextPage = posts.size() > first;

        //     //     // 如果有下一頁，就刪除最後那筆冗余的資料
        //     //     if (hasNextPage) {
        //     //         posts.remove(posts.size() - 1);
        //     //     }
        //     // } else {
        //     //     /* DESC + 下一頁: 非第一頁 after 不為 0 */
        //     //     BooleanBuilder whereCondition = new BooleanBuilder().and(Q_POST.id.lt(cursor));

        //     //     // SELECT * FROM author WHERE id < $lastId ORDER BY xxx DESC LIMIT ($first + 1)
        //     //     posts = postRepository.findNextForEdges(whereCondition, first + 1, orderBy, direction);
        //     //     hasPreviousPage = true; // 有這一頁就有上一頁
        //     //     hasNextPage = posts.size() > first;

        //     //     // 如果有下一頁，就刪除最後那筆冗余的資料
        //     //     if (hasNextPage) {
        //     //         posts.remove(posts.size() - 1);
        //     //     }
        //     // }
        // }

        // // 將 List 塞進 Edges 中
        // List<Edge<PostDTO>> edges = posts
        //         .stream()
        //         .map(post -> new DefaultEdge<>(post, CursorUtil.encode(post.getId())))
        //         .collect(Collectors.toList());

        // // PageInfo
        // PageInfo pageInfo = new DefaultPageInfo(
        //         CursorUtil.getStartCursorFrom(edges),
        //         CursorUtil.getEndCursorFrom(edges),
        //         hasPreviousPage,
        //         hasNextPage);

        // // return Connection
        // return new DefaultConnection<>(edges, pageInfo);
        return null;
    }

}
