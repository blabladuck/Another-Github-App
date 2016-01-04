package com.nosleep.githubclient.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nosleep.githubclient.R;

import com.nosleep.githubclient.utils.CircleTransform;

/**
 * Created by ssub3 on 12/30/15.
 */
public class WelcomeUserFragment extends Fragment {

    public static final String EXTRA_AVATAR_URL = "EXTRA_AVATAR_URL";
    public static final String EXTRA_USER_NAME = "EXTRA_USER_NAME";
    public static final String EXTRA_TOKEN = "EXTRA_TOKEN";

    public static final WelcomeUserFragment newInstance(@Nullable Bundle args) {
        WelcomeUserFragment frag = new WelcomeUserFragment();
        frag.setArguments(args);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_welcomeuser, container, false);
        ImageView avatar = (ImageView) rootview.findViewById(R.id.avatar);
        Bundle args = getArguments();
        final String cacheAvatar = args.getString(EXTRA_AVATAR_URL);
        final String cacheUsername = args.getString(EXTRA_USER_NAME);
        Glide.with(this).load(cacheAvatar).transform(new CircleTransform(getContext())).into(avatar);
        TextView username = (TextView) rootview.findViewById(R.id.username);
        username.setText("Welcome! " + cacheUsername);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToHomeScreen(cacheUsername, cacheAvatar);
            }
        }, 2000);
        return rootview;
    }

    void navigateToHomeScreen(String cacheUsername, String cacheAvatar) {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("avatar_url", cacheAvatar);
        intent.putExtra("username", cacheUsername);
        intent.putExtra("myrepos", "");
        startActivity(intent);
        getActivity().finish();
    }

}
