package com.yveschiong.easycalendar.sample.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yveschiong.easycalendar.sample.R;
import com.yveschiong.easycalendar.sample.presenters.ActivityListPresenter;
import com.yveschiong.easycalendar.sample.viewholders.ActivityListViewHolder;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListViewHolder> {

    private ActivityListPresenter presenter;

    public ActivityListAdapter(@NonNull ActivityListPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ActivityListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ActivityListViewHolder holder, int position) {
        presenter.populate(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }
}
