package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private ArrayList<Note> notesList;
    private OnNoteDeleteListener listener;

    public interface OnNoteDeleteListener {
        void onNoteDelete(int position);
    }

    public NotesAdapter(ArrayList<Note> notesList, OnNoteDeleteListener listener) {
        this.notesList = notesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.tvNoteTitle.setText(note.getText());
        holder.cbCompleted.setChecked(note.isCompleted());
        // Strike-through completed notes
        holder.tvNoteTitle.setPaintFlags(note.isCompleted() ?
                holder.tvNoteTitle.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG :
                holder.tvNoteTitle.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));

        if (note.isCompleted()) {
            holder.tvNoteTitle.setPaintFlags(holder.tvNoteTitle.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvNoteTitle.setPaintFlags(holder.tvNoteTitle.getPaintFlags() & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
        }
/*
        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            note.setCompleted(isChecked);
        });*/

        if (note.getReminderTime() > 0) {
            String formattedTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date(note.getReminderTime()));
            holder.tvReminder.setText("⏰ " + formattedTime);
            holder.tvReminder.setVisibility(View.VISIBLE);
        } else {
            holder.tvReminder.setText("Reminder: None");
            holder.tvReminder.setVisibility(View.GONE);
        }

        holder.btnDelete.setOnClickListener(v -> listener.onNoteDelete(position));
        // CheckBox click
        holder.cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            note.setCompleted(isChecked);
            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
/*
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoteTitle, tvReminder;
        ImageButton btnDelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvReminder = itemView.findViewById(R.id.tvReminder);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }*/
public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView tvNoteTitle, tvReminder;
    ImageButton btnDelete;
    CheckBox cbCompleted; // new

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
        tvReminder = itemView.findViewById(R.id.tvReminder);
        btnDelete = itemView.findViewById(R.id.btnDelete);
        cbCompleted = itemView.findViewById(R.id.cbCompleted);
    }
}

}
