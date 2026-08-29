package com.springa.i8lj;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/** Bottom-sheet create/edit dialog rendered from the generated Spec. */
public class ItemEditorFragment extends BottomSheetDialogFragment {

    public interface ResultListener {
        void onSave(Item draft);
    }

    private static final String ARG_ITEM = "item";

    private ResultListener listener;
    private Item edit;

    private TextInputLayout titleLayout;
    private TextInputEditText titleInput;

    public static ItemEditorFragment newInstance(@Nullable Item item) {
        ItemEditorFragment f = new ItemEditorFragment();
        Bundle b = new Bundle();
        if (item != null) {
            b.putSerializable(ARG_ITEM, item);
        }
        f.setArguments(b);
        return f;
    }

    public void setResultListener(ResultListener l) {
        this.listener = l;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            edit = (Item) args.getSerializable(ARG_ITEM);
        }

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(22);
        root.setPadding(pad, dp(14), pad, dp(18));

        TextView heading = new TextView(requireContext());
        heading.setTextSize(18);
        heading.setTextColor(getColorCompat(R.color.text));
        heading.setTypeface(heading.getTypeface(), android.graphics.Typeface.BOLD);
        heading.setText(edit == null ? getString(R.string.add_label) : getString(R.string.new_entry));
        heading.setPadding(0, 0, 0, dp(6));
        root.addView(heading);

        titleLayout = fieldRoot(root);
        titleInput = new TextInputEditText(requireContext());
        titleInput.setSingleLine(true);
        titleInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        titleLayout.setHint(Spec.ENTITY_NAME);
        titleLayout.addView(titleInput);
        root.addView(titleLayout);

        final LinearLayout bodyInput;
        if (Spec.HAS_BODY) {
            bodyInput = fieldRoot(root);
            TextInputEditText bodyEdit = new TextInputEditText(requireContext());
            bodyEdit.setSingleLine(false);
            bodyEdit.setMaxLines(4);
            bodyEdit.setMinLines(2);
            bodyInput.setHint(Spec.FIELD_BODY_LABEL);
            bodyInput.addView(bodyEdit);
            root.addView(bodyInput);
        } else {
            bodyInput = null;
        }

        final LinearLayout amountInput;
        if (Spec.HAS_AMOUNT) {
            amountInput = fieldRoot(root);
            TextInputEditText amountEdit = new TextInputEditText(requireContext());
            amountEdit.setSingleLine(true);
            amountEdit.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            amountInput.setHint(Spec.FIELD_AMOUNT_LABEL);
            amountInput.addView(amountEdit);
            root.addView(amountInput);
        } else {
            amountInput = null;
        }

        final LinearLayout categoryInput;
        if (Spec.HAS_CATEGORY) {
            categoryInput = fieldRoot(root);
            TextInputEditText categoryEdit = new TextInputEditText(requireContext());
            categoryEdit.setSingleLine(true);
            categoryInput.setHint(Spec.FIELD_CATEGORY_LABEL);
            categoryInput.addView(categoryEdit);
            root.addView(categoryInput);
        } else {
            categoryInput = null;
        }

        final CheckBox doneBox;
        if (Spec.HAS_DONE) {
            doneBox = new CheckBox(requireContext());
            doneBox.setButtonTintList(android.content.res.ColorStateList.valueOf(getColorCompat(R.color.accent)));
            doneBox.setTextColor(getColorCompat(R.color.text));
            doneBox.setText(Spec.DONE_LABEL);
            doneBox.setPadding(0, dp(8), 0, dp(4));
            root.addView(doneBox);
        } else {
            doneBox = null;
        }

        MaterialButton save = new MaterialButton(requireContext());
        save.setText(getString(R.string.save));
        save.setOnClickListener(v -> {
            Item draft = new Item();
            if (edit != null) {
                draft.id = edit.id;
                draft.created = edit.created;
                draft.updated = edit.updated;
            }
            String title = titleInput.getText() == null ? "" : titleInput.getText().toString();
            if (title.trim().isEmpty()) {
                Toast.makeText(requireContext(), Spec.ENTITY_NAME + " needs a title", Toast.LENGTH_SHORT).show();
                return;
            }
            draft.title = title.trim();

            if (bodyInput != null) {
                EditText bodyEdit = (EditText) bodyInput.getEditText();
                draft.body = bodyEdit == null ? "" : bodyEdit.getText().toString();
            }
            if (amountInput != null) {
                EditText amountEdit = (EditText) amountInput.getEditText();
                String s = amountEdit == null || amountEdit.getText() == null ? "" : amountEdit.getText().toString();
                try {
                    draft.amount = Long.parseLong(s.trim().isEmpty() ? "0" : s.trim());
                } catch (NumberFormatException ignored) {
                    draft.amount = 0;
                }
            }
            if (categoryInput != null) {
                EditText categoryEdit = (EditText) categoryInput.getEditText();
                draft.category = categoryEdit == null ? "" : categoryEdit.getText().toString().trim();
            }
            if (doneBox != null) {
                draft.done = doneBox.isChecked();
            }
            if (listener != null) {
                listener.onSave(draft);
            }
            dismiss();
        });
        root.addView(save);

        if (edit != null) {
            titleInput.setText(edit.title);
            if (bodyInput != null) {
                ((EditText) bodyInput.getEditText()).setText(edit.body == null ? "" : edit.body);
            }
            if (amountInput != null) {
                ((EditText) amountInput.getEditText()).setText(edit.amount == 0 ? "" : Long.toString(edit.amount));
            }
            if (categoryInput != null) {
                ((EditText) categoryInput.getEditText()).setText(edit.category == null ? "" : edit.category);
            }
            if (doneBox != null) {
                doneBox.setChecked(edit.done);
            }
        }
        return root;
    }

    private TextInputLayout fieldRoot(LinearLayout root) {
        TextInputLayout lay = new TextInputLayout(requireContext());
        lay.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        lay.setBoxStrokeColor(getColorCompat(R.color.accent));
        lay.setHintTextColor(getColorCompat(R.color.text_muted));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = dp(12);
        lay.setLayoutParams(lp);
        return lay;
    }

    private int dp(int v) {
        return Math.round(getResources().getDisplayMetrics().density * v);
    }

    private int getColorCompat(int res) {
        return getResources().getColor(res);
    }
}