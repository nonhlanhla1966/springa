package com.springa.i8lj;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/** Single-activity app shell: list + search + add/edit + local storage. */
public class MainActivity extends AppCompatActivity
        implements ItemAdapter.Listener, ItemEditorFragment.ResultListener {

    private ItemStore store;
    private ItemAdapter adapter;
    private List<Item> all = new ArrayList<>();
    private RecyclerView list;
    private View empty;
    private TextView stats;
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        store = ItemStore.get(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Spec.APP_NAME);
        }

        list = findViewById(R.id.list);
        empty = findViewById(R.id.empty_view);
        stats = findViewById(R.id.stats);
        adapter = new ItemAdapter(this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setHasFixedSize(false);
        list.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> openEditor(null));

        seedIfEmpty();
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String q) {
                    query = q == null ? "" : q;
                    refresh();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    query = s == null ? "" : s;
                    refresh();
                    return true;
                }
            });
        }
        return true;
    }

    private void seedIfEmpty() {
        if (store.count() > 0) {
            return;
        }
        for (String[] row : Spec.PRESETS) {
            Item it = new Item();
            it.title = row[0];
            it.category = Spec.HAS_CATEGORY && row.length > 1 ? row[1] : "";
            it.amount = Spec.HAS_AMOUNT && row.length > 2 ? parseAmount(row[2]) : 0;
            it.body = Spec.HAS_BODY && row.length > 3 ? row[3] : "";
            store.insert(it);
        }
    }

    private long parseAmount(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void refresh() {
        all = store.all();
        List<Item> shown = new ArrayList<>();
        for (Item it : all) {
            if (it.matches(query)) {
                shown.add(it);
            }
        }
        adapter.submitList(shown);
        updateEmpty(shown);
        updateStats();
    }

    private void updateEmpty(List<Item> shown) {
        if (empty != null) {
            empty.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void updateStats() {
        if (stats == null) {
            return;
        }
        long total = all.size();
        String s;
        if ("done".equals(Spec.STATS_MODE)) {
            long done = store.doneCount();
            s = done + " of " + total + " " + Spec.ENTITY_PLURAL.toLowerCase();
        } else if ("sum".equals(Spec.STATS_MODE)) {
            s = Util.formatNumber(total) + " " + Spec.ENTITY_PLURAL.toLowerCase()
                    + "  ·  " + Util.formatNumber(store.sumAmount());
        } else {
            s = Util.formatNumber(total) + " " + Spec.ENTITY_PLURAL.toLowerCase();
        }
        stats.setText(s);
    }

    private void openEditor(Item item) {
        ItemEditorFragment f = ItemEditorFragment.newInstance(item);
        f.setResultListener(this);
        f.show(getSupportFragmentManager(), "editor");
    }

    @Override
    public void onSave(Item draft) {
        if (draft.id == 0) {
            store.insert(draft);
        } else {
            store.update(draft);
        }
        refresh();
    }

    @Override
    public void onOpen(Item item) {
        openEditor(item);
    }

    @Override
    public void onToggle(Item item) {
        store.setDone(item.id, !item.done);
        item.done = !item.done;
        refresh();
    }

    @Override
    public void onDelete(Item item) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_title))
                .setMessage(getString(R.string.delete_message))
                .setPositiveButton(getString(R.string.delete_confirm), (d, w) -> {
                    store.delete(item.id);
                    refresh();
                })
                .setNegativeButton(getString(R.string.delete_keep), null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (!query.isEmpty()) {
            query = "";
            refresh();
            return;
        }
        super.onBackPressed();
    }
}