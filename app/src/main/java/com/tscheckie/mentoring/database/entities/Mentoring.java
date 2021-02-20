package com.tscheckie.mentoring.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "mentoring_table")
public class Mentoring {

    @PrimaryKey (autoGenerate = true)
    public int index;

    @ColumnInfo
    public int menteeIndex;

    @ColumnInfo
    private String topic;

    @ColumnInfo
    private String date;

    @ColumnInfo
    private String notes;

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public int getMenteeIndex() { return menteeIndex; }
    public void setMenteeIndex(int menteeIndex) { this.menteeIndex = menteeIndex; }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Mentoring (String topic, String date, String notes, int menteeIndex) {
        this.topic = topic;
        this.date = date;
        this.notes = notes;
        this.menteeIndex = menteeIndex;
    }

}
