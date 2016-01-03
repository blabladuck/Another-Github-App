package github.blabladuck.com.another_github_app.login.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import github.blabladuck.com.another_github_app.R;
import utils.CircleTransform;

/**
 * Created by ssub3 on 12/29/15.
 */
public class HomeActivity extends MasterTemplateActivity {
    private static final String TAG = "HomeActivity";
    private DrawerLayout mDrawerLayout;

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
        ImageView profilepic = (ImageView)header.findViewById(R.id.avatar);
        String avatar_url = getIntent().getStringExtra("avatar_url");
        String username = getIntent().getStringExtra("username");
        String reposUrl = getIntent().getStringExtra("myrepos");
        Log.d(TAG,reposUrl);
        TextView txtUsername = (TextView)header.findViewById(R.id.username);
        txtUsername.setText("Welcome! "+username);
        Glide.with(this).load(avatar_url).transform(new CircleTransform(this)).into(profilepic);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


    }

    private void setupDrawerContent(NavigationView navigationView) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
