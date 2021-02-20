package com.tscheckie.mentoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tscheckie.mentoring.database.entities.Mentee;
import com.tscheckie.mentoring.database.entities.Mentoring;
import com.tscheckie.mentoring.database.entities.relations.MenteeWithMentorings;

import java.util.ArrayList;
import java.util.List;

public class MentoringListAdapter extends RecyclerView.Adapter<MentoringListAdapter.MentoringViewHolder> {

    private static final String TAG = "Mentoring List Adapter";

    private List<Mentoring> mMentorings;
    private Mentee mMentee;
    private Context mContext;
    private Activity mActivity;

    public MentoringListAdapter(MenteeWithMentorings menteeWithMentorings, Context context) {

        mMentee = menteeWithMentorings.mentee;
        mMentorings = menteeWithMentorings.mentorings;
        mContext = context;
        mActivity = (Activity) context;
    }

    @NonNull
    @Override
    public MentoringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.mentoring_list_item, parent, false);

        return new MentoringViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MentoringViewHolder holder, final int position) {
        final Mentoring mentoring = mMentorings.get(position);

        String topic = String.valueOf(mentoring.getTopic());
        String date = String.valueOf(mentoring.getDate());

        holder.mTopic.setText(topic);
        holder.mDate.setText(date);

        Log.i(TAG,"Binding Mentoring Index: " + mentoring.getIndex() + " and mentee: " + mentoring.getMenteeIndex() + " at position " + position);

        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MentoringDetailsActivity.class);
                intent.putExtra("mode", "edit");
                intent.putExtra("mentoring", mentoring.getIndex());
                intent.putExtra("mentee", mentoring.getMenteeIndex());
                mContext.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }) ;

    }

    @Override
    public int getItemCount() {
        return mMentorings.size();
    }

    public class MentoringViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mItem;
        TextView mTopic;
        TextView mDate;

        public MentoringViewHolder(@NonNull View itemView) {
            super(itemView);

            mItem = itemView.findViewById(R.id.mentoringItem);
            mTopic = itemView.findViewById(R.id.mentoringTopic);
            mDate = itemView.findViewById(R.id.mentoringDate);
        }
    }
}
