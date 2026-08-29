package com.springa.i8lj;

/** Small pure helpers used by the UI and covered by JVM unit tests. */
public final class Util {

    private Util() {
    }

    /** 1200 -> "1.2k", 1520000 -> "1.5M", otherwise plain. */
    public static String formatNumber(long value) {
        long abs = Math.abs(value);
        if (abs >= 1000000) {
            return String.format(java.util.Locale.US, "%.1fM", value / 1000000.0);
        }
        if (abs >= 100000) {
            return String.format(java.util.Locale.US, "%.0fk", value / 1000.0);
        }
        if (abs >= 1000) {
            return String.format(java.util.Locale.US, "%.1fk", value / 1000.0);
        }
        return Long.toString(value);
    }

    /** Truncate to max chars (with ellipsis) or "" for null. */
    public static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        if (s.length() <= max) {
            return s;
        }
        if (max <= 1) {
            return "…";
        }
        return s.substring(0, max - 1) + "…";
    }

    /** Non-blank trimmed fallback, otherwise fallback. */
    public static String titleOf(String title, String fallback) {
        if (title == null) {
            return fallback;
        }
        String t = title.trim();
        return t.isEmpty() ? fallback : t;
    }
}