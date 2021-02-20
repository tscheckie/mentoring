package com.tscheckie.mentoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tscheckie.mentoring.database.MenteeDao;
import com.tscheckie.mentoring.database.MenteeDatabase;
import com.tscheckie.mentoring.database.entities.Mentee;

import java.lang.ref.WeakReference;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MenteeListAdapter mAdapter;
    MenteeDao mMenteeDao;
    List<Mentee> mMentees;
    RecyclerView mRecyclerView;

    View actionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMenteeDao = MenteeDatabase.getDatabase(this).menteeDao();

        Toolbar mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        setTitle("Übersicht");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        actionBtn = findViewById(R.id.addMenteeBtn);

        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditMenteeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetMenteeListTask(this).execute();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private static class GetMenteeListTask extends AsyncTask<Void, Void, List<Mentee>> {

        private WeakReference<MainActivity> activityWeakReference;

        GetMenteeListTask (MainActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected List<Mentee> doInBackground(Void ...voids) {
            if(activityWeakReference.get() != null) {
                return activityWeakReference.get().mMenteeDao.getAphabetizedMentees();
            } else
                return null;
        }

        @Override
        protected void onPostExecute(List<Mentee> mentees) {
            super.onPostExecute(mentees);


            if(mentees != null && mentees.size() > 0) {

                activityWeakReference.get().mMentees = mentees;

                activityWeakReference.get().mAdapter = new MenteeListAdapter(mentees, activityWeakReference.get());
                activityWeakReference.get().mRecyclerView.setAdapter(activityWeakReference.get().mAdapter);
                activityWeakReference.get().mAdapter.notifyDataSetChanged();

                Toast.makeText(activityWeakReference.get(), "Alle Mentees geladen", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(activityWeakReference.get(), "Keine Mentees gefunden", Toast.LENGTH_SHORT).show();
            }

            Log.i(TAG, "Mentees geladen");
        }
    }






}