package com.springa.i8lj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** Local, offline-first persistence. No network, no permissions required. */
public class ItemStore extends SQLiteOpenHelper {

    private static final String DB = "factory.db";
    private static final int VERSION = 1;

    public static final String TABLE = "items";
    public static final String C_ID = "id";
    public static final String C_TITLE = "title";
    public static final String C_BODY = "body";
    public static final String C_CATEGORY = "category";
    public static final String C_AMOUNT = "amount";
    public static final String C_DONE = "done";
    public static final String C_CREATED = "created";
    public static final String C_UPDATED = "updated";

    private static ItemStore instance;

    public static synchronized ItemStore get(Context context) {
        if (instance == null) {
            instance = new ItemStore(context.getApplicationContext());
        }
        return instance;
    }

    private ItemStore(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + C_TITLE + " TEXT NOT NULL,"
                + C_BODY + " TEXT,"
                + C_CATEGORY + " TEXT,"
                + C_AMOUNT + " INTEGER DEFAULT 0,"
                + C_DONE + " INTEGER DEFAULT 0,"
                + C_CREATED + " INTEGER NOT NULL,"
                + C_UPDATED + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private Item read(Cursor c) {
        return new Item(
                c.getLong(c.getColumnIndexOrThrow(C_ID)),
                c.getString(c.getColumnIndexOrThrow(C_TITLE)),
                c.getString(c.getColumnIndexOrThrow(C_BODY)),
                c.getString(c.getColumnIndexOrThrow(C_CATEGORY)),
                c.getLong(c.getColumnIndexOrThrow(C_AMOUNT)),
                c.getInt(c.getColumnIndexOrThrow(C_DONE)) != 0,
                c.getLong(c.getColumnIndexOrThrow(C_CREATED)),
                c.getLong(c.getColumnIndexOrThrow(C_UPDATED)));
    }

    private ContentValues values(Item it) {
        ContentValues v = new ContentValues();
        v.put(C_TITLE, it.title);
        v.put(C_BODY, it.body);
        v.put(C_CATEGORY, it.category);
        v.put(C_AMOUNT, it.amount);
        v.put(C_DONE, it.done ? 1 : 0);
        v.put(C_CREATED, it.created);
        v.put(C_UPDATED, it.updated);
        return v;
    }

    public List<Item> all() {
        List<Item> items = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE, null, null, null, null, null, C_CREATED + " DESC");
        try {
            while (c.moveToNext()) {
                items.add(read(c));
            }
        } finally {
            c.close();
        }
        return items;
    }

    public long insert(Item it) {
        long now = System.currentTimeMillis();
        it.created = now;
        it.updated = now;
        it.id = getWritableDatabase().insert(TABLE, null, values(it));
        return it.id;
    }

    public void update(Item it) {
        it.updated = System.currentTimeMillis();
        getWritableDatabase().update(TABLE, values(it), C_ID + "=?", new String[]{Long.toString(it.id)});
    }

    public void setDone(long id, boolean done) {
        ContentValues v = new ContentValues();
        v.put(C_DONE, done ? 1 : 0);
        v.put(C_UPDATED, System.currentTimeMillis());
        getWritableDatabase().update(TABLE, v, C_ID + "=?", new String[]{Long.toString(id)});
    }

    public void delete(long id) {
        getWritableDatabase().delete(TABLE, C_ID + "=?", new String[]{Long.toString(id)});
    }

    public long count() {
        return queryLong("SELECT COUNT(*) FROM " + TABLE, 0L);
    }

    public long doneCount() {
        return queryLong("SELECT COUNT(*) FROM " + TABLE + " WHERE " + C_DONE + "=1", 0L);
    }

    public long sumAmount() {
        return queryLong("SELECT COALESCE(SUM(" + C_AMOUNT + "),0) FROM " + TABLE, 0L);
    }

    private long queryLong(String sql, long fallback) {
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        try {
            if (c.moveToFirst()) {
                return c.getLong(0);
            }
        } finally {
            c.close();
        }
        return fallback;
    }
}