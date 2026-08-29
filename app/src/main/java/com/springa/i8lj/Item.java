package com.springa.i8lj;

import java.io.Serializable;

/** A row of the local storage table (one entity entry). */
public class Item implements Serializable {

    public long id;
    public String title;
    public String body;
    public String category;
    public long amount;
    public boolean done;
    public long created;
    public long updated;

    public Item() {
    }

    public Item(long id, String title, String body, String category, long amount,
                boolean done, long created, long updated) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.category = category;
        this.amount = amount;
        this.done = done;
        this.created = created;
        this.updated = updated;
    }

    public String subtitle() {
        StringBuilder sb = new StringBuilder();
        if (Spec.HAS_CATEGORY && category != null && !category.isEmpty()) {
            sb.append(category);
        }
        if (Spec.HAS_AMOUNT) {
            if (sb.length() > 0) {
                sb.append("  ·  ");
            }
            sb.append(Util.formatNumber(amount));
        }
        return sb.toString();
    }

    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String q = query.toLowerCase();
        if (title != null && title.toLowerCase().contains(q)) {
            return true;
        }
        if (body != null && body.toLowerCase().contains(q)) {
            return true;
        }
        if (category != null && category.toLowerCase().contains(q)) {
            return true;
        }
        return false;
    }

    public String displayTitle() {
        return Util.titleOf(title, Spec.ENTITY_NAME);
    }

    public String displayMeta() {
        String sub = subtitle();
        if (sub != null && !sub.trim().isEmpty()) {
            return sub;
        }
        if (Spec.HAS_BODY && body != null && !body.trim().isEmpty()) {
            return Util.truncate(body, 90);
        }
        return "Saved locally";
    }
}