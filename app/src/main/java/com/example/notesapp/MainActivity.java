package com.example.notesapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button logoutButton;
    FloatingActionButton fabAddNote;
    RecyclerView rvNotes;
    NotesAdapter adapter;
    ArrayList<Note> notesList;
    SharedPreferences prefs;
    ReminderHelper reminderHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.btnLogout);
        fabAddNote = findViewById(R.id.fabAddNote);
        rvNotes = findViewById(R.id.rvNotes);
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        notesList = loadNotes();
        reminderHelper = new ReminderHelper(this);

        adapter = new NotesAdapter(notesList, position -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        notesList.remove(position);
                        saveNotes();
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setAdapter(adapter);

        fabAddNote.setOnClickListener(v -> showAddNoteDialog());

        logoutButton.setOnClickListener(v -> {
            prefs.edit().putBoolean("isLoggedIn", false).apply();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void showAddNoteDialog() {
        EditText etNote = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add Note")
                .setView(etNote)
                .setPositiveButton("Add", (dialog, which) -> {
                    String noteText = etNote.getText().toString().trim();
                    if (!noteText.isEmpty()) {
                        // Ask for reminder
                        showDateTimePicker(noteText);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDateTimePicker(String noteText) {
        final Calendar calendar = Calendar.getInstance();

        // Date picker
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Time picker
            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                long reminderMillis = calendar.getTimeInMillis();
                Note note = new Note(noteText, reminderMillis);
                notesList.add(note);
                saveNotes();
                adapter.notifyDataSetChanged();

                // Set alarm using Calendar (fix)
                reminderHelper.setReminder(calendar, "Note Reminder", noteText);

                Toast.makeText(this, "Note & Reminder added!", Toast.LENGTH_SHORT).show();
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();


        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private ArrayList<Note> loadNotes() {
        ArrayList<Note> list = new ArrayList<>();
        String json = prefs.getString("notes", "[]");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                // Each note stored as JSON object with text and reminderTime
                list.add(new Note(array.getJSONObject(i).getString("text"),
                        array.getJSONObject(i).getLong("reminderTime")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void saveNotes() {
        JSONArray array = new JSONArray();
        for (Note note : notesList) {
            try {
                array.put(new org.json.JSONObject()
                        .put("text", note.getText())
                        .put("reminderTime", note.getReminderTime()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString("notes", array.toString()).apply();
    }
}
