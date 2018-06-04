package com.yveschiong.easycalendar.sample.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yveschiong.easycalendar.sample.R;
import com.yveschiong.easycalendar.sample.interfaces.ActivityListViewInterface;

public class ActivityListViewHolder extends RecyclerView.ViewHolder implements ActivityListViewInterface {

    private TextView nameTextView;
    private TextView descriptionTextView;

    public ActivityListViewHolder(View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.name);
        descriptionTextView = itemView.findViewById(R.id.description);
    }

    @Override
    public void setName(String name) {
        nameTextView.setText(name);
    }

    @Override
    public void setDescription(String description) {
        descriptionTextView.setText(description);
    }
}
