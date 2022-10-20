package com.yhao3.springboot.qraphql.cursorbasedpagination.utils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.Edge;

public class CursorUtil {
    public static ConnectionCursor encode(Long id) {
        return new DefaultConnectionCursor(
                Base64.getEncoder().encodeToString(id.toString().getBytes(StandardCharsets.UTF_8))
        );
    }
    public static ConnectionCursor encode(Object item) {
        return new DefaultConnectionCursor(
                Base64.getEncoder().encodeToString(item.toString().getBytes(StandardCharsets.UTF_8))
        );
    }
    public static ConnectionCursor encode(String base64Cursor) {
        return new DefaultConnectionCursor(base64Cursor);
    }

    public static Long decodeToLong(String cursor) {
        return Long.parseLong(new String(Base64.getDecoder().decode(cursor)));
    }
    public static String decodeToString(String cursor) {
        return new String(Base64.getDecoder().decode(cursor));
    }
    public static Instant decodeToInstant(String cursor) {
        return Instant.parse(new String(Base64.getDecoder().decode(cursor)));
    }

    public static <T> ConnectionCursor getStartCursor(List<Edge<T>> edges) {
        return edges.isEmpty() ? null : edges.get(0).getCursor();
    }

    public static <T> ConnectionCursor getEndCursor(List<Edge<T>> edges) {
        return edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor();
    }
}