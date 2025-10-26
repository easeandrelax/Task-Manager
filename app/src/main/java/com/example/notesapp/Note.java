package com.example.notesapp;

public class Note {
    private String text;
    private long reminderTime; // 0 = no reminder
    private boolean completed;
    public Note(String text) {
        this.text = text;
        this.reminderTime = 0;
        this.completed = false;
    }

    public Note(String text, long reminderTime) {
        this.text = text;
        this.reminderTime = reminderTime;
        this.completed = false;
    }

    public String getText() {
        return text;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    @Override
    public String toString() {
        return "Note{" +
                "text='" + text + '\'' +
                ", reminderTime=" + reminderTime +
                ", completed=" + completed +
                '}';
    }


}
