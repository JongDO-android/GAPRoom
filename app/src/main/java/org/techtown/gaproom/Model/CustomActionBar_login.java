package org.techtown.gaproom.Model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import org.techtown.gaproom.R;

public class CustomActionBar_login {
    private Activity activity;
    private ActionBar actionBar;

    public CustomActionBar_login(Activity activity, ActionBar actionBar){
        this.activity = activity;
        this.actionBar = actionBar;
    }

    public void setActionBar(){
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        View mCustomView = LayoutInflater.from(activity).inflate(R.layout.custom_actionbar_login, null);
        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT
                , ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setCustomView(mCustomView, params);
    }
}