package com.tscheckie.mentoring.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "mentee_table")
public class Mentee {

        @PrimaryKey (autoGenerate = true)
        private int index;

        @ColumnInfo
        private String name;

        @ColumnInfo
        private String goal;

        @ColumnInfo
        private String timeSpan;

        @ColumnInfo
        private String rhythm;

        public Mentee( @NonNull String name, @NonNull String goal, @NonNull String timeSpan, @NonNull String rhythm) {
            this.name = name;
            this.goal = goal;
            this.timeSpan = timeSpan;
            this.rhythm = rhythm;
        }

        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getGoal() {
            return goal;
        }
        public void setGoal(String goal) {
            this.goal = goal;
        }

        public String getTimeSpan() {
            return timeSpan;
        }
        public void setTimeSpan(String timeSpan) {
            this.timeSpan = timeSpan;
        }

        public String getRhythm() {
            return rhythm;
        }
        public void setRhythm(String rhythm) {
            this.rhythm = rhythm;
        }

}

