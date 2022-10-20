package com.yhao3.springboot.qraphql.cursorbasedpagination.connections;

import java.util.List;

import graphql.PublicApi;
import graphql.relay.DefaultConnection;
import graphql.relay.Edge;
import graphql.relay.PageInfo;

@PublicApi
public class CustomizedConnection<T> extends DefaultConnection<T> {

    private final long totalCount;

    public CustomizedConnection(List<Edge<T>> edges, PageInfo pageInfo, long totalCount) {
        super(edges, pageInfo);
        this.totalCount = totalCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public String toString() {
        return "CustomizedConnection [edges=" + this.getEdges() + ", pageInfo=" + this.getPageInfo() + "totalCount=" + this.totalCount + "]";
    }
    
}
