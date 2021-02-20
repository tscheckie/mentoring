package com.tscheckie.mentoring;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tscheckie.mentoring.R.id;
import com.tscheckie.mentoring.database.entities.Mentee;

import java.util.List;

public class MenteeListAdapter extends RecyclerView.Adapter<MenteeListAdapter.MenteeViewHolder> {

    private List<Mentee> mMentees;
    private Context mContext;
    private Activity mActivity;

    public MenteeListAdapter(List<Mentee> data, Context context) {
        mMentees = data;
        mContext = context;
        mActivity = (Activity) context;
        
    }

    @NonNull
    @Override
    public MenteeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.mentee_list_item, parent, false);

        return new MenteeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenteeViewHolder holder, final int position) {
        final Mentee mentee = mMentees.get(position);

        String name = String.valueOf(mentee.getName());

        holder.mName.setText(name);

     

        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MenteeDetailsActivity.class);
                intent.putExtra("index", mentee.getIndex());

                mContext.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }) ;
    }

    @Override
    public int getItemCount() {
        return mMentees.size();
    }

    public class MenteeViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mItem;
        TextView mName;

        public MenteeViewHolder(@NonNull View itemView) {
            super(itemView);
            mItem = itemView.findViewById(id.menteeItem);
            mName = itemView.findViewById(R.id.menteeItemName);

        }
    }
}
