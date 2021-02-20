package com.tscheckie.mentoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tscheckie.mentoring.database.MenteeDao;
import com.tscheckie.mentoring.database.MenteeDatabase;
import com.tscheckie.mentoring.database.entities.Mentee;
import com.tscheckie.mentoring.database.entities.Mentoring;
import com.tscheckie.mentoring.database.entities.relations.MenteeWithMentorings;

import java.lang.ref.WeakReference;
import java.util.List;

public class MenteeDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MenteeDetails";
    int index = -1;
    TextView goalTv;
    TextView timeSpanTv;
    TextView rhythmTv;

    MentoringListAdapter mAdapter;
    MenteeDao mMenteeDao;
    Mentee mMentee;
    ImageButton editMentee;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentee_details);

        Toolbar toolbar = findViewById(R.id.menteeDetailsToolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        goalTv = findViewById(R.id.menteeDetailsGoal);
        timeSpanTv = findViewById(R.id.menteeDetailsTimeSpan);
        rhythmTv = findViewById(R.id.menteeDetailsRhythm);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
         index = bundle.getInt("index");
        }

        mMenteeDao = MenteeDatabase.getDatabase(this).menteeDao();

        new RetrieveMenteeTask(this).execute(index);

        editMentee = findViewById(R.id.editMenteeBtn);

        recyclerView = findViewById(R.id.mentoringList);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton addMentoringBtn = findViewById(R.id.addMentoringBtn);

        addMentoringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditMentoringActivity.class);
                intent.putExtra("mentee", index);
                intent.putExtra("mode", "add");
                startActivity(intent);
            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class RetrieveMenteeTask extends AsyncTask<Integer, Void, MenteeWithMentorings> {

        private WeakReference<MenteeDetailsActivity> activityWeakReference;

        RetrieveMenteeTask (MenteeDetailsActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected MenteeWithMentorings doInBackground(Integer... integers) {
            if(activityWeakReference.get() != null) {

                Log.i(TAG, "doInBackground Task");

                return activityWeakReference.get().mMenteeDao.getMenteeWithMentorings(integers[0]);
            } else
                return null;
        }

        @Override
        protected void onPostExecute(final MenteeWithMentorings menteeWithMentorings) {
            super.onPostExecute(menteeWithMentorings);

            Log.i(TAG, "Post Execute Task");

            if(menteeWithMentorings != null) {

                Log.i(TAG, "Post Execute Task != null");
                Mentee mentee = menteeWithMentorings.mentee;
                activityWeakReference.get().mMentee = mentee;
                activityWeakReference.get().setTitle(mentee.getName());
                activityWeakReference.get().goalTv.setText("Ziel: " + mentee.getGoal());
                activityWeakReference.get().timeSpanTv.setText("Zeitraum: " + mentee.getTimeSpan());
                activityWeakReference.get().rhythmTv.setText("Rhythmus: " + mentee.getRhythm());


                activityWeakReference.get().editMentee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activityWeakReference.get(), EditMenteeActivity.class);
                        intent.putExtra("index", menteeWithMentorings.mentee.getIndex());
                        startActivity(intent);
                    }
                });


                activityWeakReference.get().mAdapter = new MentoringListAdapter(menteeWithMentorings, activityWeakReference.get());
                activityWeakReference.get().recyclerView.setAdapter(mAdapter);
                activityWeakReference.get().mAdapter.notifyDataSetChanged(); // just to make sure the data is up to date

                Toast.makeText(activityWeakReference.get(), "Daten geladen", Toast.LENGTH_SHORT).show();
            } else {

                Log.i(TAG, "Post Execute Task == null");

                activityWeakReference.get().editMentee.setVisibility(View.INVISIBLE);
                Toast.makeText(activityWeakReference.get(), "Daten konnten nicht geladen werden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}