package com.tscheckie.mentoring.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabase.Callback;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tscheckie.mentoring.database.entities.Mentee;
import com.tscheckie.mentoring.database.entities.Mentoring;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Mentee.class, Mentoring.class},
        version = 1,
        exportSchema = false
)
public abstract class MenteeDatabase extends RoomDatabase {

    public abstract MenteeDao menteeDao();

    private static volatile MenteeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService dataBaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MenteeDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (MenteeDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MenteeDatabase.class, "mentee_db").addCallback(menteeDatabaseCallback).build();
                }
            }
        }

        return INSTANCE;
    }

    private static Callback menteeDatabaseCallback = new Callback() {


        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            dataBaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    MenteeDao dao = INSTANCE.menteeDao();
                    dao.deleteAllMentees();
                    dao.deleteAllMentorings();
                }
            });
        }
    };
}
