package com.tscheckie.mentoring.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.tscheckie.mentoring.database.entities.Mentee;
import com.tscheckie.mentoring.database.entities.Mentoring;
import com.tscheckie.mentoring.database.entities.relations.MenteeWithMentorings;

import java.util.List;

@Dao
public interface MenteeDao {

    // Mentees
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insertMentee(Mentee mentee);

    @Query("DELETE FROM mentee_table")
    void deleteAllMentees();

    @Query("SELECT * FROM mentee_table ORDER BY name ASC")
    List<Mentee> getAphabetizedMentees();
    //LiveData<List<Mentee>> getAphabetizedMentees();

    @Query("SELECT * FROM mentee_table WHERE `index` = :index")
    Mentee getMenteeByIndex(int index);
    //LiveData<Mentee> getMenteeByIndex(int index);

    @Query("SELECT * FROM mentee_table ORDER BY `index` DESC LIMIT 1")
    //LiveData<Mentee> getLastAddedMentee();
    Mentee getLastAddedMentee();

    @Update
    void updateMentee(Mentee mentee);

    // Mentorings
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insertMentoring (Mentoring mentoring);

    @Query("DELETE FROM mentoring_table")
    void deleteAllMentorings();

    @Query("SELECT * FROM mentoring_table WHERE `index` = :index")
    //LiveData<Mentoring> getMentoringByIndex (int index);
    Mentoring getMentoringByIndex (int index);

    @Update
    void updateMentoring(Mentoring mentoring);

    @Query("SELECT * FROM mentoring_table ORDER BY `index` DESC LIMIT 1")
    Mentoring getLastAddedMentoring();
    //LiveData<Mentoring> getLastAddedMentoring();

    // Mentees with Mentorings

    @Transaction
    @Query("SELECT * FROM mentee_table WHERE `index` = :menteeIndex LIMIT 1")
    MenteeWithMentorings getMenteeWithMentorings(int menteeIndex);
    //LiveData<List<MenteeWithMentorings>> getMenteeWithMentorings(int menteeIndex);



}
