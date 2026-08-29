package com.springa.i8lj;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/** Animating, diffing list of entries. Two taps on a row delete it. */
public class ItemAdapter extends ListAdapter<Item, ItemAdapter.Holder> {

    public interface Listener {
        void onOpen(Item item);

        void onToggle(Item item);

        void onDelete(Item item);
    }

    private static final DiffUtil.ItemCallback<Item> DIFF = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(Item a, Item b) {
            return a.id == b.id;
        }

        @Override
        public boolean areContentsTheSame(Item a, Item b) {
            if (a.amount != b.amount || a.done != b.done) {
                return false;
            }
            return eq(a.title, b.title) && eq(a.body, b.body) && eq(a.category, b.category);
        }

        private boolean eq(String x, String y) {
            if (x == null) {
                return y == null;
            }
            return x.equals(y);
        }
    };

    private final Listener listener;

    public ItemAdapter(Listener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(getItem(position));
    }

    class Holder extends RecyclerView.ViewHolder {
        final View card;
        final TextView title;
        final TextView meta;
        final TextView stamp;
        final CheckBox doneBox;

        Holder(View v) {
            super(v);
            card = v;
            title = v.findViewById(R.id.row_title);
            meta = v.findViewById(R.id.row_meta);
            stamp = v.findViewById(R.id.row_stamp);
            doneBox = v.findViewById(R.id.row_done);
        }

        void bind(Item item) {
            title.setText(item.displayTitle());
            meta.setText(item.displayMeta());
            stamp.setText(DateUtils.getRelativeTimeSpanString(item.created));
            card.setActivated(item.done);
            title.setAlpha(item.done ? 0.45f : 1f);
            boolean hasDone = Spec.HAS_DONE;
            if (doneBox != null) {
                doneBox.setVisibility(hasDone ? View.VISIBLE : View.GONE);
                if (hasDone) {
                    doneBox.setChecked(item.done);
                    doneBox.setOnClickListener(view -> {
                        if (listener != null) {
                            listener.onToggle(item);
                        }
                    });
                }
            }
            card.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onOpen(item);
                }
            });
            card.setOnLongClickListener(view -> {
                if (listener != null) {
                    listener.onDelete(item);
                }
                return true;
            });
        }
    }
}