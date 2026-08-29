package com.springa.i8lj;

/**
 * AppFactory-generated configuration. All product decisions are baked in here,
 * keeping the rest of the app generic, testable and small.
 */
public final class Spec {

    public static final String APP_NAME = "Springa";
    public static final String ENTITY_NAME = "Habit";
    public static final String ENTITY_PLURAL = "Habits";
    public static final String DONE_LABEL = "Today";
    public static final String ADD_LABEL = "Add Habit";
    public static final String EMPTY_TITLE = "One habit at a time";
    public static final String EMPTY_BODY = "Today is a great day to start a new habit.";
    public static final String FIELD_BODY_LABEL = "Details";
    public static final String FIELD_AMOUNT_LABEL = "Today";
    public static final String FIELD_CATEGORY_LABEL = "Category";

    public static final boolean HAS_BODY = false;
    public static final boolean HAS_AMOUNT = true;
    public static final boolean HAS_CATEGORY = false;
    public static final boolean HAS_DONE = true;
    public static final boolean CATEGORY_FIRST = false;

    /** count | done | sum */
    public static final String STATS_MODE = "count";

    public static final String[] CATEGORIES = {"Morning", "Evening", "Movement", "Mind"};

    /** Starter rows seeded once on first launch: {title, category, amount} */
    public static final String[][] PRESETS = {
            {"Drink 2L of water", "", "2", ""},
            {"Walk 30 minutes", "", "30", ""}
        };

    private Spec() {
    }
}