package com.springa.i8lj;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilTest {

    @Test
    public void formatNumber_plain() {
        assertEquals("0", Util.formatNumber(0));
        assertEquals("42", Util.formatNumber(42));
        assertEquals("999", Util.formatNumber(999));
        assertEquals("-7", Util.formatNumber(-7));
    }

    @Test
    public void formatNumber_thousands() {
        assertEquals("1.2k", Util.formatNumber(1200));
        assertEquals("15.0k", Util.formatNumber(15000));
    }

    @Test
    public void formatNumber_millions() {
        assertEquals("1.5M", Util.formatNumber(1520000));
    }

    @Test
    public void truncate_handlesNull() {
        assertEquals("", Util.truncate(null, 5));
    }

    @Test
    public void truncate_shortStays() {
        assertEquals("abc", Util.truncate("abc", 5));
    }

    @Test
    public void truncate_longCutsWithEllipsis() {
        String out = Util.truncate("abcdef", 3);
        assertEquals(3, out.length());
        assertEquals("…", out.substring(out.length() - 1));
    }

    @Test
    public void titleOf_fallbackOnBlank() {
        assertEquals("x", Util.titleOf(null, "x"));
        assertEquals("x", Util.titleOf("   ", "x"));
        assertEquals("real", Util.titleOf(" real ", "x"));
    }

    @Test
    public void mathSanity() {
        assertTrue(Util.formatNumber(1000).endsWith("k"));
    }
}