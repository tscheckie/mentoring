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
import com.tscheckie.mentoring.database.entities.Mentoring;

import java.lang.ref.WeakReference;

public class MentoringDetailsActivity extends AppCompatActivity {


    private static final String TAG = "MentoringDetails";

    int menteeIndex;
    Mentoring mentoring;
    int mentoringIndex;

    TextView topicTV;
    TextView dateTV;
    TextView notesTV;

    MenteeDao mMenteeDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentoring_details);

        topicTV = findViewById(R.id.mentoringDetailsTopic);
        dateTV = findViewById(R.id.mentoringDetailsDate);
        notesTV = findViewById(R.id.mentoringDetailsNotes);

        Toolbar toolbar = findViewById(R.id.mentoringDetailsToolbar);
        setSupportActionBar(toolbar);

        mMenteeDao = MenteeDatabase.getDatabase(getApplicationContext()).menteeDao();

        setTitle(getString(R.string.mentoringDetails));

        FloatingActionButton editMentoringBtn = findViewById(R.id.editMentoringBtn);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {

            menteeIndex = bundle.getInt("mentee", -1);
            mentoringIndex = bundle.getInt("mentoring", -1);
            new RetrieveMentoringTask(this).execute(mentoringIndex);

            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MenteeDetailsActivity.class);
                    intent.putExtra("index", menteeIndex);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });

            editMentoringBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EditMentoringActivity.class);
                    intent.putExtra("mode", "edit");
                    intent.putExtra("mentee", menteeIndex);
                    intent.putExtra("mentoring", mentoringIndex);

                    startActivity(intent);
                }
            });
        } else {
            editMentoringBtn.setVisibility(View.GONE);

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class RetrieveMentoringTask extends AsyncTask <Integer, Void, Mentoring> {

        WeakReference<MentoringDetailsActivity> activityWeakReference;

        private RetrieveMentoringTask(MentoringDetailsActivity context) {
            activityWeakReference =  new WeakReference<>(context);
        }

        @Override
        protected Mentoring doInBackground(Integer... integers) {
            if(activityWeakReference.get() != null) {

                Log.i(TAG, "doInBackground Task, index : " + integers[0]);
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

                activityWeakReference.get().mentoring = mentoring;
                activityWeakReference.get().topicTV.setText(mentoring.getTopic());
                activityWeakReference.get().dateTV.setText(mentoring.getDate());
                activityWeakReference.get().notesTV.setText(mentoring.getNotes());

                Toast.makeText(activityWeakReference.get(), "Mentoring geladen", Toast.LENGTH_SHORT).show();
            } else {

                Log.i(TAG, "Post Execute Task == null");

                Toast.makeText(activityWeakReference.get(), "Mentoring konnte nicht geladen werden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}