package com.example.notesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private ArrayList<Note> notesList = new ArrayList<>();
    private NotesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set adapter with lambda for delete
        adapter = new NotesAdapter(notesList, position -> deleteNote(position));

        recyclerView.setAdapter(adapter);

        return view;
    }

    // Method to add a new note
    public void addNote(Note note) {
        notesList.add(note);
        adapter.notifyItemInserted(notesList.size() - 1);
    }

    // Method to delete a note
    private void deleteNote(int position) {
        if (notesList != null && position >= 0 && position < notesList.size()) {
            notesList.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Note deleted!", Toast.LENGTH_SHORT).show();
        }
    }
}
