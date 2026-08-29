package com.springa.i8lj;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** Model tests run on the JVM with no Android dependencies. */
public class ItemTest {

    @Test
    public void displayTitle_fallsBackToEntity() {
        Item it = new Item();
        it.title = null;
        assertNotNull(it.displayTitle());
        assertEquals(Spec.ENTITY_NAME, it.displayTitle());
    }

    @Test
    public void displayTitle_usesSetTitle() {
        Item it = new Item();
        it.title = "  Hello  ";
        assertEquals("Hello", it.displayTitle());
    }

    @Test
    public void matches_emptyQueryAlwaysTrue() {
        Item it = new Item();
        it.title = null;
        assertTrue(it.matches(""));
        assertTrue(it.matches(null));
        assertTrue(it.matches("   "));
    }

    @Test
    public void matches_caseInsensitiveOnFields() {
        Item it = new Item();
        it.title = "Drink More Water";
        assertTrue(it.matches("water"));
        assertTrue(it.matches("DRINK"));
        assertFalse(it.matches("xyzzy"));
    }

    @Test
    public void matches_categoryAndBody() {
        Item it = new Item();
        it.title = "Title";
        it.category = "Work";
        it.body = "long notes here";
        assertTrue(it.matches("notes"));
        assertTrue(it.matches("work"));
    }

    @Test
    public void displayMeta_neverNullText() {
        Item it = new Item();
        assertNotNull(it.displayMeta());
        assertTrue(it.displayMeta().length() > 0);
    }
}