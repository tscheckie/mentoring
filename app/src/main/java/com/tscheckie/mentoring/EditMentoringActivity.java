package com.tscheckie.mentoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tscheckie.mentoring.database.MenteeDao;
import com.tscheckie.mentoring.database.MenteeDatabase;
import com.tscheckie.mentoring.database.entities.Mentee;
import com.tscheckie.mentoring.database.entities.Mentoring;

import java.lang.ref.WeakReference;
import java.text.BreakIterator;

public class EditMentoringActivity extends AppCompatActivity {

    int index = -1;
    int menteeIndex = -1;

    TextView topicTv;
    TextView dateTv;
    TextView notesTv;

    final static String TAG = "edit mentoring";

    MenteeDao mMenteeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mentoring);

        Toolbar toolbar = findViewById(R.id.editMentoringToolbar);
        setSupportActionBar(toolbar);

        mMenteeDao = MenteeDatabase.getDatabase(getApplicationContext()).menteeDao();

        FloatingActionButton confirmBtn = findViewById(R.id.conFirmMentoringBtn);
        topicTv = findViewById(R.id.editMentoringTopicText);
        dateTv = findViewById(R.id.editMentoringDateText);
        notesTv = findViewById(R.id.editMentoringNotesText);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            String mode = bundle.containsKey("mode") ? String.valueOf(bundle.getString("mode")) : "none";

            if(mode.equals("add")) {

                Log.d(TAG, "adding mentoring ");
                setTitle("Mentoring hinzufügen");

                menteeIndex = bundle.getInt ("mentee", -1 );

                final AddMentoringTask addMentoringTask = new AddMentoringTask(this);

                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Mentoring addMentoring = new Mentoring(topicTv.getText().toString(), dateTv.getText().toString(), notesTv.getText().toString(), menteeIndex);
                        addMentoringTask.execute(addMentoring);
                    }
                });

            } else if(mode.equals("edit")){

                Log.d(TAG, "editing mentoring ");
                setTitle("Mentoring bearbeiten");

                index = bundle.getInt("mentoring" , -1);

                new RetrieveMentoringTask(this).execute(index);

                final EditMentoringTask editMentoringTask = new EditMentoringTask(this);

                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        editMentoringTask.execute(index);

                    }
                });


            }
        }
    }

    private static class EditMentoringTask extends AsyncTask<Integer, Void, Mentoring> {

        WeakReference<EditMentoringActivity> activityWeakReference;

        EditMentoringTask (EditMentoringActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Mentoring doInBackground(Integer ... index) {
            if(activityWeakReference.get() != null) {
                Mentoring original = activityWeakReference.get().mMenteeDao.getMentoringByIndex(index[0]);

                original.setTopic(activityWeakReference.get().topicTv.getText().toString());
                original.setDate(activityWeakReference.get().dateTv.getText().toString());
                original.setNotes(activityWeakReference.get().notesTv.getText().toString());
                activityWeakReference.get().mMenteeDao.updateMentoring(original);
                Log.i(TAG, "updated Mentoring");
                return  original;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Mentoring mentoring) {
            super.onPostExecute(mentoring);

            if(mentoring != null) {
                Intent intent = new Intent (activityWeakReference.get(), MentoringDetailsActivity.class);
                intent.putExtra("mentoring", mentoring.getIndex());
                intent.putExtra("mentee", mentoring.getMenteeIndex());
                activityWeakReference.get().startActivity(intent);
                activityWeakReference.get().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Log.i(TAG, "starting activtiy");
            }
        }
    }

    private class AddMentoringTask extends AsyncTask<Mentoring, Void, Mentoring> {
        WeakReference<EditMentoringActivity> activityWeakReference;

        AddMentoringTask(EditMentoringActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Mentoring doInBackground(Mentoring... mentorings) {
            if(activityWeakReference.get() != null) {
                activityWeakReference.get().mMenteeDao.insertMentoring(mentorings[0]);
                Log.i(TAG, "added Mentoring");
                return  activityWeakReference.get().mMenteeDao.getLastAddedMentoring();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Mentoring mentoring) {
            super.onPostExecute(mentoring);

            if(mentoring != null) {

                Log.i(TAG, "Last added mentoring index: " + mentoring.getIndex());
                Intent intent = new Intent (activityWeakReference.get(), MentoringDetailsActivity.class);
                intent.putExtra("mentoring", mentoring.getIndex());
                intent.putExtra("mentee", mentoring.getMenteeIndex());
                activityWeakReference.get().startActivity(intent);
                activityWeakReference.get().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Log.i(TAG, "starting activtiy");
            }

        }
    }

    private class RetrieveMentoringTask extends AsyncTask <Integer, Void, Mentoring> {

        WeakReference<EditMentoringActivity> activityWeakReference;

        private RetrieveMentoringTask(EditMentoringActivity context) {
            activityWeakReference =  new WeakReference<>(context);
        }

        @Override
        protected Mentoring doInBackground(Integer... integers) {
            if(activityWeakReference.get() != null) {

                Log.i(TAG, "doInBackground Task");
                return activityWeakReference.get().mMenteeDao.getMentoringByIndex(integers[0]);
            } else
                return null;
        }

        @Override
        protected void onPostExecute(Mentoring mentoring) {
            super.onPostExecute(mentoring);

            Log.i(TAG, "Post Execute Task");

            if(mentoring != null) {

                Log.i(TAG, "Post Execute Task != null");

                activityWeakReference.get().topicTv.setText(mentoring.getTopic());
                activityWeakReference.get().dateTv.setText(mentoring.getDate());
                activityWeakReference.get().notesTv.setText(mentoring.getNotes());

                Toast.makeText(activityWeakReference.get(), "Mentoring geladen", Toast.LENGTH_SHORT).show();
            } else {

                Log.i(TAG, "Post Execute Task == null");

                Toast.makeText(activityWeakReference.get(), "Mentoring konnte nicht geladen werden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}