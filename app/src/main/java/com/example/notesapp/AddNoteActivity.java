package com.example.notesapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitle, editContent, editReminder;
    private Button btnSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        editReminder = findViewById(R.id.editReminder);
        btnSave = findViewById(R.id.btnSave);

        // Optional: show Date & Time picker for reminder
        editReminder.setOnClickListener(v -> showDateTimePicker());

        btnSave.setOnClickListener(v -> saveNote());
    }

    private void showDateTimePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    TimePickerDialog timePicker = new TimePickerDialog(this,
                            (timeView, hourOfDay, minute) -> {
                                String reminder = dayOfMonth + "/" + (month + 1) + "/" + year + " " + hourOfDay + ":" + minute;
                                editReminder.setText(reminder);
                            },
                            c.get(Calendar.HOUR_OF_DAY),
                            c.get(Calendar.MINUTE),
                            true);
                    timePicker.show();
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void saveNote() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();
        String reminder = editReminder.getText().toString().trim();

        if (title.isEmpty() && content.isEmpty()) {
            editTitle.setError("Enter title or content");
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("content", content);
        resultIntent.putExtra("reminder", reminder);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
