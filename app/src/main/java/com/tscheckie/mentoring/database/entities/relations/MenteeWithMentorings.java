package com.tscheckie.mentoring.database.entities.relations;


import androidx.room.Embedded;
import androidx.room.Relation;

import com.tscheckie.mentoring.database.entities.Mentee;
import com.tscheckie.mentoring.database.entities.Mentoring;

import java.util.List;

public class MenteeWithMentorings {


    @Embedded public Mentee mentee;

    @Relation(
        parentColumn = "index",
        entityColumn = "menteeIndex"
    )
    public List<Mentoring> mentorings;
}
