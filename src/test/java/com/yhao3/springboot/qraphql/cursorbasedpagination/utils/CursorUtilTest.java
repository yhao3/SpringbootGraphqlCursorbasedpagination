package com.yhao3.springboot.qraphql.cursorbasedpagination.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

public class CursorUtilTest {
    @Test
    void testDecodeToInstant() {
        Instant instant = CursorUtil.decodeToInstant("MjAyMi0wMi0xNVQxODozNToyNC4wMFo=");
        assertEquals(Instant.parse("2022-02-15T18:35:24.00Z"), instant);
    }
}
