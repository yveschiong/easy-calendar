package com.yveschiong.easycalendar.sample.presenters;

import android.support.annotation.NonNull;

import com.yveschiong.easycalendar.sample.helpers.ActivityHelper;
import com.yveschiong.easycalendar.sample.interfaces.ActivityListViewInterface;
import com.yveschiong.easycalendar.sample.models.Module;

import java.util.List;

public class ActivityListPresenter {
    private List<Module> moduleList;
    private ActivityHelper activityHelper;

    public ActivityListPresenter(@NonNull List<Module> moduleList, @NonNull ActivityHelper activityHelper) {
        this.moduleList = moduleList;
        this.activityHelper = activityHelper;
    }

    public void populate(ActivityListViewInterface viewInterface, int position) {
        Module module = moduleList.get(position);
        viewInterface.setName(module.getName());
        viewInterface.setDescription(module.getDescription());
    }

    public void clicked(int position) {
        activityHelper.startActivity(moduleList.get(position).getModuleClass());
    }

    public int getItemCount() {
        return moduleList.size();
    }
}
