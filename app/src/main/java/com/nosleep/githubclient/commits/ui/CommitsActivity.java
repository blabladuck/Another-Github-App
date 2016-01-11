package com.nosleep.githubclient.commits.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nosleep.githubclient.R;

/**
 * Created by Sanjeev on 09/01/16.
 */
public class CommitsActivity extends AppCompatActivity {
    public static final String EXTRA_REPO = "EXTRA_REPO";

    public static final String EXTRA_OWNER = "EXTRA_OWNER";
    public static final String EXTRA_BRANCH = "EXTRA_BRANCH";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commits);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
