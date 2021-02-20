package com.tscheckie.mentoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
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
import com.tscheckie.mentoring.database.entities.relations.MenteeWithMentorings;

import java.lang.ref.WeakReference;

public class EditMenteeActivity extends AppCompatActivity {

    int index = -1;
    TextView nameTv;
    TextView goalTv;
    TextView timeSpanTv;
    TextView rhythmTv;
    final static String TAG = "edit mentee";
    MenteeDao mMenteeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mentee);

        Toolbar toolbar = findViewById(R.id.editMenteeToolbar);
        setSupportActionBar(toolbar);

        mMenteeDao = MenteeDatabase.getDatabase(getApplicationContext()).menteeDao();

        FloatingActionButton confirmBtn = findViewById(R.id.confirmMenteeBtn);
        nameTv = findViewById(R.id.editMenteeNameText);
        goalTv = findViewById(R.id.editMenteeGoalText);
        timeSpanTv = findViewById(R.id.editMenteeTimeSpanText);
        rhythmTv = findViewById(R.id.editMenteeRhythmText);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            Log.d(TAG, "editing mentee ");
            setTitle(R.string.editMentee);

            index = bundle.getInt("index");

            new RetrieveMenteeTask(this).execute(index);

            final EditMenteeTask editMenteeTask = new EditMenteeTask(this);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editMenteeTask.execute(index);

                }
            });

        } else {
            Log.d(TAG, "adding mentee ");
            setTitle(R.string.addMentee);

            final AddMenteeTask addMenteeTask = new AddMenteeTask(this);

            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Mentee addMentee = new Mentee(nameTv.getText().toString(), goalTv.getText().toString(), timeSpanTv.getText().toString(), rhythmTv.getText().toString());
                    addMenteeTask.execute(addMentee);

                }
            });
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private static class AddMenteeTask extends AsyncTask<Mentee, Void, Integer> {

        WeakReference<EditMenteeActivity> activityWeakReference;

        AddMenteeTask (EditMenteeActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }


        @Override
        protected Integer doInBackground(Mentee ... mentee) {
            if(activityWeakReference.get() != null) {
                activityWeakReference.get().mMenteeDao.insertMentee(mentee[0]);
                Log.i(TAG, "added Mentee");
                return  activityWeakReference.get().mMenteeDao.getLastAddedMentee().getIndex();

            }
            return null;
        }


        @Override
        protected void onPostExecute(Integer index) {
            super.onPostExecute(index);

            if(index != null) {
                Intent intent = new Intent (activityWeakReference.get(), MenteeDetailsActivity.class);
                intent.putExtra("index", index);
                activityWeakReference.get().startActivity(intent);
                activityWeakReference.get().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Log.i(TAG, "starting activtiy");
            }
        }
    }

    private static class EditMenteeTask extends AsyncTask<Integer, Void, Integer> {

        WeakReference<EditMenteeActivity> activityWeakReference;

        EditMenteeTask (EditMenteeActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Integer doInBackground(Integer ... index) {
            if(activityWeakReference.get() != null) {
                Mentee original = activityWeakReference.get().mMenteeDao.getMenteeByIndex(index[0]);

                original.setName(activityWeakReference.get().nameTv.getText().toString());
                original.setGoal(activityWeakReference.get().goalTv.getText().toString());
                original.setTimeSpan(activityWeakReference.get().timeSpanTv.getText().toString());
                original.setRhythm(activityWeakReference.get().rhythmTv.getText().toString());
                activityWeakReference.get().mMenteeDao.updateMentee(original);
                Log.i(TAG, "updated Mentee");
                return  original.getIndex();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer index) {
            super.onPostExecute(index);

            if(index != null) {
                Intent intent = new Intent (activityWeakReference.get(), MenteeDetailsActivity.class);
                intent.putExtra("index", index);
                activityWeakReference.get().startActivity(intent);
                activityWeakReference.get().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Log.i(TAG, "starting activtiy");
            }
        }
    }

    private static class RetrieveMenteeTask extends AsyncTask<Integer, Void, Mentee> {

        private WeakReference<EditMenteeActivity> activityWeakReference;

        RetrieveMenteeTask (EditMenteeActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Mentee doInBackground(Integer... integers) {
            if(activityWeakReference.get() != null) {

                Log.i(TAG, "doInBackground Task");

                return activityWeakReference.get().mMenteeDao.getMenteeByIndex(integers[0]);
            } else
                return null;
        }

        @Override
        protected void onPostExecute(final Mentee mentee) {
            super.onPostExecute(mentee);

            Log.i(TAG, "Post Execute Task");

            if(mentee != null) {

                Log.i(TAG, "Post Execute Task != null");

                activityWeakReference.get().nameTv.setText(mentee.getName());
                activityWeakReference.get().goalTv.setText(mentee.getGoal());
                activityWeakReference.get().timeSpanTv.setText(mentee.getTimeSpan());
                activityWeakReference.get().rhythmTv.setText(mentee.getRhythm());

                Toast.makeText(activityWeakReference.get(), "Daten geladen", Toast.LENGTH_SHORT).show();
            } else {

                Log.i(TAG, "Post Execute Task == null");

                Toast.makeText(activityWeakReference.get(), "Daten konnten nicht geladen werden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}