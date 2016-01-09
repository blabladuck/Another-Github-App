package com.nosleep.githubclient.home.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nosleep.githubclient.R;

import com.nosleep.githubclient.business.BusinessInjector;
import com.nosleep.githubclient.business.MyRepos;
import com.nosleep.githubclient.datalayer.services.ServiceInjector;
import com.nosleep.githubclient.datalayer.storage.InMemoryStorage;
import com.nosleep.githubclient.datalayer.storage.StorageInjector;
import com.nosleep.githubclient.home.HomeContract;
import com.nosleep.githubclient.home.presenter.HomePresenter;
import com.nosleep.githubclient.utils.CircleTransform;
import com.nosleep.githubclient.utils.MasterTemplateActivity;

import java.util.List;

/**
 * Created by ssub3 on 12/29/15.
 */
public class HomeActivity extends MasterTemplateActivity implements HomeContract.WelcomeView, WelcomeUserFragment.onWelcomeAnimationListener, ReposFragment.ReposActionListener {
    private static final String TAG = "HomeActivity";
    private DrawerLayout mDrawerLayout;
    private ImageView profilepic;
    private TextView txtusername;
    private HomeContract.UserAction userAction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
        ab.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        profilepic = (ImageView) header.findViewById(R.id.avatar);
        txtusername = (TextView) header.findViewById(R.id.username);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        BusinessInjector businessInjector = BusinessInjector.getInstance(this);
        Bundle bundle = new Bundle();
        WelcomeUserFragment frag = WelcomeUserFragment.newInstance(bundle);
        frag.registerWelcomeAnimationListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.contentPanel, frag, "welcome").
                commit();
        userAction = new HomePresenter(this, businessInjector.getUserProfileBusiness(), businessInjector.getReposBusiness());
        userAction.getUser();
    }

    private void setupDrawerContent(NavigationView navigationView) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showWelcomeScreen(String username, String avatar) {
        Log.d(TAG,"avatar = "+avatar);
        txtusername.setText(username);
        Glide.with(this).load(avatar).transform(new CircleTransform(this)).into(profilepic);
        WelcomeUserFragment welcomeUserFragment = (WelcomeUserFragment) getSupportFragmentManager().findFragmentByTag("welcome");
        welcomeUserFragment.displayWelcome(username, avatar);
    }

    @Override
    public void showReposScreen(List<MyRepos.RepoInfo> info) {
        ReposFragment fragment = (ReposFragment) getSupportFragmentManager().findFragmentByTag("repos");
        fragment.displayRepos(info);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onWelcomeAnimationComplete(String avatarurl) {
        Bundle bundle = new Bundle();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentPanel, ReposFragment.newInstance(bundle), "repos").
                commit();
    }

    @Override
    public void onRepoRefreshPulled() {
        userAction.getRepos(true);
    }

    @Override
    public void onRepoViewCreated() {
        userAction.getRepos(false);
    }
}
